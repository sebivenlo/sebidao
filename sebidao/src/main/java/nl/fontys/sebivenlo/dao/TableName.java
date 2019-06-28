package nl.fontys.sebivenlo.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify the table name for a class.
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableName {
    String value() default "mytable";
}
