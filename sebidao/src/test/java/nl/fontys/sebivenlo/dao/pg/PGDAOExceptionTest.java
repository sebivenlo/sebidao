/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.dao.pg;

import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.mock;

/**
 * Test if all sql exceptions are properly translated (as in wrapped in) into
 * DAOExceptions. For coverage. The methods all will find a connection that is
 * broken and throws an exception.
 *
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class PGDAOExceptionTest extends PGDAOExceptionTestBase {

    @Override
    Connection getConnection() {
        Connection conn = mock( Connection.class, anything -> {
            throw new SQLException( "I am supposed to" );
        } );
        return conn;
    }
}
