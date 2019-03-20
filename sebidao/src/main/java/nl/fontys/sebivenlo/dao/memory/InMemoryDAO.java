package nl.fontys.sebivenlo.dao.memory;

import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.Entity2;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An in memory implementation of a DAO, typically used for testing.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 * @param <K> key for lookup
 * @param <E> type of entity
 */
public class InMemoryDAO<K extends Serializable, E extends Entity2<K>> implements
        DAO<K, E>, Serializable {

    /**
     * Create a DAO using the given name for persistence to disk.
     *
     * @param storageName on disk file name
     */
    public InMemoryDAO( String storageName ) {
        this.storageName = storageName;
        if ( Files.exists( Paths.get( this.storageName ) ) ) {
            System.out.println( "loaded " + storageName );
            this.load( this.storageName );
        }
        Thread saveThread = new Thread( () -> persistToDisk() );
        Runtime.getRuntime().addShutdownHook( saveThread );
    }

    /**
     * Create a DAO, deriving the name for the on disk storage from the type
     * name.
     *
     * @param tclass managed by this DAO
     */
    public InMemoryDAO( Class<E> tclass ) {
        this( tclass.getCanonicalName() + ".ser" );
    }

    private final Map<K, E> storage = new HashMap<>();

    private final String storageName;

    @Override
    public Optional<E> get( K id ) {
        return Optional.ofNullable( storage.get( id ) );
    }

    @Override
    public Collection<E> getAll() {
        return storage.values();
    }

    @Override
    public E save( E t ) {

        storage.put( t.getNaturalId(), t );
        return t;
    }

    @Override
    public E update( E t ) {
        storage.replace( t.getNaturalId(), t );
        return t;
    }

    @Override
    public void delete( E t ) {
        storage.remove( t.getId() );
    }

    private void persistToDisk() {
        if ( storage.isEmpty() ) {
            return; // nothing to do
        }
        try (
                ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream( getStorageName() ) ); ) {
            out.writeObject( this.storage );
        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( InMemoryDAO.class.getName() ).
                    log( Level.SEVERE, null, ex );
        } catch ( IOException ex ) {
            Logger.getLogger( InMemoryDAO.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }
    }

    private void load( String aStorageName ) {
        try ( ObjectInputStream in = new ObjectInputStream(
                new FileInputStream( aStorageName ) ) ) {
            this.storage.clear();
            Map<K, E> readMap = (Map<K, E>) in.readObject();

            this.storage.putAll( readMap );

        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( InMemoryDAO.class.getName() ).
                    log( Level.SEVERE, null, ex );
        } catch ( IOException | ClassNotFoundException ex ) {
            Logger.getLogger( InMemoryDAO.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

    }

    /**
     * Get the storage name.
     *
     * @return the name
     */
    String getStorageName() {
        return this.storageName;
    }

    @Override
    public int size() {
        return storage.size();
    }

}
