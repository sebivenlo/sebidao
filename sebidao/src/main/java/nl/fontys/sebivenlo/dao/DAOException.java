package nl.fontys.sebivenlo.dao;

/**
 * Wraps any exception occurring in the DAO. Has all constructors of its super.
 * This implementation currently implements only constructor.
 *
 * @author Pieter van den Hombergh  {@code pieter.van.den.hombergh@gmail.com}
 */
public class DAOException extends RuntimeException {


    /**
     * Message and underlying cause.
     *
     * @param message to transfer
     * @param cause leading to this exception
     */
    public DAOException( String message, Throwable cause ) {
        super( message, cause );
    }

    /**
     * When there is no underlying cause.
     * @param message 
     */
    public DAOException( String message ) {
        super( message );
    }
    
}
