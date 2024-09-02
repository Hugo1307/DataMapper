package dev.hugog.libs.java.datamapper.validation;

import dev.hugog.libs.java.datamapper.annotations.validation.NotEmpty;
import dev.hugog.libs.java.datamapper.annotations.validation.NotNull;
import dev.hugog.libs.java.datamapper.dtos.Dto;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataValidationService {

    private final Logger logger = Logger.getLogger(DataValidationService.class.getName());

    private final Dto transferObject;

    public DataValidationService(Dto transferObject) {
        this.transferObject = transferObject;
    }

    public boolean isValid() {

        boolean passedNotNullValidation = validateNotNullFields();
        boolean passedNotEmptyValidation = validateNotEmptyFields();

        return passedNotNullValidation && passedNotEmptyValidation;

    }

    private Set<Field> getNotNullFields() {
         return Arrays.stream(transferObject.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(NotNull.class))
                .collect(Collectors.toSet());
    }

    private Set<Field> getNotEmptyFields() {
         return Arrays.stream(transferObject.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(NotEmpty.class))
                .collect(Collectors.toSet());
    }

    private boolean validateNotNullFields() {

        Set<Field> notNullFields = getNotNullFields();

        for (Field field : notNullFields) {

            NotNull notEmptyAnnotation = field.getAnnotation(NotNull.class);

            try {

                field.setAccessible(true);

                if (field.get(transferObject) == null) {
                    if (notEmptyAnnotation.thrown()) {
                        throw new InvalidAttributeException(field.getName(), notEmptyAnnotation.message());
                    }
                    if (notEmptyAnnotation.log()) {
                        logger.info(new InvalidAttributeException(field.getName(), notEmptyAnnotation.message()).getMessage());
                    }
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return true;

    }

    private boolean validateNotEmptyFields() {

        Set<Field> notEmptyFields = getNotEmptyFields();

        for (Field field : notEmptyFields) {

            System.out.println(field.getName());

            NotEmpty notEmptyAnnotation = field.getAnnotation(NotEmpty.class);

            if (field.getType() != String.class) {
                if (notEmptyAnnotation.thrown()) {
                    throw new InvalidAttributeException(field.getName(), "@NotEmpty can only be used on String fields.");
                }
                if (notEmptyAnnotation.log()) {
                    logger.info("@NotEmpty used on invalid field type " + field.getName());
                }
            }

            try {
                field.setAccessible(true);
                if (field.get(transferObject) == null) {
                    return false;
                }
                if (field.get(transferObject).toString().isEmpty()) {
                    if (notEmptyAnnotation.thrown()) {
                        throw new InvalidAttributeException(field.getName(), notEmptyAnnotation.message());
                    }
                    if (notEmptyAnnotation.log()) {
                        logger.info(new InvalidAttributeException(field.getName(), notEmptyAnnotation.message()).getMessage());
                    }
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

        return true;

    }

}
