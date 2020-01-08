package restserver;

import nl.fontys.sebivenlo.postrest.AbstractPostRestService;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
@Stateless
@Path( "teachers" )
public class TeachersService extends DAOCrudService {

    @Override
    protected String getRelName() {
        return "teachers";
    }
}
