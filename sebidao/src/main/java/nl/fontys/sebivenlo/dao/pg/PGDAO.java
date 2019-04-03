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

    /**
     * Transaction capable version.
     *
     * @param id of E to get
     * @return optionally the E
     */
    @Override
    public Optional<E> get( K id ) {
        if ( null != transactionToken ) {
            Connection con = transactionToken.getConnection();
            return get( con, id );
        }
        try ( Connection con = this.getConnection(); ) {
            return get( con, id );
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    /**
     * Use a connection provided.
     *
     * @param con connection
     * @param id value
     * @return Optional of E
     */
    private Optional<E> get( final Connection con, K id ) {
        String sql = format( "select * from %s where %s=?", tableName,
                idName );
        try (
                PreparedStatement pst = con.prepareStatement( sql ); ) {
            pst.setObject( 1, id );
            try (
                    ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    return Optional.ofNullable( recordToEntity( rs ) );
                } else {
                    return Optional.empty();
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
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
        return new Object[ rs.getMetaData().getColumnCount() ];
    }

    @Override
    public void delete( E t ) {
        if ( null != transactionToken ) {
            delete( transactionToken.getConnection(), t );
            return;
        }
        try ( Connection con = ds.getConnection(); ) {
            delete( con, t );
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private void delete( final Connection con, E t ) {
        String sql = format( "delete from %s where %s=?", tableName, idName );
        try (
                PreparedStatement pst = con.prepareStatement( sql ); ) {
            pst.setObject( 1, mapper.keyExtractor().apply( t ) );
            boolean ignored = pst.execute();
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    @Override
    public E update( E t ) {
        if ( null != transactionToken ) {
            return update( transactionToken.getConnection(), t );
        }
        try ( Connection con = this.getConnection(); ) {
            return update( con, t );
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private E update( final Connection c, E t ) {
        Set<String> keyNames = mapper.keyNames();
        List<String> columnNames
                = mapper.persistentFieldNames().stream().collect( toList() );
        String columns = String.join( ",", columnNames );
        String placeholders = makePlaceHolders( columnNames );
        K key = mapper.keyExtractor().apply( t );
        String sql = String.format( "update %s set (%s)=(%s) where (%s)=(?)"
                + " returning * ",
                tableName, columns,
                placeholders,
                keyNames.iterator().next(),
                key );

        try ( PreparedStatement pst = c.
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
                    return recordToEntity( rs );
                } else {
                    return null;
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    @Override
    public E save( E t ) {
        if ( null != transactionToken ) {
            return save( transactionToken.getConnection(), t );
        }

        try ( Connection c = this.getConnection(); ) {
            return save( c, t );
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private E save( final Connection c, E t ) {
        final List<String> columnNames
                = getColumnNames();
        String columns = String.join( ",", columnNames );
        String placeholders = makePlaceHolders( columnNames );
        String sql
                = format( "insert into %s (%s) \n"
                        + "values(%s) %n"
                        + "returning *", tableName,
                        columns, placeholders );
        try (
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            Object[] parts = mapper.explode( t );
            int j = 1;
            for ( int i = mapper.generateKey() ? 1 : 0; i < parts.length;
                    i++ ) {
                pst.setObject( j++, parts[ i ] );
            }

            try ( ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    return recordToEntity( rs );
                } else {
                    return null;
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private List<String> getColumnNames() {
        return mapper.persistentFieldNames()
                .stream()
                .filter( s -> !( mapper.keyNames().contains( s )
                && mapper.generateKey() ) )
                .collect( toList() );
    }

    private String makePlaceHolders( final List<String> columnNames ) {
        String[] qm = new String[ columnNames.size() ];
        Arrays.fill( qm, "?" );
        String placeholders = String.join( ",", qm );
        return placeholders;
    }

    @Override
    public Collection<E> getAll() {
        if ( null != transactionToken ) {
            return getAll( transactionToken.getConnection() );
        }
        try ( Connection c = this.getConnection(); ) {
            return getAll( c );
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private Collection<E> getAll( final Connection c ) {
        String sql = format( "select * from %s ", tableName );
        try (
                PreparedStatement pst = c.prepareStatement( sql );
                ResultSet rs = pst.executeQuery(); ) {
            Collection<E> result = new ArrayList<>();
            Object[] parts = createPartsArray( rs );
            while ( rs.next() ) {
                // note columns start at 1
                fillPartsArray( parts, rs );
                E e = mapper.implode( parts );
                result.add( e );
            }
            return result;
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    @Override
    public int lastId() {
        String sql = format( "select max(%s)  as lastid from %s", idName,
                tableName );
        return executeIntQuery( sql );
    }

    /**
     * Execute a query that produces an int value such as count.
     *
     * @param sql to execute
     * @return the value found or 0
     */
    final int executeIntQuery( String sql ) {
        if ( transactionToken != null ) {
            return executeIntQuery( transactionToken.getConnection(), sql );
        }
        try ( Connection c = this.getConnection(); ) {
            return executeIntQuery( c, sql );
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private int executeIntQuery( final Connection c, String sql ) {
        try (
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            try ( ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    return rs.getInt( 1 );
                } else {
                    return 0;
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    /**
     * Execute a query with a key parameter that produces and int.
     *
     * @param sql the query text
     * @param k the parameter
     * @return the value or 0 if not found
     */
    final int executeIntQuery( String sql, K k ) {
        if ( transactionToken != null ) {
            return executeIntQuery( transactionToken.getConnection(), sql, k );
        }
        try ( Connection c = this.getConnection(); ) {
            return executeIntQuery( c, sql, k );
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private int executeIntQuery( final Connection c, String sql, K k ) {
        try (
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            pst.setObject( 1, k );
            try ( ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    return rs.getInt( 1 );
                } else {
                    return 0;
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    @Override
    public int size() {
        String sql = format( "select count(1)  as size from %s", tableName );
        int result = executeIntQuery( sql );

        return result;

    }

    private Connection getConnection() {
        if ( transactionToken != null ) {
            return transactionToken.getConnection();
        } else {
            try {
                return ds.getConnection();
            } catch ( SQLException ex ) {
                Logger.getLogger( PGDAO.class.getName() ).
                        log( Level.SEVERE, null, ex );
                throw new DAOException( ex.getMessage(), ex );
            }
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
            System.out.println( "token set" );
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
     *
     * @param k lookup key
     * @return the int value matching this key.
     */
    public int getIdForKey( K k ) {
        String ks = k.toString();
        String sql = String.format( "select %s from %s where %s=?", mapper.
                idName(), mapper.tableName(), mapper.naturalKeyName() );
        return executeIntQuery( sql, k );

    }
}