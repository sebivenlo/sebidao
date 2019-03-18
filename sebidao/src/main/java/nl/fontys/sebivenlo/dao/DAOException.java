package nl.fontys.sebivenlo.dao;

/**
 * Wraps any exception occurring in the DAO.
 * Has all constructors of its super.
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class DAOException extends RuntimeException {

    public DAOException() {
    }

    public DAOException( String message ) {
        super( message );
    }

    public DAOException( String message, Throwable cause ) {
        super( message, cause );
    }

    public DAOException( Throwable cause ) {
        super( cause );
    }

    public DAOException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
    
    
}
