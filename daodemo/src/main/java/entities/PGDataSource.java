package entities;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * This data source can be used in simple java se applications
 * and doesn't depend on container like GlassFish
 * @author hvd
 */
public enum PGDataSource implements DataSource{
    
    DATA_SOURCE;

    private final PGSimpleDataSource dataSource;

    private PGDataSource() {

        dataSource = new PGSimpleDataSource();

        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("simpledao");
        dataSource.setUser("exam");
        dataSource.setPassword("exam");

    }

    /**
     * Get a connection.
     *
     * @return a java.sql.Connection to the data source.
     * @throws java.sql.SQLException
     */
    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
 
}

    @Override
    public Connection getConnection( String username, String password ) throws
            SQLException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void setLogWriter( PrintWriter out ) throws SQLException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void setLoginTimeout( int seconds ) throws SQLException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public <T> T unwrap( Class<T> iface ) throws SQLException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public boolean isWrapperFor(
            Class<?> iface ) throws SQLException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}