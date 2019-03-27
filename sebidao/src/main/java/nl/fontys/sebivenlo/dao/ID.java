package nl.fontys.sebivenlo.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies the ID field of an Entity.
 * 
 * Just adding the annotation will tag the id generated, i.e. typically using some sequence.
 * Use @ID(generated=false) if you have a natural key that should NOT be generated.
 * 
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ID {
    boolean generated() default true;
}
