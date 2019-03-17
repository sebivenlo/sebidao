package entities;

import nl.fontys.sebivenlo.dao.pg.PGDao;
import javax.sql.DataSource;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class EmployeeDao extends PGDao<Integer,Employee> {

    public EmployeeDao( DataSource ds ) {
        super( ds, new EmployeeMapper() );
    }


    @Override
    public Employee update( Employee t ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


}
