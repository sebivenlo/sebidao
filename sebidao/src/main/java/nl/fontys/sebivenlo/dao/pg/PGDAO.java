package nl.fontys.sebivenlo.dao.pg;

import java.io.Serializable;
import static java.lang.String.format;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import java.util.stream.StreamSupport;
import javax.sql.DataSource;
import nl.fontys.sebivenlo.dao.AbstractMapper;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.TransactionToken;
import nl.fontys.sebivenlo.dao.Entity2;
import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;

/**
 * PostgreSQL based DAO.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <K> key to retrieve entity.
 * @param <E> type of elements for the this data access object.
 */
public class PGDAO<K extends Serializable, E extends Entity2<K>>
        implements
        DAO<K, E> {

    /**
     * When a transaction is taking place .
     */
    protected PGTransactionToken transactionToken;

    final PGDAOFactory factory;
    final AbstractMapper<K, E> mapper;

    final Map<String, String> queryTextCache;

    /**
     * Create a DAO for a data source and a mapper.
     *
     * @param fac factory for this type of daos.
     * @param ds injected through this ctor.
     * @param mapper injected through this ctor.
     * @param queryTextCache cache to save work
     */
    PGDAO( PGDAOFactory fac, DataSource ds, AbstractMapper<K, E> mapper,
            Map<String, String> queryTextCache ) {
        this.factory = fac;
        this.mapper = mapper;
        this.queryTextCache = queryTextCache;
    }

    private String allColumns() {
        return this.queryTextCache.computeIfAbsent( "allColumns",
                x -> String.join( ",", mapper
                        .persistentFieldNames() ) );
    }

    /**
     * Name of the table backing this DAO.
     *
     * @return the table name
     */
    protected String tableName() {
        return this.queryTextCache.computeIfAbsent( "tableName",
                x -> mapper.tableName() );
    }

    /**
     * The name of the key column.
     *
     * @return the name of the key column
     */
    protected String idName() {
        return this.queryTextCache.computeIfAbsent( "idName",
                x -> mapper.idName() );
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
        } catch ( SQLException ex ) { // cannot test cover this, unless connection breaks mid-air
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
        String sql = getQueryText();
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

    private String getQueryText() {
        String sql = queryTextCache.computeIfAbsent( "selectsingle", ( x )
                -> String
                        .format( "select %s from %s where %s=?", allColumns(),
                                tableName(),
                                idName() ) );
        return sql;
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
            Class<?> type = mapper.getFieldType( i ); //generatedFields().get( i );
            parts[ i ] = factory.marshallIn( type, rs.getObject( i + 1 ) );
        }
        return parts;
    }

    private Object[] createPartsArray( final ResultSet rs ) throws SQLException {
        return new Object[ mapper.getFieldCount() ];
    }

    @Override
    public void delete( E e ) {
        delete( mapper.keyExtractor().apply( e ) );
    }

    @Override
    public void delete( K k ) {
        delete( getConnection(), k );
    }

    @Override
    public void deleteAll( Iterable<E> entities ) {
        deleteAll( getConnection(), entities );
    }

    private void deleteAll( Connection con, Iterable<E> entities ) {
        List<K> keys = StreamSupport.stream( entities.spliterator(), false )
                .map( mapper.keyExtractor() )
                .collect( toList() );
        String[] questionMarks = new String[ keys.size() ];
        Arrays.fill( questionMarks, "?" );
        String marks = String.join( ",", questionMarks );
        String sql = String.format( "delete from %s where %s in (%s)", mapper
                .tableName(), mapper.idName(), marks );
        System.out.println( "sql = " + sql );
        try ( PreparedStatement pst = con.prepareStatement( sql ); ) {
            int col = 1;
            for ( K key : keys ) {
                pst.setObject( col++, key );
            }
            boolean ignored = pst.execute();
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() )
                    .log( Level.SEVERE, null, ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private void delete( final Connection con, K k ) {
        String sql = deleteQueryText();
        System.out.println( "sql = " + sql );
        try (
                PreparedStatement pst = con.prepareStatement( sql ); ) {
            pst.setObject( 1, k );
            boolean ignored = pst.execute();
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private String deleteQueryText() {

        String sql = queryTextCache.computeIfAbsent( "delete",
                x -> format( "delete from %s where %s=?", tableName(),
                        mapper.naturalKeyName() ) );
        return sql;
    }

    @Override
    public E update( E t ) {
        if ( null != transactionToken ) {
            return update( transactionToken.getConnection(), t );
        }
        try ( Connection con = this.getConnection(); ) {
            return update( con, t );
        } catch ( SQLException ex ) { // cannot test cover this, unless connection breaks mid-air
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private E update( final Connection c, E t ) {
        String sql
                = updateQueryText();
        K key = mapper.keyExtractor().apply( t );
        try ( PreparedStatement pst = c.prepareStatement( sql ); ) {
            Object[] parts = check( mapper.explode( t ) );
            check( parts );
            int j = 1;

            // all fields
            for ( Object part : parts ) {
                if ( part == null ) {
                    pst.setObject( j++, part );
                    continue;
                }
                Object po = factory.marshallOut( part );
                if ( po instanceof PGobject ) {
                    pst.setObject( j++, part, java.sql.Types.OTHER );
                } else {
                    pst.setObject( j++, part );
                }
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

    private String updateQueryText() {
        String sql = queryTextCache.computeIfAbsent( "update", x -> {
            List<String> columnNames
                    = mapper
                            .persistentFieldNames()
                            .stream()
                            .collect( toList() );
            String columns = String
                    .join( ",", columnNames );
            String placeholders = makePlaceHolders( columnNames );
            String sqlt = format( "update %s set (%s)=(%s) where (%s)=(?)"
                    + " returning * ",
                    tableName(), columns,
                    placeholders,
                    mapper
                            .idName() );
            return sqlt;
        } );
        return sql;
    }

    @Override
    public E save( E t ) {
        if ( null != transactionToken ) {
            return save( transactionToken.getConnection(), t );
        }

        try ( Connection c = this.getConnection(); ) {
            return save( c, t );
        } catch ( SQLException ex ) { // cannot test cover this, unless connection breaks mid-air
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    @Override
    public List<E> saveAll( Iterable<E> entity ) {
        if ( null != transactionToken ) {
            return saveAll( transactionToken.getConnection(), entity );
        }

        try ( Connection c = this.getConnection(); ) {
            return saveAll( c, entity );
        } catch ( SQLException ex ) { // cannot test cover this, unless connection breaks mid-air
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private E save( final Connection c, E t ) {
        String sql
                = saveQueryText();
        try (
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            populatePrepared( t, pst );

            try ( ResultSet rs = pst.executeQuery(); ) {
                if ( rs.next() ) {
                    return recordToEntity( rs );
                } else {
                    // cannot cover without serious jumping through loops, 
                    // so we will not bother.
                    return null;
                }
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private String saveQueryText() {
        String sql = queryTextCache.computeIfAbsent( "save", x -> {
            final List<String> columnNames
                    = getFilteredColumnNames();
            String columns = String
                    .join( ",", columnNames );
            String placeholders = makePlaceHolders( columnNames );
            String sqlt
                    = format( "insert into %s (%s) %n"
                            + "values(%s) %n"
                            + "returning *", tableName(),
                            columns, placeholders );
            return sqlt;
        } );
        return sql;
    }

    private List<E> saveAll( final Connection c, Iterable<E> elements ) {
        String sql
                = saveQueryText();
        List<E> result = new ArrayList<>();
        try ( PreparedStatement pst = c.prepareStatement( sql ); ) {
            for ( E t : elements ) {

                populatePrepared( t, pst );

                try ( ResultSet rs = pst.executeQuery(); ) {
                    if ( rs.next() ) {
                        result.add( recordToEntity( rs ) );
                    }
                }
            }
            return result;
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private void populatePrepared( E t, final PreparedStatement pst ) throws
            SQLException {
        Object[] parts = dropGeneneratedParts( check( mapper.explode( t ) ) );
        int j = 1;
        for ( Object part : parts ) {
            if ( part == null ) {
                pst.setObject( j++, part );
                continue;
            }
            Object po = factory.marshallOut( part );
            if ( po instanceof PGobject ) {
                pst.setObject( j++, part, java.sql.Types.OTHER );
            } else {
                pst.setObject( j++, part );
            }
        }
    }

    private List<String> getFilteredColumnNames() {
        return mapper.persistentFieldNames()
                .stream()
                .filter( isGenerated().negate() )
                .collect( toList() );
    }

    final Object[] dropGeneneratedParts( Object[] parts ) {
        List<Object> result = new ArrayList<>();
        Predicate<String> notGen = isGenerated().negate();
        int i = 0;

        for ( String pfn : mapper.persistentFieldNames() ) {
            if ( notGen.test( pfn ) ) {
                result.add( parts[ i ] );
            }
            i++;
        }
        return result.toArray();
    }

    private Predicate<String> isGenerated() {
        return s -> mapper.generatedFields().contains( s );
    }

    private String makePlaceHolders( final List<String> columnNames ) {
        String[] qm = new String[ columnNames.size() ];
        Arrays.fill( qm, "?" );
        String placeholders = String.join( ",", qm );
        return placeholders;
    }

    @Override
    public List<E> getAll() {
        if ( null != transactionToken ) {
            return getAll( transactionToken.getConnection() );
        }
        try ( Connection c = this.getConnection(); ) {
            return getAll( c );
        } catch ( SQLException ex ) { // cannot test cover this, unless connection breaks mid-air
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private List<E> getAll( final Connection c ) {

        String sql = allQuery();
        try (
                PreparedStatement pst = c.prepareStatement( sql ); ResultSet rs = pst.executeQuery(); ) {
            List<E> result = new ArrayList<>();
            Object[] parts = createPartsArray( rs );
            while ( rs.next() ) {
                // note columns start at 1
                E e = recordToEntity( rs );
                result.add( e );
            }
            return result;
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private String allQuery() {
        String sql = queryTextCache.computeIfAbsent( "all", x
                -> format( "select %s from %s ", allColumns(), tableName() ) );
        return sql;
    }

    @Override
    public List<E> getByColumnValues( Object... keyValues ) {
        if ( null != transactionToken ) {
            return getByColumnValues( transactionToken.getConnection(),
                    keyValues );
        }
        try ( Connection con = this.getConnection(); ) {
            return getByColumnValues( con, keyValues );
        } catch ( SQLException ex ) { // cannot test cover this, unless connection breaks mid-air
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }

    }

    private List<E> getByColumnValues( Connection c, Object... keyValues ) {
        //Connection c = getConnection();
        final List<String> keys = new ArrayList<>();
        final List<Serializable> values = new ArrayList<>();
        for ( int i = 0; i < keyValues.length; i += 2 ) {
            keys.add( String.class.cast( keyValues[ 0 + i ] ) );
            values.add( Serializable.class.cast( keyValues[ 1 + i ] ) );
        }
        String columns = String.join( ",", keys );
        String placeholders = makePlaceHolders( keys );

        String sql
                = format( "select %s from %s where (%s) =(%s)",
                        allColumns(), tableName(),
                        columns, placeholders );
        try (
                PreparedStatement pst = c.prepareStatement( sql ); ) {
            int j = 1;
            for ( int i = 0; i < values.size(); i++ ) {
//                pst.setObject( j++, values.get( i ) );
                Object part = values.get( i );
                if ( part == null ) {
                    pst.setObject( j++, part );
                    continue;
                }
                Object po = factory.marshallOut( part );
                if ( po instanceof PGobject ) {
                    pst.setObject( j++, part, java.sql.Types.OTHER );
                } else {
                    pst.setObject( j++, part );
                }

            }
            try ( ResultSet rs = pst.executeQuery(); ) {
                List<E> result = new ArrayList<>();
                while ( rs.next() ) {
                    // note columns start at 1
                    E e = recordToEntity( rs );
                    result.add( e );
                }
                return result;
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    @Override
    public int lastId() {
        String sql = format( "select max(%s)  as lastid from %s", idName(),
                tableName() );
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
        } catch ( SQLException ex ) { // cannot test cover this, unless connection breaks mid-air
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
        } catch ( SQLException ex ) { // cannot test cover this, unless connection breaks mid-air
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
        String sql = format( "select count(1) as size from %s", tableName() );
        int result = executeIntQuery( sql );

        return result;

    }

    /**
     * Get the connection this DAO is using. Get the connection to do hand
     * crafted queries for problems that this DAO does not support. The returned
     * connection may already be participating in a transaction when this DAO
     * already is.
     *
     * When using the returned connection it is still good style to use
     * {@link TransactionToken#commit} to commit the transaction. You can obtain
     * the token by invoking {@link DAO#getTransactionToken()}.
     *
     * @return the connection.
     */
    public Connection getConnection() {
        PGTransactionToken tok = getTransactionToken();
        if ( tok != null ) {
            return tok.getConnection();
        } else {
            try {
                return factory.getConnection();
            } catch ( SQLException ex ) {
                Logger.getLogger( PGDAO.class.getName() ).
                        log( Level.SEVERE, null, ex );
                throw new DAOException( ex.getMessage(), ex );
            }
        }
    }

    @Override
    public PGTransactionToken getTransactionToken() {
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
    public PGTransactionToken startTransaction() {
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
        String sql = format( "select %s from %s where %s=?", mapper.
                idName(), mapper.tableName(), mapper
                .naturalKeyName() );
        return executeIntQuery( sql, k );

    }

    @Override
    public void close() throws Exception {
        if ( this.transactionToken != null ) {
            transactionToken.close();
            transactionToken = null;
        }
    }

    @Override
    public String toString() {
        return "PGDAO{" + "tableName=" + tableName() + ", idName=" + idName()
                + ", mapper=" + mapper + '}';
    }

    private Object[] check( Object[] parts ) {
        if ( parts.length != mapper.persistentFieldNames().size() ) {
            throw new RuntimeException( "The number of parts in produced by the explode method"
                    + " of " + this.mapper.entityType().getCanonicalName()
                    + " does not match the number of fields in the entity, please check you Entity.asPart() method"
            );
        }

        return parts;
    }

    /**
     * Execute a query that produces a List of E.
     *
     * @param con connection to use
     * @param queryText sql text
     * @param params positional parameters in the query
     * @return list of e, produced by the query.
     */
    private List<E> anyQuery( Connection con, String queryText, Object... params ) {
        try (
                PreparedStatement pst = con.prepareStatement( queryText ); ) {
            int j = 1;
            for ( Object param : params ) {
                pst.setObject( j++, param );
            }
            try ( ResultSet rs = pst.executeQuery(); ) {
                List<E> result = new ArrayList<>();
                while ( rs.next() ) {
                    // note columns start at 1
                    E e = recordToEntity( rs );
                    result.add( e );
                }
                return result;
            }
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    /**
     * Execute a query producing a list. Use case: a function is part of the
     * query, and the query may in fact produce an update on the underlying
     * database
     *
     * @param queryText sql of prepared statement
     * @param params positional parameters to the prepared statement
     * @return a list
     */
    @Override
    public List<E> anyQuery( String queryText, Object... params ) {
        if ( transactionToken != null ) {
            return anyQuery( transactionToken.getConnection(), queryText, params );
        }
        try ( Connection con = this.getConnection(); ) {
            return anyQuery( con, queryText, params );
        } catch ( SQLException ex ) { // cannot test cover this, unless connection breaks mid-air
            Logger.getLogger( PGDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }
}
