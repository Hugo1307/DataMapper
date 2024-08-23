package dev.hugog.libs.java.datamapper.mappers;

import dev.hugog.libs.java.datamapper.dbdata.DatabaseData;
import dev.hugog.libs.java.datamapper.dtos.Dto;

public interface Mapper<T extends DatabaseData, C extends Dto> {

    T toDataObject(C transferObject);

    C toTransferObject(T databaseDataObject);

}
