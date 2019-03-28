package nl.fontys.sebivenlo.dao;

/**
 * Wraps any exception occurring in the DAO. Has all constructors of its super.
 *
 * @author Pieter van den Hombergh  {@code pieter.van.den.hombergh@gmail.com}
 */
public class DAOException extends RuntimeException {

    /**
     * Has a default ctor.
     */
    public DAOException() {
    }

    /**
     * Has a message.
     *
     * @param message for this exception
     */
    public DAOException( String message ) {
        super( message );
    }

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
     * Just a cause.
     *
     * @param cause typically an SQL exception, but in mocks it can be thrown
     * for any reason.
     */
    public DAOException( Throwable cause ) {
        super( cause );
    }

    /**
     * All that throwable provides.
     *
     * @param message to convey
     * @param cause the culprit
     * @param enableSuppression to allow other exceptions to take precedence
     * @param writableStackTrace to show this exception info
     */
    public DAOException( String message, Throwable cause, 
            boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
