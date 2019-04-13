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
import static java.util.logging.Logger.*;

/**
 * Test if all sql exceptions are properly translated (as in wrapped in) into
 * DAOExceptions. For coverage. The methods all will find a connection that is
 * broken and throws an exception.
 *
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class PGDAOConnectionCloseExceptionTest extends PGDAOExceptionTestBase {

    @Override
    Connection getConnection() {
        try {
            Connection realConnection = DBTestHelpers.ds.getConnection();
            return new NonClosingConnection( realConnection );
        } catch ( SQLException ex ) {
            getLogger( PGDAOConnectionCloseExceptionTest.class.getName() )
                    .log( Level.SEVERE, null, ex );
        }
        return null;
    }
}
