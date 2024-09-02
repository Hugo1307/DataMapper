package dev.hugog.libs.java.datamapper.dtos;

import dev.hugog.libs.java.datamapper.DataObject;
import dev.hugog.libs.java.datamapper.validation.DataValidationService;

public interface Dto extends DataObject {

    default boolean validate() {
        DataValidationService dataValidationService = new DataValidationService(this);
        return dataValidationService.isValid();
    }

}
