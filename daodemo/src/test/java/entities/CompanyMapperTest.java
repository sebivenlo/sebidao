package entities;

import entities.Company;
import entities.CompanyMapper;
import nl.fontys.sebivenlo.dao.AbstractMapper;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class CompanyMapperTest {

    CompanyMapper mapper;

    @Before
    public void setup() {

        mapper = new CompanyMapper(  );

    }


    @Test
    public void testCreateCompany() {
        Object[] a = new Object[] { "Pieters", "Belgium", "Waterschei",
            "Molenstraat 20", "PITRS", "B46587", 0, null };
        Company comp = mapper.implode( a );
        assertNotNull("Got a product",comp);
        System.out.println( "comp = " + comp );
    }
}
