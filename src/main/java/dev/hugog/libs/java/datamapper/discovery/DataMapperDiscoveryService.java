package dev.hugog.libs.java.datamapper.discovery;

import dev.hugog.libs.java.datamapper.DataMapping;
import dev.hugog.libs.java.datamapper.mappers.AbstractDataMapper;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class DataMapperDiscoveryService {

    private final DataMapping dataMappingInstance;
    private final Reflections reflectionUtils;

    public DataMapperDiscoveryService(DataMapping dataMappingInstance, String applicationBasePackage) {
        this.dataMappingInstance = dataMappingInstance;
        this.reflectionUtils = new Reflections(applicationBasePackage);
    }

    public List<AbstractDataMapper<?,?>> autoRegisterDataMappers() {
        return reflectionUtils.getSubTypesOf(AbstractDataMapper.class)
                .stream()
                .map(this::getDataMapperFromClass)
                .collect(Collectors.toList());
    }

    public AbstractDataMapper<?, ?> getDataMapperFromClass(Class<?> myClass) {

        try {
            return (AbstractDataMapper<?, ?>) myClass.getDeclaredConstructor(DataMapping.class).newInstance(dataMappingInstance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

}
