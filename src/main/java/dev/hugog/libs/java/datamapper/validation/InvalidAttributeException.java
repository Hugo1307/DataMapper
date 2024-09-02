package dev.hugog.libs.java.datamapper.validation;

public class InvalidAttributeException extends RuntimeException {

    private final String attribute;
    private final String message;

    public InvalidAttributeException(String attribute, String message) {
        super(message);
        this.attribute = attribute;
        this.message = message;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getMessage() {
        return message;
    }

}
