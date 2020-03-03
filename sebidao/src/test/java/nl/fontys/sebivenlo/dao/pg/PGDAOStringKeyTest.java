package nl.fontys.sebivenlo.dao.pg;

import entities.Company;
import entities.CompanyMapper;
import entities.DBTestHelpers;
import java.util.Collection;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.Entity2;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class PGDAOStringKeyTest extends DBTestHelpers {

    DAO<String, Company> cDao;

    @Before

    public void setup() throws Exception {
        loadDatabase();
        daof.registerMapper( Company.class, new CompanyMapper() );
        cDao = (DAO<String, Company>) daof.createDao(
                Company.class );

    }

    Company intel = new Company( "INTC", "Intel", "USA", "Santa Clara",
                                 "2200 Mission College Blvd", "CA 95052" );
    Company amd = new Company( "AMD", "Advanced Micro Devices", "USA",
                               "Santa Clara",
                               "2485 Augustine Drive", "CA 95054" );

    Company fontys = new Company( "FNTS", "Fontys Hogescholen", "Netherlands",
                                  "Venlo", "Tegelseweg 255", "5912 BG" );

    @Test
    public void testSave() {

        int size = cDao.size();
        Company savedC = cDao.save( intel );
        int size2 = cDao.size();

        assertThat( size2 ).as( "added" ).isEqualTo( size + 1 );
        assertThat( savedC.getName() ).isEqualTo( intel.getName() );
//        fail( "testSave not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testDelete() {
        int size = cDao.size();
        Company savedC = cDao.save( amd );
        int size2 = cDao.size();
        cDao.delete( savedC );
        int size3 = cDao.size();

        assertThat(  size + 1 == size2 && size == size3 ).as("in and gone").isTrue();

        //fail( "testDelete not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testUpdate() {
        Company sFontys = cDao.save( fontys );

        sFontys.setAddress( "Hulsterweg 6" );
        sFontys.setPostcode( "5912 PL" );

        Company uFontys = cDao.update( sFontys );

        Company dFontys = cDao.get( sFontys.getNaturalId() ).get();

        assertThat( dFontys.getPostcode() ).isEqualTo( sFontys.getPostcode() );
        Collection<Company> all = cDao.getAll();
        assertThat( all.isEmpty() ).isFalse();
        // fail( "testUpdate not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testSanityAnCoverage() {

        assertThat( cDao.toString().isEmpty() ).isFalse();
        assertThat( cDao.size() ).isEqualTo( 0 );
        //fail( "testSanityAnCoverage not yet implemented. Review the code and comment or delete this line" );
    }
}
