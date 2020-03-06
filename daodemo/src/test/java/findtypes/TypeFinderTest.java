package findtypes;

import entities.DBTestHelpers;
import entities.PGDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.MINUTES;
import nl.fontys.sebivenlo.pgtypes.TSRange;
//import nl.fontys.sebivenlo.pgranges.TSRange;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.postgresql.PGConnection;
import org.postgresql.jdbc.PgParameterMetaData;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TypeFinderTest extends DBTestHelpers {

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
                    System.out.println( "columnName = '" + columnName + '\'' );

                    String columnTypeName = metaData.getColumnTypeName( i );
                    int columnType = metaData.getColumnType( i );
                    System.out.println( "columnClassName = " + columnClassName
                            + ", catalog name= '" + columnName
                            + "', columnTypeName = '" + columnTypeName + "', columnType='"
                            + columnType + "'" );
                    Object object = rs.getObject( i );
                    System.out.println( "value  = " + object.toString() );
                    PGConnection pgConnection = (PGConnection) con
                            .unwrap( Connection.class );
                    String name = pgConnection.getClass().getName();
                    System.out.println( "unwrap name = " + name );
                }
            }

        }

//        Assert.fail( "method method reached end. You know what to do." );
    }

//    @Disabled( "Think TDD" )
    @Test
    public void tryInsert() throws SQLException {
        String query = "insert into truckplans (truckid,plan) values(?,?) returning *";
        try ( Connection con = ds.getConnection(); ) {
            try (
                    PreparedStatement pst = con.prepareStatement( query ); ) {

                TSRange ts = new TSRange( LocalDateTime
                        .parse( "2020-08-15T15:35" ), Duration.of( 30, MINUTES ) );
                pst.setObject( 1, (Integer) 1 );
                PgParameterMetaData parameterMetaData = (PgParameterMetaData) pst
                        .getParameterMetaData();
                String parameterClassName = parameterMetaData
                        .getParameterClassName( 2 );
                System.out.println( "parameterMetaData = " + parameterMetaData );
                System.out
                        .println( "parameterClassName = " + parameterClassName );
                System.out.println( "ts = " + ts );
                System.out.println( "pst1 = " + pst );
                pst.setObject( 2, ts, ts.getVendorTypeNumber() );
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
                            String columnTypeName = metaData
                                    .getColumnTypeName( i );
                            System.out.println( "columnClassName = "
                                    + columnClassName
                                    + ", catalog name= " + columnName );
                            Object colValue = rs.getObject( i );
                            System.out.println( "value  = " + colValue
                                    .toString() );
                            Class<? extends Object> aClass = colValue.getClass();
                            System.out.println( "aClass = " + aClass );
                            PGConnection pgConnection = (PGConnection) con
                                    .unwrap( Connection.class );
                            String name = pgConnection.getClass().getName();
                            System.out.println( "unwrap name = " + name );
                        }
                    }
                }
            }

        } finally {

            doDDL( "truncate table truckplans" );
        }
//        Assertions.fail( "method tryInsert reached end. You know what to do." );
    }

    //@Disabled
    @Test
    void overlapPrevention() throws SQLException {
        String query = "insert into truckplans (truckid,plan) values(?,?) returning *";

        try ( Connection con = ds.getConnection();
                PreparedStatement pst = con.prepareStatement( query ); ) {
            TSRange ts1 = new TSRange( LocalDateTime
                    .parse( "2020-08-15T15:35" ), Duration.of( 30, MINUTES ) );
            TSRange ts2 = new TSRange( LocalDateTime
                    .parse( "2020-08-15T15:45" ), Duration.of( 30, MINUTES ) );
            // insert first plan, should succeed.

            pst.setObject( 1, (Integer) 1 );
            pst.setObject( 2, ts1, ts1.getVendorTypeNumber() );
            ThrowingCallable executeInsert = () -> pst.executeQuery();

            assertThatCode( executeInsert ).doesNotThrowAnyException();

            pst.setObject( 1, (Integer) 1 );
            pst.setObject( 2, ts2, ts1.getVendorTypeNumber() );
            Assertions.assertThatThrownBy( executeInsert )
                    .isInstanceOf( SQLException.class )
                    .hasMessageContaining( "violates exclusion constraint" );

        } finally {

            doDDL( "truncate table truckplans" );
        }
//        Assertions
//                .fail( "test overlapPrevention reached it's and. You will know what to do." );
    }

    @AfterEach
    void cleanup() throws SQLException {
//        doDDL( "truncate table truckplans" );

    }
}
