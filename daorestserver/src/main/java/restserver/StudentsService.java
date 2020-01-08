package restserver;

import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Pieter van den Hombergh {@code <pieter.van.den.hombergh@gmail.com>}
 */
@Stateless
@Path( "students" )
public class StudentsService extends DAOCrudService {

    @Override
    protected String getRelName() {
        return "students";
    }

}
