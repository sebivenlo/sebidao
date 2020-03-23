package nl.fontys.sebivenlo.dao.pg;

import entities.DBTestHelpers;
import entities.Email;
import entities.Employee;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class PGDAOTestBase extends DBTestHelpers {

    PGDAO<Integer, Employee> eDao;

    @BeforeAll
    public static void setupClass() {
        DBTestHelpers.setupClass();
        loadDatabase();
    }

    @BeforeEach
    void setUp() throws Throwable {
        assertThat( daof ).isNotNull();
//        daof.registerInMarshaller( Email.class, Email::new );
//        daof.registerOutMarshaller( Email.class, x-> PGDAOFactory.pgobject("citext", x ));
        daof.registerPGMashallers( Email.class, Email::new, x -> PGDAOFactory.pgobject( "citext", x ) );
        eDao = daof.createDao( Employee.class );

    }

}
