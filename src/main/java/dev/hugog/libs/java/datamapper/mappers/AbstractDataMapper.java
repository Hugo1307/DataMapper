package dev.hugog.libs.java.datamapper.mappers;

import dev.hugog.libs.java.datamapper.DataMapping;
import dev.hugog.libs.java.datamapper.dbdata.DatabaseData;
import dev.hugog.libs.java.datamapper.dtos.Dto;

public abstract class AbstractDataMapper<T extends DatabaseData, C extends Dto>
        implements DataMapper<T, C> {

    private final Class<? extends DatabaseData> databaseDataClass;
    private final Class<? extends Dto> transferObjectClass;
    private final DataMapping dataMappingInstance;

    public AbstractDataMapper(Class<? extends DatabaseData> databaseDataClass,
                              Class<? extends Dto> transferObjectClass,
                              DataMapping dataMappingInstance) {

        this.databaseDataClass = databaseDataClass;
        this.transferObjectClass = transferObjectClass;
        this.dataMappingInstance = dataMappingInstance;
    }

    public Class<? extends DatabaseData> getDatabaseDataClass() {
        return databaseDataClass;
    }

    public Class<? extends Dto> getTransferObjectClass() {
        return transferObjectClass;
    }

    public DataMapping getMapper() {
        return dataMappingInstance;
    }

}
