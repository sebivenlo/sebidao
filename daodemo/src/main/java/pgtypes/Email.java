package pgtypes;

import java.sql.SQLException;
import java.util.Objects;
import org.postgresql.util.PGobject;

/**
 * Simple string to email proxy for the email type declared in the demo
 * database.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class Email extends PGobject {

    public Email() {
        setType( "email" );
    }

    public Email( String aValue ) throws SQLException {
        setType( "email" );
        setValue( aValue );
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( !( obj instanceof Email ) ) {
            return false;
        }
        return Objects.equals( this, obj );
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue( String value ) throws SQLException {
        setType( "email" );
        super.setValue( value ); //To change body of generated methods, choose Tools | Templates.
    }
}
