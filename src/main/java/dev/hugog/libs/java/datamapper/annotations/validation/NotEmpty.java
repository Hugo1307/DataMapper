package dev.hugog.libs.java.datamapper.annotations.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {
    String message() default "Value should not be empty.";
    boolean thrown() default true;
    boolean log() default true;
}
