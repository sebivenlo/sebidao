package restserver;

import javax.ejb.Stateless;
import javax.ws.rs.Path;
import nl.fontys.sebivenlo.dao.Entity2;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
@Stateless
@Path( "courses" )
public class CoursesService extends DAOCrudService<Integer,Course> {

    @Override
    Class<? extends Entity2> getEntityType() {
        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }


}
