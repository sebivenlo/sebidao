package nl.fontys.sebivenlo.dao.pg;

import entities.PGDataSource;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.HashMap;
import java.util.Map;
import nl.fontys.sebivenlo.pgtypes.LocalDateTimeRange;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.postgresql.PGConnection;
import org.postgresql.jdbc.PgParameterMetaData;
import org.postgresql.util.PGobject;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TypeFinderTest {

    public TypeFinderTest() {
    }

    static PGDataSource ds = PGDataSource.DATA_SOURCE;

//    @Ignore( "Think TDD" )
    @Test
    public void findTypes() throws SQLException {

        String query = "select * from truckplans natural join trucks";
        try ( Connection con = ds.getConnection();
                PreparedStatement pst = con.prepareStatement( query );
                ResultSet rs = pst.executeQuery(); ) {
            if ( rs.next() ) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for ( int i = 1; i <= columnCount; i++ ) {
                    System.out.println( "====" );
                    String columnClassName = metaData.getColumnClassName( i );
                    String columnName = metaData.getColumnName( i );
                    System.out.println( "columnName = " + columnName );

                    String columnTypeName = metaData
                            .getColumnTypeName( columnCount );

                    System.out
                            .println( "columnClassName = " + columnClassName + ", catalog name= " + columnName + ", columnTypeName = " + columnTypeName );
                    Object object = rs.getObject( i );
                    System.out.println( "value  = " + object.toString() );
                    PGConnection pgConnection = (PGConnection) con
                            .unwrap( Connection.class );
                    String name = pgConnection.getClass().getName();
                    System.out.println( "unwrap name = " + name );
                }
            }

        }

        // Assert.fail( "method method reached end. You know what to do." );
    }

//    @Ignore( "Think TDD" )
    @Test
    public void tryInsert() throws SQLException {
        String query = "insert into truckplans (truckid,plan) values(?,?) returning *";
        try ( Connection con = ds.getConnection(); ) {
            PGConnection pgcon = (PGConnection) con;
//            pgcon.addDataType( "tsrange", LocalDateTimeRange.class );
            try (
                    PreparedStatement pst = con.prepareStatement( query ); ) {

                LocalDateTimeRange ts = new LocalDateTimeRange( LocalDateTime
                        .parse( "2018-08-15T15:35" ), Duration.of( 30, MINUTES ) );
                pst.setObject( 1, (Integer) 1 );
                PgParameterMetaData parameterMetaData = (PgParameterMetaData) pst
                        .getParameterMetaData();
                String parameterClassName = parameterMetaData
                        .getParameterClassName( 2 );
                System.out.println( "parameterMetaData = " + parameterMetaData );
                System.out
                        .println( "parameterClassName = " + parameterClassName );
                System.out.println( "ts = " + ts );
                System.out.println( "pst 1 = " + pst );
                pst.setObject( 2, ts );
                System.out.println( "pst2 = " + pst );
                try (
                        ResultSet rs = pst.executeQuery(); ) {
                    if ( rs.next() ) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        for ( int i = 1; i <= columnCount; i++ ) {
                            String columnClassName = metaData
                                    .getColumnClassName( i );
                            String columnName = metaData.getColumnName( i );
                            System.out
                                    .println( "columnClassName = " + columnClassName + ", catalog name= " + columnName );
                            Object object = rs.getObject( i );
                            System.out
                                    .println( "value  = " + object.toString() );
                            PGConnection pgConnection = (PGConnection) con
                                    .unwrap( Connection.class );
                            String name = pgConnection.getClass().getName();
                            System.out.println( "unwrap name = " + name );
                        }
                    }
                }
            }

        }

//        Assert.fail( "method tryInsert reached end. You know what to do." );
    }
}
