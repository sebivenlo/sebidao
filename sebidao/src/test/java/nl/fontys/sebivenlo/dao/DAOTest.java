package nl.fontys.sebivenlo.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

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

        @Override
        public void delete( Serializable k ) {
            throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
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

        assertThat( dao.startTransaction() ).isNull();
        dao.setTransactionToken( ignoredToken );
        assertThat( dao.getTransactionToken() ).isNull();
        assertThat( dao.size() ).isEqualTo( 0 );
        assertThat( dao.lastId() ).isEqualTo( 0 );
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
