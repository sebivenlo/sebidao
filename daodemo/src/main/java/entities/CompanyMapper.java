package entities;

import java.util.function.Function;
import nl.fontys.sebivenlo.dao.AbstractMapper;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class CompanyMapper extends AbstractMapper<String,Company> {

    public CompanyMapper( Class<String> keyType, Class<Company> entityType ) {
        super( keyType, entityType );
    }

    @Override
    public Object[] explode( Company e ) {
        return e.asParts();
    }

    @Override
    public Function<Company, String> keyExtractor() {
        return Company::getTicker;
    }

    @Override
    public String tableName() {
        return "companies";
    }
    
}
