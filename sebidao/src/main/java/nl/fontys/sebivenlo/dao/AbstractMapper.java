package nl.fontys.sebivenlo.dao;

import java.util.Set;

/**
 * Simplify the creation of a Mapper by using a bit of reflection and some
 * caching.
 *
 * Creating a mapper by extending this class is best shown using an example. The
 * obvious imports are assumed.
 *
 * <pre class='brush:java'>
 * public class EmployeeMapper2 extends AbstractMapper&lt;Integer,Employee&gt; {
 *
 * public EmployeeMapper2( Class&lt;Integer&gt; keyType ,Class&lt;Employee&gt;
 * entityType) { super( keyType,entityType ); }
 *
 * &#64;Override public Object[] explode( Employee e ) { return e.asParts(); }
 *
 * &#64;Override public Employee implode( Object... parts ) { return new
 * Employee(parts ); }
 *
 * &#64;Override public Function&lt;Employee, Integer&gt; keyExtractor() {
 * return Employee::getEmployeeid; }
 *
 * }
 * </pre>
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 * @param <K> key type.
 * @param <E> entity type.
 */
public abstract class AbstractMapper<K, E> implements Mapper<K, E> {

    /**
     * The key type for this mapper.
     */
    protected final Class<K> keyType;
    /**
     * The entity type for this mapper.
     */
    protected final Class<E> entityType;

    /**
     * Cached knowledge about the entity type.
     */
    private EntityMetaData<E> entityMetaData;

    /**
     * Create a mapper for entity and key type.
     *
     * @param entityType for entity
     * @param keyType for key
     */
    public AbstractMapper( Class<K> keyType, Class<E> entityType ) {
        this.entityType = entityType;
        this.keyType = keyType;
        entityMetaData = new EntityMetaData<>( entityType );
    }

    /**
     * Get entity type.
     *
     * @return the type.
     */
    @Override
    public Class<E> entityType() {
        return this.entityType;
    }

    /**
     * Get the column names.
     *
     * @return the set of names.
     */
    @Override
    public Set<String> persistentFieldNames() {
        return entityMetaData.typeMap.keySet();
    }

    @Override
    public boolean generateKey() {
        return entityMetaData.isIDGenerated();
    }

    @Override
    public String idName() {
        return entityMetaData.getIdName();
    }

    @Override
    public String toString() {
        return "AbstractMapper{" + "keyType=" + keyType + "\n, entityType=" + entityType + ",\n entityMetaData=" + entityMetaData + '}';
    }

}
