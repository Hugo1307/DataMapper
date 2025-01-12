package dev.hugog.libs.java.datamapper.mappers;

import dev.hugog.libs.java.datamapper.DataMapper;
import dev.hugog.libs.java.datamapper.dbdata.DatabaseData;
import dev.hugog.libs.java.datamapper.dtos.Dto;

public abstract class AbstractMapper<T extends DatabaseData, C extends Dto> implements Mapper<T, C> {

    private final Class<T> databaseDataClass;
    private final Class<C> transferObjectClass;
    private final DataMapper dataMapperInstance;

    protected AbstractMapper(Class<T> databaseDataClass, Class<C> transferObjectClass, DataMapper dataMapperInstance) {
        this.databaseDataClass = databaseDataClass;
        this.transferObjectClass = transferObjectClass;
        this.dataMapperInstance = dataMapperInstance;
    }

    public Class<T> getDatabaseDataClass() {
        return databaseDataClass;
    }

    public Class<C> getTransferObjectClass() {
        return transferObjectClass;
    }

    public DataMapper getMapper() {
        return dataMapperInstance;
    }

}
