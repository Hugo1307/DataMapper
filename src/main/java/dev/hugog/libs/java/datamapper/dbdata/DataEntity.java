package dev.hugog.libs.java.datamapper.dbdata;

import java.util.UUID;

/**
 * Represents a data entity that is stored in a database.
 *
 * <p>Data entities are the main objects that are persisted in the database. They have a unique identifier
 * and can be queried, updated, and deleted.
 */
public interface DataEntity extends DatabaseData {

    /**
     * Gets the unique identifier of the data entity.
     *
     * @return The unique identifier of the data entity.
     */
    UUID getId();

}
