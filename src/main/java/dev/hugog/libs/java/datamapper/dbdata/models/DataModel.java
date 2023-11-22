package dev.hugog.libs.java.datamapper.dbdata.models;

import dev.hugog.libs.java.datamapper.dbdata.DatabaseData;

import java.util.UUID;

public interface DataModel extends DatabaseData {

    UUID getId();

    void setId(UUID id);

}
