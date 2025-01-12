package dev.hugog.libs.java.datamapper.registration;

import dev.hugog.libs.java.datamapper.DataMapper;
import dev.hugog.libs.java.datamapper.mappers.AbstractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapperRegistry {

    private final Logger logger = LoggerFactory.getLogger(MapperRegistry.class);
    private final DataMapper dataMapper;
    private final List<AbstractMapper<?, ?>> mappers;

    public MapperRegistry(DataMapper dataMapper) {
        this.dataMapper = dataMapper;
        this.mappers = new ArrayList<>();
    }

    /**
     * Register a mapper manually.
     *
     * @param mapper the mapper to register
     */
    public void registerMapper(AbstractMapper<?, ?> mapper) {
        mappers.add(mapper);
        logger.info("Manually registered {} data mapper.", mapper.getClass().getSimpleName());
    }

    /**
     * Register all mappers in a package.
     *
     * <p>
     * This method will skip mappers that were already registered manually.
     *
     * @param applicationBasePackage the base package to scan for mappers
     */
    public void autoRegisterMappers(String applicationBasePackage) {
        DataMapperDiscoveryService dataMapperDiscoveryService = new DataMapperDiscoveryService(dataMapper, applicationBasePackage);
        Set<Class<?>> discoveredMappers = dataMapperDiscoveryService.findDataMappers();

        // Remove already registered mappers (i.e. mappers that were added manually)
        discoveredMappers.removeIf(m -> mappers.stream().anyMatch(a -> a.getClass().equals(m)));
        mappers.addAll(dataMapperDiscoveryService.obtainInstancedMappers(discoveredMappers));
        logger.info("Auto-registered {} data mappers.", discoveredMappers.size());
    }

    /**
     * Get all registered mappers.
     *
     * @return a list of all registered mappers
     */
    public List<AbstractMapper<?, ?>> getMappers() {
        return mappers;
    }

}
