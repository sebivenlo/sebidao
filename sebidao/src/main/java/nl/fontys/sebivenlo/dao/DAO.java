package nl.fontys.sebivenlo.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.StreamSupport;

/**
 * Data Access Object with transactions.
 *
 * A DAO has a few simple operations to support the traditional persistent
 * storage work:
 * <dl>
 * <dt>Create</dt> <dd>called {@code save(Entity e)} here.</dd>
 * <dt>Read</dt> <dd>called {@code Optional<Entity> get(Key k} here.</dd>
 * <dt>Update</dt> <dd>called {@code Entity update(Entity e)} here.</dd>
 * <dt>Delete</dt> <dd>called {@code void delete(Entity e)} here.</dd>
 * <dt>Get all</dt><dd>called {@code Collection<Entity> getAll()} here.</dd>
 * </dl>
 *
 * A future version of this DAO can participate in transactions by passing
 * around a transaction token. The implementing class of this interface is
 * advised to have a constructor accepting the token.
 *
 * The implementations that need to forward checked exceptions should wrap these
 * exceptions in a appropriate unchecked variant, or just log them.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <K> Key to the entity
 * @param <E> the type of Entity2.
 */
public interface DAO<K extends Serializable, E extends Entity2<K>> extends AutoCloseable {

    /**
     * Get the entity by id, if any.
     *
     * @param id of the entity
     * @return a nullable optional containing the entity or not.
     */
    Optional<E> get( K id );

    /**
     * Get all entities.
     *
     * @return all entities available through this dao
     */
    Collection<E> getAll();

    /**
     * Get by column keyvalues pairs. Convenience method to get a entities by
     * field=value[,field=value].
     *
     * @param keyValues even sized parameter list like
     * ("departmentid",1,"firstname", "Piet").
     * @return the collection of enities matching key value pairs.
     */
    default Collection<E> getByColumnValues( Object... keyValues ) {
        throw new UnsupportedOperationException( "Not yet available" );
    }

    /**
     * Persist an entity.
     *
     * @param e to save
     * @return the update entity, primary key and all
     */
    E save( E e );

    /**
     * Update and entity. In a typical database scenario, all fields are set to
     * the new values. It is up to the user to ensure that the id value is
     * stable throughout this operation.
     *
     * @param e entity to update
     * @return the updated entity
     */
    E update( E e );

    /**
     * Delete t using its primary key.
     *
     * @param e entity to delete
     */
    void delete( E e );

    /**
     * Start a transaction and create a token carrying the transaction relevant
     * information.
     *
     * @return the token
     * @throws java.lang.Exception on error, eg database connection failure
     */
    default TransactionToken startTransaction() throws Exception {
        return null;
    }

    /**
     * Pass an existing transaction token to this DAO. In the default
     * implementation, the token is silently ignored.
     *
     * @param tok to use for the remainder.
     * @return this DAO
     */
    default DAO<K, E> setTransactionToken( TransactionToken tok ) {
        return this;
    }

    /**
     * Get the transaction token of this DAO, if any.
     *
     * @return the token or null if no transaction has been started.
     */
    default TransactionToken getTransactionToken() {
        return null;
    }

    /**
     * Get the number of entities accessible through this DAO.
     *
     * @return the size of the backing store (table, lst, map...)
     */
    default int size() {
        return 0;
    }

    /**
     * Get the highest id used in this DAO.
     *
     * @return the last used number.
     */
    default int lastId() {
        return 0;
    }

    /**
     * Default no-op close.
     *
     * @throws Exception whenever the implementer sees it fit.
     */
    @Override
    default void close() throws Exception {
    }

    /**
     * Save all entities, returning the result in a collection with the saved
     * versions of the entities, generated fields and all.
     *
     * The caller is advised to give this DAO a transaction token, so this
     * operation can perform atomically by rolling back any progress for those
     * persistence implementations that support commit and rollback.
     *
     *
     * @param entities to save
     * @return the saved enities
     * @since 0.4
     */
    default Collection<E> saveAll( Iterable<E> entities ) {
        return StreamSupport.stream( entities.spliterator(), false )
                .map( e -> this.save( e ) ).collect( toList() );
    }

    /**
     * Save the given entities, returning the result as a list.
     *
     * @param entities to save
     * @return the saved versions of the entities in a list.
     */
    default Collection<E> saveAll( E... entities ) {

        return saveAll( Arrays.asList( entities ) );
    }
}
