package nl.fontys.sebivenlo.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

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
 * If the entity class provides an 'all-fields' constructor, with the field
 * parameters in field declaration order, this mapper is able to find that
 * combination and can use it in the default implode method.
 *
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
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
    private final EntityMetaData<E> entityMetaData;

    private List<String> generatedFields = null;

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
        deriveAssemblerFunction();
    }

    /**
     * Try to find a constructor that take as parameter types the fields types
     * in field declaration order.
     *
     * If the method succeeds the result will be stored as a
     * {@code Function&lt;Object[],E&gt;} assembler.
     *
     * The programmer of the entity class is kindly advised to create the
     * desired constructor.
     *
     * @param entityType1
     */
    final void deriveAssemblerFunction() {
        Collection<Class<?>> fieldTypes = entityMetaData.typeMap.values();
        Class[] types = new Class[ fieldTypes.size() ];
        int p = 0;
        for ( Class<?> clz : fieldTypes ) {
            types[ p++ ] = clz;
        }
        try {
            Constructor<E> assemblerCtor = entityType.getConstructor( types );
            System.out.println( "found assemblerCtor = " + assemblerCtor );
            assembler = ( Object[] a ) -> {
                try {
                    return assemblerCtor.newInstance( a );
                } catch ( InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException ex ) {
                    Logger.getLogger( AbstractMapper.class.getName() ).
                            log( Level.SEVERE, null, ex );
                    throw new DAOException(
                            "could not invoke assembler constructor "
                            + assemblerCtor, ex );
                }
            };
        } catch ( NoSuchMethodException | SecurityException ex ) {
            Logger.getLogger( AbstractMapper.class.getName() ).
                    log( Level.INFO, "cannot find assembler constructor "
                            + assemblerCtor(), ex );
            //throw new DAOException( "cannot find assembler constructor "+assemblerCtor(), ex );
        }
    }

    Function<Object[], E> assembler;

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

    /**
     * Get the types of the columns (fields) in declaration order.
     *
     * @return the java types of the fields.
     */
    public List<Class<?>> persistentFieldTypes() {
        List<Class<?>> result = new ArrayList<>( entityMetaData.typeMap.size() );
        result.addAll( entityMetaData.typeMap.values() );
        return result;
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
        return "AbstractMapper{" + "keyType=" + keyType + "\n, entityType="
                + entityType + ",\n entityMetaData=" + entityMetaData + '}';
    }

    /**
     * Override this method for a more efficient implementation.
     *
     * @param parts to use in the construction of the entity
     * @return the fresh entity instance.
     */
    @Override
    public E implode( Object[] parts ) {
        if ( assembler != null ) {
            return assembler.apply( parts );
        } else {
            throw new DAOException(
                    "No assembler constructor found with signature "
                    + assemblerCtor() );
        }
    }

    /**
     * A string representation of a constructor the entityType preferably should
     * have.
     *
     * @return the string
     */
    String assemblerCtor() {
        StringBuilder sb = new StringBuilder( entityType.getSimpleName() );
        sb.append( "( " );
        String params
                = entityMetaData.typeMap.values().stream().map( c -> c.
                getSimpleName() )
                        .collect( Collectors.joining( ", " ) );
        return sb.append( params ).append( ')' ).toString();
    }

    public List<String> generatedFields() {
        if ( generatedFields == null ) {
            Field[] declaredFields = entityType.getDeclaredFields();

            generatedFields = Arrays.stream( declaredFields )
                    .filter( this::isGenerated )
                    .map( Field::getName )
                    .collect( toList() );
        }
        return generatedFields;
    }

    private boolean isGenerated( Field f ) {
        ID idannotation = f.getAnnotation( ID.class );
        Generated genannotation = f.getAnnotation( Generated.class );

        return null != genannotation || ( null != idannotation && idannotation.generated() );
    }

}
