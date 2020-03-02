package nl.fontys.sebivenlo.dao;

/**
 * Simple string to type marshaller. Many 'over the wire' types are represented
 * as strings. Example is are LocalDateTimeRanges which map to TSRange. As
 * functional interface it is String to T. Typical use case is having maps of type to in marshaller,
 * where the key is the database type. This design supports immutable types.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public interface InMarshaller<T> {

    /**
     * Parse the string into a value of T.
     * @param input
     * @return 
     */
    T read( String input );

}
