/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.mockito.Mockito.mock;

/**
 * Test if all sql exceptions are properly translated (as in wrapped in) into
 * DAOExceptions. For coverage. The methods all will find a connection that is
 * broken and throws an exception.
 *
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class PGDAOConnectionCloseExceptionTest extends PGDAOExceptionTestBase {

    @Override
    Connection getConnection() {
        try {
            Connection realConnection = DBTestHelpers.ds.getConnection();
            return new NonClosingConnection( realConnection );
        } catch ( SQLException ex ) {
            Logger.getLogger( PGDAOConnectionCloseExceptionTest.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return null;
    }
}
