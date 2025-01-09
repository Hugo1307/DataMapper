package dev.hugog.libs.java.datamapper;

import dev.hugog.libs.java.datamapper.dbdata.DatabaseData;
import dev.hugog.libs.java.datamapper.discovery.DataMapperDiscoveryService;
import dev.hugog.libs.java.datamapper.dtos.Dto;
import dev.hugog.libs.java.datamapper.mappers.AbstractMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class DataMapper {

    private final List<AbstractMapper<?, ?>> mappers = new ArrayList<>();

    /**
     * Maps a DTO to DatabaseData.
     *
     * @param classToMap DTO to map
     * @param <T>        Type of DataModel to map to
     * @param <C>        Type of Dto to map from
     * @return mapped Model
     */
    @SuppressWarnings("unchecked") // The cast is safe because we check the types using the filter clause
    public <T extends DatabaseData, C extends Dto> T map(C classToMap, Class<T> targetClass) {
        if (classToMap == null) {
            return null;
        }

        Optional<? extends AbstractMapper<T, C>> mapper = mappers.stream()
                .filter(m -> m.getTransferObjectClass().equals(classToMap.getClass()) && m.getDatabaseDataClass().equals(targetClass))
                .map(m -> (AbstractMapper<T, C>) m)
                .findFirst();

        return mapper.map(m -> m.toDataObject(classToMap))
                .orElseThrow(() -> new IllegalArgumentException("No mapper found: " + classToMap.getClass().getName() + " -> " + targetClass.getName()));
    }

    /**
     * Maps Database Data to a DTO.
     *
     * @param classToMap  data model to map
     * @param targetClass the class of the DTO to map to
     * @param <T>         type of Dto to map to
     * @param <C>         type of DataModel to map from
     * @return mapped DTO
     */
    @SuppressWarnings("unchecked") // The cast is safe because we check the types using the filter clause
    public <T extends Dto, C extends DatabaseData> T map(C classToMap, Class<T> targetClass) {
        if (classToMap == null) {
            return null;
        }

        Optional<? extends AbstractMapper<C, T>> mapper = mappers.stream()
                .filter(m -> m.getDatabaseDataClass().equals(classToMap.getClass()) && m.getTransferObjectClass().equals(targetClass))
                .map(m -> (AbstractMapper<C, T>) m)
                .findFirst();

        return mapper.map(m -> m.toTransferObject(classToMap))
                .orElseThrow(() -> new IllegalArgumentException("No mapper found: " + classToMap.getClass().getName() + " -> " + targetClass.getName()));
    }

    /**
     * Maps a list of DatabaseData to DTOs or vice versa.
     *
     * @param classesToMap list to map
     * @param targetClass  the target class
     * @param <C>          type representing a DTO class
     * @param <D>          type representing a DatabaseData class
     * @param <E>          type representing a DataObject class
     * @return the mapped list
     */
    @SuppressWarnings("unchecked") // The casts are safe because we check the types using the 'if' statements
    public <C extends Dto, D extends DatabaseData, E extends DataObject> List<E> mapAll(List<? extends DataObject> classesToMap,
                                                                                        Class<? extends DataObject> targetClass) {
        if (classesToMap == null || targetClass == null) {
            return null;
        }
        if (classesToMap.isEmpty()) {
            return new ArrayList<>();
        }
        if (Dto.class.isAssignableFrom(targetClass)) {
            return (List<E>) classesToMap.stream()
                    .map(classToMap -> map((D) classToMap, (Class<C>) targetClass))
                    .map(targetClass::cast)
                    .collect(Collectors.toList());
        } else if (DatabaseData.class.isAssignableFrom(targetClass)) {
            return (List<E>) classesToMap.stream()
                    .map(classToMap -> map((C) classToMap, (Class<D>) targetClass))
                    .map(targetClass::cast)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid target class: " + targetClass.getName());
        }
    }

    /**
     * Maps a set of DatabaseData to DTOs or vice versa.
     *
     * @param classesToMap set to map
     * @param targetClass  the target class
     * @param <C>          type representing a DTO class
     * @param <D>          type representing a DatabaseData class
     * @param <E>          type representing a DataObject class
     * @return the mapped set
     */
    @SuppressWarnings("unchecked") // The casts are safe because we check the types using the 'if' statements
    public <C extends Dto, D extends DatabaseData, E extends DataObject> Set<E> mapAll(Set<? extends DataObject> classesToMap,
                                                                                    Class<? extends DataObject> targetClass) {
        if (classesToMap == null || targetClass == null) {
            return null;
        }
        if (classesToMap.isEmpty()) {
            return new HashSet<>();
        }
        if (Dto.class.isAssignableFrom(targetClass)) {
            return (Set<E>) classesToMap.stream()
                    .map(classToMap -> map((C) classToMap, (Class<D>) targetClass))
                    .collect(Collectors.toCollection(HashSet::new));
        } else if (DatabaseData.class.isAssignableFrom(targetClass)) {
            return (Set<E>) classesToMap.stream()
                    .map(classToMap -> map((D) classToMap, (Class<C>) targetClass))
                    .collect(Collectors.toCollection(HashSet::new));
        } else {
            throw new IllegalArgumentException("Invalid target class: " + targetClass.getName());
        }
    }

    public void registerMapper(Class<? extends AbstractMapper<?, ?>> mapper) {
        try {
            mappers.add(mapper.getDeclaredConstructor(DataMapper.class).newInstance(this));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerMappersWithDiscovery(String applicationBasePackage) {
        mappers.addAll(new DataMapperDiscoveryService(this, applicationBasePackage).autoRegisterDataMappers());
    }

}