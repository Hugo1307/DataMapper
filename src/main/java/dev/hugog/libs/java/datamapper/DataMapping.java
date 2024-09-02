package dev.hugog.libs.java.datamapper;

import dev.hugog.libs.java.datamapper.annotations.validation.Validated;
import dev.hugog.libs.java.datamapper.dbdata.DatabaseData;
import dev.hugog.libs.java.datamapper.discovery.DataMapperDiscoveryService;
import dev.hugog.libs.java.datamapper.dtos.Dto;
import dev.hugog.libs.java.datamapper.mappers.AbstractDataMapper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataMapping {

    private final List<AbstractDataMapper<?, ?>> mappers = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public <T extends DataObject> T map(DataObject classToMap, Class<T> classToMapTo) {

        if (classToMap == null) return null;

        if (classToMap instanceof DatabaseData) { // Then I want to map to a DTO

            Optional<? extends AbstractDataMapper<?, ?>> mapper = mappers.stream()
                    .filter(m -> m.getTransferObjectClass().equals(classToMapTo))
                    .findFirst();

            if (mapper.isPresent()) {
                return (T) mapper.get().toTransferObject((DatabaseData) classToMap);
            }

        } else if (classToMap instanceof Dto) { // Then I want to map to a raw data model

            Optional<? extends AbstractDataMapper<?, ?>> mapper = mappers.stream()
                    .filter(m -> m.getDatabaseDataClass().equals(classToMapTo))
                    .findFirst();

            // Validate DTO if it has the @Validated annotation
            if (classToMap.getClass().isAnnotationPresent(Validated.class)) {
                ((Dto) classToMap).validate();
            }

            if (mapper.isPresent()) {
                return (T) mapper.get().toData((Dto) classToMap);
            }

        } else {
            throw new IllegalArgumentException("classToMap is not of type ServiceRawData or Dto");
        }

        throw new IllegalArgumentException("No mapper found for class " + classToMap.getClass().getName());

    }

    /**
     * Maps a list of DataObjects to a list of DataObjects of another type
     *
     * @param classesToMap List of DataObjects to map
     * @param classToMapTo Class to map to
     * @return List of mapped DataObjects
     * @param <T> Type of DataObject to map to
     */
    public <T extends DataObject> List<T> mapAll(List<? extends DataObject> classesToMap, Class<T> classToMapTo) {

        if (classesToMap == null) return null;

        return classesToMap.stream()
                .map(classToMap -> map(classToMap, classToMapTo))
                .toList();

    }

    public void registerMapper(Class<? extends AbstractDataMapper<?, ?>> mapper) {

        try {
            mappers.add(mapper.getDeclaredConstructor(DataMapping.class).newInstance(this));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public void registerMappersWithDiscovery(String applicationBasePackage) {
        mappers.addAll(new DataMapperDiscoveryService(this, applicationBasePackage).autoRegisterDataMappers());
    }

}