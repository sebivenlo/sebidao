package nl.fontys.sebivenlo.dao;

/**
 * Token to enable transaction using a DAO.
 *
 * The typical use case is to store a database connection, but this is not
 * specified.
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public interface TransactionToken {

    default void commit() throws Exception {
    }

    ;
    default void rollback() throws Exception {
    }
;
}
