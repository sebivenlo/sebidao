package nl.fontys.sebivenlo.dao.pg;

import java.sql.Connection;
import java.sql.SQLException;
import nl.fontys.sebivenlo.dao.DAOException;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * For coverage.
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class PGTransactionTokenTest {

    @Mock
    Connection conn;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks( this );

        Mockito.doThrow( new SQLException( "Just for fun" ) ).when( conn ).setAutoCommit( false );
    }

    @Test( expected = DAOException.class )
    public void testExecptionOnConnection() {

        PGTransactionToken tok = new PGTransactionToken( conn );
    }

}
