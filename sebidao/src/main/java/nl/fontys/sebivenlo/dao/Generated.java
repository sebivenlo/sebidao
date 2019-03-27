package nl.fontys.sebivenlo.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For fields be generated by the backing store. If the field does not qualify
 * as the id field of the entity, use this annotation instead.
 *
 * Just adding the annotation will tag the id generated, i.e. typically using
 * some sequence.
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Generated {
}
