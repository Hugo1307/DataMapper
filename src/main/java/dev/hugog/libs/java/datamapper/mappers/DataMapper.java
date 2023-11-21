package dev.hugog.libs.java.datamapper.mappers;

import dev.hugog.libs.java.datamapper.dbdata.DatabaseData;
import dev.hugog.libs.java.datamapper.dtos.Dto;

public interface DataMapper<T extends DatabaseData, C extends Dto> {

    T toData(Dto transferObject);

    C toTransferObject(DatabaseData databaseDataObject);

}
