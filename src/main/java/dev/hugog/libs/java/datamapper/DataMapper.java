package dev.hugog.libs.java.datamapper;

import dev.hugog.libs.java.datamapper.dbdata.DatabaseData;
import dev.hugog.libs.java.datamapper.discovery.DataMapperDiscoveryService;
import dev.hugog.libs.java.datamapper.dtos.Dto;
import dev.hugog.libs.java.datamapper.mappers.AbstractMapper;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DataMapper {

    private final List<AbstractMapper<?, ?>> mappers = new ArrayList<>();

    /**
     * Maps a DTO to DatabaseData.
     *
     * @param classToMap DTO to map
     * @return mapped Model
     * @param <T> Type of DataModel to map to
     * @param <C> Type of Dto to map from
     */
    @SuppressWarnings("unchecked")
    public <T extends DatabaseData, C extends Dto> T map(C classToMap) {
        if (classToMap == null) {
            return null;
        }

        Optional<? extends AbstractMapper<T, C>> mapper = mappers.stream()
                .filter(m -> m.getTransferObjectClass().equals(classToMap.getClass()))
                .map(m -> (AbstractMapper<T, C>) m)
                .findFirst();

        return mapper.map(m -> m.toDataObject(classToMap))
                .orElseThrow(() -> new IllegalArgumentException("No mapper found for class " + classToMap.getClass().getName()));
    }

    /**
     * Maps DatabaseData to a DTO.
     *
     * @param classToMap DataModel to map
     * @return mapped DTO
     * @param <T> Type of Dto to map to
     * @param <C> Type of DataModel to map from
     */
    @SuppressWarnings("unchecked")
    public <T extends Dto, C extends DatabaseData> T map(C classToMap) {
        if (classToMap == null) {
            return null;
        }

        Optional<? extends AbstractMapper<C, T>> mapper = mappers.stream()
                .filter(m -> m.getDatabaseDataClass().equals(classToMap.getClass()))
                .map(m -> (AbstractMapper<C, T>) m)
                .findFirst();

        return mapper.map(m -> m.toTransferObject(classToMap))
                .orElseThrow(() -> new IllegalArgumentException("No mapper found for class " + classToMap.getClass().getName()));
    }

    /**
     * Maps a list of DTOs to DatabaseData.
     *
     * @param classesToMap List of DTOs to map
     * @param target parameter to avoid type erasure (should be null)
     * @return List of mapped Models
     * @param <T> Type of DatabaseData to map to
     * @param <C> Type of Dto to map from
     */
    @SuppressWarnings("unchecked")
    public <T extends DatabaseData, C extends Dto> List<T> mapAll(List<C> classesToMap, @Nullable C target) {
        if (classesToMap == null) return null;

        return classesToMap.stream()
                .map(classToMap -> (T) map(classToMap))
                .toList();
    }

    /**
     * Maps a list of DatabaseData to DTOs.
     *
     * @param classesToMap List of DatabaseData to map
     * @param target parameter to avoid type erasure (should be null)
     * @return List of mapped DTOs
     * @param <T> Type of Dto to map to
     * @param <C> Type of DatabaseData to map from
     */
    @SuppressWarnings("unchecked")
    public <T extends Dto, C extends DatabaseData> List<T> mapAll(List<C> classesToMap, @Nullable C target) {
        if (classesToMap == null) return null;

        return classesToMap.stream()
                .map(classToMap -> (T) map(classToMap))
                .toList();
    }

    /**
     * Maps a set of DTOs to DatabaseData.
     *
     * @param classesToMap Set of DTOs to map
     * @param target parameter to avoid type erasure (should be null)
     * @return Set of mapped Models
     * @param <T> Type of DatabaseData to map to
     * @param <C> Type of Dto to map from
     */
    @SuppressWarnings("unchecked")
    public <T extends DatabaseData, C extends Dto> Set<T> mapAll(Set<C> classesToMap, @Nullable C target) {
        if (classesToMap == null) return null;

        return classesToMap.stream()
                .map(classToMap -> (T) map(classToMap))
                .collect(Collectors.toSet());
    }

    /**
     * Maps a set of DatabaseData to DTOs.
     *
     * @param classesToMap Set of DatabaseData to map
     * @param target parameter to avoid type erasure (should be null)
     * @return Set of mapped DTOs
     * @param <T> Type of Dto to map to
     * @param <C> Type of DatabaseData to map from
     */
    @SuppressWarnings("unchecked")
    public <T extends Dto, C extends DatabaseData> Set<T> mapAll(Set<C> classesToMap, @Nullable C target) {
        if (classesToMap == null) return null;

        return classesToMap.stream()
                .map(classToMap -> (T) map(classToMap))
                .collect(Collectors.toSet());
    }

    public void registerMapper(Class<? extends AbstractMapper<?, ?>> mapper) {
        try {
            mappers.add(mapper.getDeclaredConstructor(DataMapper.class).newInstance(this));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerMappersWithDiscovery(String applicationBasePackage) {
        mappers.addAll(new DataMapperDiscoveryService(this, applicationBasePackage).autoRegisterDataMappers());
    }

}