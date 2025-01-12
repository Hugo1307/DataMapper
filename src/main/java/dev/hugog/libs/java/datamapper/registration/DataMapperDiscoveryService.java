package dev.hugog.libs.java.datamapper.registration;

import dev.hugog.libs.java.datamapper.DataMapper;
import dev.hugog.libs.java.datamapper.annotations.Mapper;
import dev.hugog.libs.java.datamapper.exceptions.InvalidMapper;
import dev.hugog.libs.java.datamapper.mappers.AbstractMapper;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

public class DataMapperDiscoveryService {

    private final DataMapper dataMapperInstance;
    private final Reflections reflectionUtils;

    public DataMapperDiscoveryService(DataMapper dataMapperInstance, String applicationBasePackage) {
        this.dataMapperInstance = dataMapperInstance;
        this.reflectionUtils = new Reflections(applicationBasePackage);
    }

    public Set<Class<?>> findDataMappers() {
        return reflectionUtils.getTypesAnnotatedWith(Mapper.class);
    }

    public Set<AbstractMapper<?, ?>> obtainInstancedMappers(Set<Class<?>> dataMappers) {
        return dataMappers.stream()
                .map(this::getDataMapperFromClass)
                .collect(Collectors.toSet());
    }

    private AbstractMapper<?, ?> getDataMapperFromClass(Class<?> myClass) {
        try {
            return (AbstractMapper<?, ?>) myClass.getDeclaredConstructor(DataMapper.class)
                    .newInstance(dataMapperInstance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new InvalidMapper("Unable to find default mapper constructor!", e);
        }
    }

}
