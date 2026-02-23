package dev.hugog.libs.java.datamapper.dbdata;

/**
 * Represents a data object that is embedded within another data object.
 *
 * <p>Data embedded objects are not stored as separate entities in the database, but rather as part of the
 * parent data object. They do not require a unique identifier or separate storage.
 */
public interface DataEmbedded extends DatabaseData {

}
