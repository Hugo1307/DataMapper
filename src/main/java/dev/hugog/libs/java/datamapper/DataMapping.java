package dev.hugog.libs.java.datamapper;

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

            if (mapper.isPresent()) {
                return (T) mapper.get().toData((Dto) classToMap);
            }

        } else {
            throw new IllegalArgumentException("classToMap is not of type ServiceRawData or Dto");
        }

        throw new IllegalArgumentException("No mapper found for class " + classToMap.getClass().getName());

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