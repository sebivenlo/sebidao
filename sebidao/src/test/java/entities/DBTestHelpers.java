package entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import static java.util.stream.Collectors.joining;
import nl.fontys.sebivenlo.dao.pg.PGDAOFactory;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class DBTestHelpers {
        static PGDataSource ds = PGDataSource.DATA_SOURCE;
    static PGDAOFactory daof;

        static void loadDatabase() throws IOException, SQLException {
        String ddl
                = Files.lines( Paths.get( "dbscripts/newpiet.sql" ) )
                        .filter( l -> !l.startsWith( "--" ) )
                        .collect( joining( System.lineSeparator() ) );
        doDDL( ddl );
    }

    static void doDDL( String ddl ) throws
            SQLException {
        try ( Connection con = ds.getConnection() ) {
            con.prepareStatement( ddl ).execute();
        }
    }

}
