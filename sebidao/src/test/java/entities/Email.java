package entities;

import java.io.Serializable;
import java.util.Objects;
import org.postgresql.util.PGobject;

/**
 * To test user types.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class Email implements Serializable {

    String value;

    /**
     * Should do regex validation but does not.
     *
     * @param value string.
     */
    public Email( Object value ) {
        this.value = value.toString();
    }

    @Override
    public String toString() {
        return value;
    }

    public static Email email( Object v ) {
        return new Email( v.toString() );
    }
}
