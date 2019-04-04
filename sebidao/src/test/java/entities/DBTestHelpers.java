package entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import static java.util.stream.Collectors.joining;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;
import org.junit.BeforeClass;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class DBTestHelpers {

    protected static PGDataSource ds = PGDataSource.DATA_SOURCE;
    protected static PGDAOFactory daof;

    protected static void loadDatabase() throws IOException, SQLException {
        String ddl
                = Files.lines( Paths.get( "dbscripts/newpiet.sql" ) )
                        .filter( l -> !l.startsWith( "--" ) )
                        .collect( joining( System.lineSeparator() ) );
        doDDL( ddl );
    }

    protected static void doDDL( String ddl ) throws
            SQLException {
        try ( Connection con = ds.getConnection() ) {
            con.prepareStatement( ddl ).execute();
        }
    }

    @BeforeClass
    public static void setupClass() {
        daof = new PGDAOFactory( ds );
        daof.registerMapper( Employee.class, new EmployeeMapper2( Integer.class, Employee.class ) );

    }
}
