package nl.fontys.sebivenlo.dao.pg;

import java.io.Serializable;
import nl.fontys.sebivenlo.dao.Mapper;
import static java.lang.String.format;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.TransactionToken;
import nl.fontys.sebivenlo.dao.Entity2;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <K> key to retrieve entity.
 * @param <E> type of elements for the this data access object.
 */
public class PGDAO<K extends Serializable, E extends Entity2<K>>
        implements
        DAO<K, E> {

    /**
     * Table backing this DAO.
     */
    protected final String tableName;
    /**
     * Id column name.
     */
    protected final String idName;
    /**
     * When a transaction is taking place .
     */
    protected PGTransactionToken transactionToken;
    @Resource
    final DataSource ds;
    @Resource
    final Mapper<K, E> mapper;

    /**
     * Create a DAO for a data source and a mapper.
     *
     * @param ds injected through this ctor.
     * @param mapper injected through this ctor.
     */
    public PGDAO( DataSource ds, Mapper<K, E> mapper ) {
        this.ds = ds;
        this.mapper = mapper;
        this.tableName = mapper.tableName();
        this.idName = mapper.idName();
    }

    @Override
    public Optional<E> get( K id ) {
        String sql = format( "select * from %s where %s=?", tableName, idName );
        try ( Connection c = this.getConnection();
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            pst.setObject( 1, id );
            try (
                    ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    E result = recordToEntity( rs );
                    return Optional.ofNullable( result );
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
        return Optional.empty();
    }

    private E recordToEntity( final ResultSet rs ) throws SQLException {
        E result = mapper.implode( fillPartsArray( rs ) );
        return result;
    }

    private Object[] fillPartsArray( final ResultSet rs ) throws
            SQLException {
        return fillPartsArray( createPartsArray( rs ), rs );
    }

    private Object[] fillPartsArray( Object[] parts, final ResultSet rs ) throws
            SQLException {
        for ( int i = 0; i < parts.length; i++ ) {
            parts[ i ] = rs.getObject( i + 1 );
        }
        return parts;
    }

    private Object[] createPartsArray( final ResultSet rs ) throws SQLException {
        //int columnCount = rs.getMetaData().getColumnCount();
        Object[] parts = new Object[ rs.getMetaData().getColumnCount() ];
        return parts;
    }

    @Override
    public void delete( E t ) {
        String sql = format( "delete from %s where %s=?", tableName, idName );
        try ( Connection c = ds.getConnection();
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            pst.setObject( 1, mapper.keyExtractor().apply( t ) );
            boolean execute = pst.execute();
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );

        }

    }

    @Override
    public E update( E t ) {
        // get key field names,
        // get persistent field names, key fields removed
        // get natural id, chop up into parts for where (keys...) = (parts...)
        // get object parts
        // sql = "
        E result = null;
        Set<String> keyNames = mapper.keyNames();
        List<String> columnNames
                = mapper.persistentFieldNames().stream().collect( toList() );
        String columns = String.join( ",", columnNames );
        String placeholders = makePlaceHolders( columnNames );
        K key = mapper.keyExtractor().apply( t );
        //if (key.getClass().i)
        String sql = String.format( "update %s set (%s)=(%s) where (%s)=(?)"
                + " returning * ",
                tableName, columns,
                placeholders,
                keyNames.iterator().next(),
                key );

        try ( Connection c = this.getConnection(); PreparedStatement pst = c.
                prepareStatement( sql ); ) {
            Object[] parts = mapper.explode( t );
            int j = 1;

            // all fields
            for ( int i = 0; i < parts.length; i++ ) {
                pst.setObject( j++, parts[ i ] );
            }
            pst.setObject( j, key );
            try ( ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    result = recordToEntity( rs );
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
        return result;

    }

    @Override
    public E save( E t ) {
        E result = null;
        final List<String> columnNames
                = mapper.persistentFieldNames()
                        .stream()
                        .filter( s -> !( mapper.keyNames().contains( s )
                        && mapper.generateKey() ) )
                        .collect( toList() );

        String columns = String.join( ",", columnNames );
        String placeholders = makePlaceHolders( columnNames );
        //String placeholders = Arrays.f
        String sql
                = format( "insert into %s (%s) \n"
                        + "values(%s) %n"
                        + "returning *", tableName,
                        columns, placeholders );
        try ( Connection c = this.getConnection();
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            Object[] parts = mapper.explode( t );
            int j = 1;
            for ( int i = mapper.generateKey() ? 1 : 0; i < parts.length; i++ ) {
                pst.setObject( j++, parts[ i ] );
            }

            try ( ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    result = recordToEntity( rs );
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
        return result;
    }

    private String makePlaceHolders( final List<String> columnNames ) {
        String[] qm = new String[ columnNames.size() ];
        Arrays.fill( qm, "?" );
        String placeholders = String.join( ",", qm );
        return placeholders;
    }

    @Override
    public Collection<E> getAll() {
        String sql = format( "select * from %s ", tableName );
        Collection<E> result = new ArrayList<>();
        try ( Connection c = this.getConnection();
                PreparedStatement pst = c.prepareStatement( sql );
                ResultSet rs = pst.executeQuery(); ) {
            Object[] parts = createPartsArray( rs );
            while ( rs.next() ) {
                // note columns start at 1
                fillPartsArray( parts, rs );
                E e = mapper.implode( parts );
                result.add( e );
            }

        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
        return result;
    }

    @Override
    public int lastId() {
        String sql = format( "select max(%s)  as lastid from %s", idName,
                tableName );
        int result = executeIntQuery( sql );

        return result;
    }

    final int executeIntQuery( String sql ) {
        int result = 0;
        try ( Connection c = this.getConnection();
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            try ( ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    result = rs.getInt( 1 );
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
        return result;
    }

    final int executeIntQuery( String sql, K k ) {
        int result = 0;
        try ( Connection c = this.getConnection();
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            pst.setObject( 1, k );
            try ( ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    result = rs.getInt( 1 );
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
        return result;
    }

    @Override
    public int size() {
        String sql = format( "select count(1)  as size from %s", tableName );
        int result = executeIntQuery( sql );

        return result;

    }

    private Connection getConnection() throws SQLException {
        if ( transactionToken == null ) {
            return ds.getConnection();
        } else {
            return transactionToken.getConnection();
        }
    }

    @Override
    public TransactionToken getTransactionToken() {
        return this.transactionToken;
    }

    @Override
    public PGDAO<K, E> setTransactionToken( TransactionToken tok ) {
        if ( tok instanceof PGTransactionToken ) {
            this.transactionToken = (PGTransactionToken) tok;
        }
        return this;
    }

    @Override
    public TransactionToken startTransaction() throws SQLException {
        if ( null == transactionToken ) {
            transactionToken = new PGTransactionToken( getConnection() );
        }
        return transactionToken;
    }

    /**
     * Simply get the integer translation for a natural key.
     * @param k lookup key
     * @return the int value matching this key.
     */
    public int getIdForKey( K k ) {
        String ks = k.toString();
        String sql = String.format( "select %s from %s where %s=?", mapper.idName(), mapper.tableName(), mapper.naturalKeyName() );
        return executeIntQuery( sql, k );

    }
}
