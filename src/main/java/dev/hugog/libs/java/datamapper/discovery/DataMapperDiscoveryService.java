package dev.hugog.libs.java.datamapper.discovery;

import dev.hugog.libs.java.datamapper.DataMapper;
import dev.hugog.libs.java.datamapper.annotations.Mapper;
import dev.hugog.libs.java.datamapper.mappers.AbstractMapper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class DataMapperDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(DataMapperDiscoveryService.class);
    private final DataMapper dataMapperInstance;
    private final Reflections reflectionUtils;

    public DataMapperDiscoveryService(DataMapper dataMapperInstance, String applicationBasePackage) {
        this.dataMapperInstance = dataMapperInstance;
        this.reflectionUtils = new Reflections(applicationBasePackage);
    }

    public List<AbstractMapper<?,?>> autoRegisterDataMappers() {
        List<AbstractMapper<?, ?>> mappers = reflectionUtils.getTypesAnnotatedWith(Mapper.class)
                .stream()
                .map(this::getDataMapperFromClass)
                .collect(Collectors.toList());

        log.info("Auto-registered {} data mappers.", mappers.size());
        return mappers;
    }

    public AbstractMapper<?, ?> getDataMapperFromClass(Class<?> myClass) {
        try {
            return (AbstractMapper<?, ?>) myClass.getDeclaredConstructor(DataMapper.class)
                    .newInstance(dataMapperInstance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
