package entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.joining;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;
import org.junit.jupiter.api.BeforeAll;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class DBTestHelpers {

    protected static PGDataSource ds = PGDataSource.DATA_SOURCE;
    protected static PGDAOFactory daof;

    protected static void loadDatabase() {
        try {
            String ddl
                    = Files.lines( Paths.get( "dbscripts/newpiet.sql" ) )
                            .filter( l -> !l.startsWith( "--" ) )
                            .collect( joining( System.lineSeparator() ) );
            doDDL( ddl );
        } catch ( IOException | SQLException ex ) {
            Logger.getLogger( DBTestHelpers.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }
    }

    protected static void doDDL( String ddl ) throws
            SQLException {
        try ( Connection con = ds.getConnection();
                PreparedStatement pst = con.prepareStatement( ddl ); ) {
            System.out.println( "loading database" );
            pst.execute();
            System.out.println( "database ready for use" );
        }
    }

    @BeforeAll
    public static void setupClass() {
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, new EmployeeMapper2() );
//        daof.registerMapper( Employee.class, new EmployeeMapper());

    }
}
