package nl.fontys.sebivenlo.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Coverage.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class DAOTest {

    DAO dao
            = new DAO() {
        @Override
        public Optional get( Serializable id ) {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

        @Override
        public Collection getAll() {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

        @Override
        public Entity2 save( Entity2 e ) {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

        @Override
        public Entity2 update( Entity2 e ) {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

        @Override
        public void delete( Entity2 e ) {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

    };

    TransactionToken ignoredToken = new TransactionToken() {
    };

    /**
     * This test serves to cover the default implemenations in DAO.
     *
     * @throws Exception no expected
     */
    @Test
    public void testAllCovered() throws Exception {

        assertNull( "no transaction", dao.startTransaction() );
        dao.setTransactionToken( ignoredToken );
        assertNull( "no transaction", dao.getTransactionToken() );
        assertEquals( "empty to start with", 0, dao.size() );
        assertEquals( "last id", 0, dao.lastId() );
        try {
            dao.close();
        } catch ( Exception e ) {
            fail( "should not throw exception, threw " + e );
        }
        //fail( "testAllCovered not yet implemented. Review the code and comment or delete this line" );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testNotImplemented() {
        Collection notexepected = dao.getByColumnValues( "key", "value" );
        // fail( "test method testNotImplemented reached its end, you can remove this line when you aggree." );
    }

}
