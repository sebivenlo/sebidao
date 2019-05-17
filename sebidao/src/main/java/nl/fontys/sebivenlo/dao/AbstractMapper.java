package nl.fontys.sebivenlo.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.LinkedHashSet;
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
public abstract class AbstractMapper<K, E> {//implements Mapper<K, E> {

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
        deriveDisAssemblerFunction();
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
                            + assemblerCtor
                            + "\n" + parameters( a ), ex );
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

    Function<E, Object[]> disAssembler;

    /**
     * Reflect on fields and methods and try to find getters for all persistent
     * fields. Combine getter invocations to take the E apart.
     */
    final void deriveDisAssemblerFunction() {

        if ( tryAsPart() ) {
            return;
        }

        tryFieldAndMethodReflection();
    }

    final void tryFieldAndMethodReflection() {
        Set<String> fieldNames = entityMetaData.typeMap.keySet();
        final Method[] getters = new Method[ fieldNames.size() ];
        final int fieldCount = entityMetaData.typeMap.size();
        int g = 0;
        for ( String fieldName : fieldNames ) {
            try {
                String getterName = "get" + fieldName.substring( 0, 1 ).toUpperCase() + fieldName.substring( 1 );
                Method m = entityType.getDeclaredMethod( getterName );
                if ( m.isAccessible() ) {
                    Logger.getLogger( AbstractMapper.class.getName() ).log( Level.INFO, "method is not accessible" + m );
                } else {
                    getters[ g ] = entityType.getDeclaredMethod( getterName );
                }
                g++;
            } catch ( NoSuchMethodException | SecurityException ex ) {
                Logger.getLogger( AbstractMapper.class.getName() ).log( Level.INFO, null, ex );
            }
        }
        if ( g == entityMetaData.typeMap.size() ) {
            disAssembler = ( E e ) -> {
                Object[] result = new Object[ fieldCount ];
                int i = 0;
                for ( Method getter : getters ) {
                    try {
                        if ( null != getter ) {
                            result[ i ] = getter.invoke( e );
                        }
                    } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException ex ) {
                        Logger.getLogger( AbstractMapper.class.getName() ).log( Level.SEVERE, null, ex );
                    }
                    i++;
                }
                return result;
            };
        }
    }

    final boolean tryAsPart() {
        try {
            // first try asParts
            final Method asParts = entityType.getMethod( "asParts" );
            if ( asParts == null ) {
                return false;
            }
            Class<?> returnType = asParts.getReturnType();
            if ( returnType.isArray() ) {
                disAssembler = ( E e ) -> {
                    Object[] result = null;
                    try {
                        result = (Object[]) asParts.invoke( e );
                    } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException ex ) {
                        Logger.getLogger( AbstractMapper.class.getName() ).log( Level.SEVERE, null, ex );
                    }
                    return result;
                };
                return true;
            }
        } catch ( NoSuchMethodException | SecurityException ex ) {
            Logger.getLogger( AbstractMapper.class.getName() ).log( Level.FINE, null, ex );
        }
        return false;
    }

    /**
     * Get entity type.
     *
     * @return the type.
     */
//    @Override
    public Class<E> entityType() {
        return this.entityType;
    }

    /**
     * Get the column names.
     *
     * @return the set of names.
     */
//    @Override
    public Set<String> persistentFieldNames() {
        return entityMetaData.typeMap.keySet();
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
//    @Override
    public E implode( Object... parts ) {
        if ( assembler != null ) {
            Object[] checkedParts = check( parts );
            return assembler.apply( checkedParts );
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

    /**
     * Read the persistent fields of the object as an array.
     *
     * @param e the object to read.
     * @return the fields as array.
     */
    public Object[] explode( E e ) {
        if ( null == disAssembler ) {
            throw new IllegalStateException( "the definition of " + entityType.getCanonicalName()
                    + " does provide the required getters" );
        }
        return disAssembler.apply( e );
    }

    /**
     * Get the table name for the entity.
     *
     * @return the name of the table in the database
     */
    public String tableName() {
        return entityType().getSimpleName().toLowerCase() + 's';
    }

    /**
     * Get the column name(s) for the key column(e), typically the forming
     * primary key.
     *
     * Note that the minimal, but also default length of the returned array is
     * one.
     *
     * @return the id column name
     */
    public Set keyNames() {
        return new LinkedHashSet<>( asList( idName() ) );
    }

    /**
     * Get the name of the id field or column.
     *
     * @return the name.
     */
    public String idName() {
        return entityType().getSimpleName().toLowerCase() + "id";
    }

    /**
     * Get the name of the natural key of this relation, if it is not the
     * default.
     *
     * @return the key name
     */
    public String naturalKeyName() {
        return idName();
    }

    /**
     * Get a field type by position.
     *
     * @param i the field position (order) in the entity.
     * @return the field.
     */
    public Class<?> getFieldType( int i ) {
        return entityMetaData.getFieldType( i );
    }

    /**
     * Get the numer for fields of the mapped entity.
     *
     * @return the fieldCount
     */
    public int getFieldCount() {
        return entityMetaData.getFields().size();
    }

    /**
     * Get the id from the entity.
     *
     * @return a method to retrieve the id.
     */
    public abstract Function<E, K> keyExtractor();

    private Object[] check( Object[] parts ) {

        if ( parts.length >= getFieldCount() ) {
            return parts;
        }
        return Arrays.copyOf( parts, getFieldCount() );

    }

    private static String parameters( Object[] a ) {
        StringBuilder sb = new StringBuilder( "received parameters (type=value) {\n" );
        for ( Object object : a ) {
            sb.append( "\t(" )
                    .append( object.getClass().toString() )
                    .append( "=" )
                    .append( object.toString() )
                    .append( ')' )
                    .append( System.lineSeparator() );
        }
        sb.append( "}" );
        return sb.toString();
    }
}
