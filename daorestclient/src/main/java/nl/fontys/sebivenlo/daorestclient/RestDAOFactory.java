package nl.fontys.sebivenlo.daorestclient;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.AbstractDAOFactory;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.TransactionToken;

/**
 * Using a factory allows the addition of features to the DAO or used connection
 * without interfering with the normal API usage.
 *
 * The intended extension would be to add basic authentication headers.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class RestDAOFactory extends AbstractDAOFactory {

    private final String baseUrl;

    public RestDAOFactory( String baseUrl ) {
        this.baseUrl = baseUrl;
    }

    @Override
    public <K extends Serializable, E extends Entity2<K>> DAO<K, E> createDao(
            Class<E> forClass ) {
        return new RestDAO<>( baseUrl,
                forClass, this.connectionConfigurator() );
    }

    /**
     * Required by the DAO super class, but since rest does not have a true
     * transactional model, this implementation ignores the token
     *
     * @param <K> key
     * @param <E> entity
     * @param forClass type
     * @param token ignored
     * @return
     */
    @Override
    public <K extends Serializable, E extends Entity2<K>> DAO<K, E> createDao(
            Class<E> forClass, TransactionToken token ) {
        return createDao( forClass );
    }

    /**
     * Get a configurator for the basic headers.
     *
     * This allows to add authentication in a clean way.
     * The configurator should be used to open the connection and add headers to it.
     *
     * Simply overwrite this method in a sub class, keeping the rest the same
     * will do the trick.
     *
     * @return the header configurator.
     */
    protected BiFunction<URL, String, HttpURLConnection> connectionConfigurator() {
        return ( URL u, String verb ) -> {
            try {
                HttpURLConnection con = (HttpURLConnection) u.openConnection();
                con.setRequestMethod( verb );
                con.setRequestProperty( "Content-Type", "application/json" );
                con.setRequestProperty( "charset", "utf-8" );
                con.setRequestProperty( "Accept", "application/json" );
                con.setUseCaches( false );
                con.setDoOutput( true );
                return con;
            } catch ( IOException ex ) {
                Logger.getLogger( RestDAOFactory.class.getName() ).
                        log( Level.SEVERE, null, ex );
                throw new DAOException( "cannot open connection for url " + u.
                        toExternalForm(), ex );
            }
        };

    }
}
