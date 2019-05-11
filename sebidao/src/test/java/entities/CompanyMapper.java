package entities;

import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class CompanyMapper extends AbstractMapper<String, Company> {

    public CompanyMapper() {
        super( String.class, Company.class );
    }

    @Override
    public Function<Company, String> keyExtractor() {
        return Company::getTicker;
    }

    @Override
    public String idName() {
        return "ticker";//super.idName(); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public String tableName() {
        return "companies";
    }

}
