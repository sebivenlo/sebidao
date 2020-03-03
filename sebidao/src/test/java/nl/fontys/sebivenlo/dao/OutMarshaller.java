package nl.fontys.sebivenlo.dao;

/**
 * Simple type to string converter, whose output can be forwarded to the
 * database.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 * @param <T> value type
 */
public interface OutMarshaller<T> {

    /**
     * Converts the value to the string format understood by the database.
     *
     * @param value to write
     * @return the jdbc string representation.
     */
    String write( T value );
}
