package dev.hugog.libs.java.datamapper.annotations.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
    String message() default "Value should not be null.";
    boolean thrown() default true;
    boolean log() default true;
}
