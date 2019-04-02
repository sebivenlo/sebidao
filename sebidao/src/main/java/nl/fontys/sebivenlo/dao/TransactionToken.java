package nl.fontys.sebivenlo.dao;

/**
 * Token to enable transaction using a DAO.
 *
 * The typical use case is to store a database connection, but this is not
 * specified.
 *
 * @author Pieter van den Hombergh  {@code pieter.van.den.hombergh@gmail.com}
 */
public interface TransactionToken extends AutoCloseable {

    /**
     * Save the effect of a transaction.
     *
     * @throws Exception on error.
     */
    default void commit() throws Exception {
    }

    /**
     * Undo the work of the transaction, making the transaction atomic.
     *
     * @throws Exception on error.
     */
    default void rollback() throws Exception {
    }

    @Override
    default void close() throws Exception{};

}
