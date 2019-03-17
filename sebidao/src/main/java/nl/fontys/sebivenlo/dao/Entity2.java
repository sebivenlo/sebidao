package nl.fontys.sebivenlo.dao;

import java.io.Serializable;
import java.util.function.ToIntFunction;

/**
 * An entity that is access via a key.
 *
 * To be able to retrieve an entity from a backing persistence layer, it needs
 * to have a key. And yes, if you associate that with concept of a primary key,
 * you would be right.
 *
 * <p>
 * <b>Use case:</b> Your customer is positively critical but also a bit
 * pedantic. He read a book on SQL, in particular heard things about natural and
 * composite keys. It is the only book he read (allegedly) and considers this
 * the philosophers stone. He considers surrogate keys bad, and wishes to avoid
 * them.</p>
 *
 * <p>
 * However, as any true software engineer knows, nothing in the real world
 * matches the real requirements of a natural key: immutable and not null. To
 * give you a few examples:</p>
 * <ul>
 * <li>Address as natural key? What if the customer moves, who do you send the
 * invoice to?</li>
 * <li>Birth date and gender. We live in 2019+, so who never heard of Gender
 * change</li>
 * </ul>
 *
 * <p>
 * Then the smarty-pants comes to you and says, yeah, Okay, but what about
 * <b>many-to-many</b>
 * relations, as in many customers buy many different products, and you want
 * that in a many-to-many resolving table. Would that not make the use case for
 * at least a composite key? Okay, you say, I will accepts both suggestions as
 * requirement, as long as you pay for the added complexity.</p>
 *
 * <p>
 * So there you have it, an entity that is mapped by any type, even a customer
 * type, as long as it is serializable. Make sure that it is unique, by setting
 * a unique index and not null on all fields in the key. We will not tell him
 * that we use a private surrogate anyway, to keep our life simple.</p>
 *
 * <p>
 * Hint to the implementor of a database version: Define the 'natural key' as
 * both <b>NOT NULL</b> and <b>UNIQUE</b>, which in PostgreSQL is automatically
 * indexed from then on and gives you all the benefits of the natural key
 * without the hassle. And hey, the customer pays, so accept that he wants to
 * wast his memory and disk space by using potentially overly big foreign keys
 * in tables that would be find with numbers.</p>
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 * @param <K> key to the entity.
 */
public interface Entity2<K extends Serializable> extends SimpleEntity {

    /**
     * Get the key of this entity.
     *
     * @return the key.
     */
    K getNaturalId();

    /**
     * The backing store is easiest to implement using a simple number.
     *
     * @return the id in int form
     */
    @Override
    default int getId() {
        return idMapper().applyAsInt( getNaturalId() );
    }

    /**
     * Get the function to transform the natural and potential composite key to
     * a surrogate key.
     *
     * @return the function
     */
    ToIntFunction<K> idMapper();

}
