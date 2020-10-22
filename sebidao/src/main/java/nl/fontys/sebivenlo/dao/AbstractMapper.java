package nl.fontys.sebivenlo.dao;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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
import java.util.Map;
import static java.util.Map.entry;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
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
public abstract class AbstractMapper<K, E> {

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
        deriveAssemblerFunction2();
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
                    Logger.getLogger( AbstractMapper.class.getName() )
                            .log( Level.SEVERE, null, ex );
                    throw new DAOException(
                            "could not invoke assembler constructor "
                            + assemblerCtor
                            + "\n" + parameters( a ), ex );
                }
            };
        } catch ( NoSuchMethodException | SecurityException ex ) {
            Logger.getLogger( AbstractMapper.class.getName() )
                    .log( Level.INFO, "cannot find assembler constructor "
                            + assemblerCtor(), ex );
            //throw new DAOException( "cannot find assembler constructor "+assemblerCtor(), ex );
        }
    }

    final void deriveAssemblerFunction2() {

        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        Collection<Class<?>> fieldTypes = entityMetaData.typeMap.values();
        Class[] types = new Class[ fieldTypes.size() ];
        int p = 0;
        for ( Class<?> clz : fieldTypes ) {
            types[ p++ ] = clz;
        }
        MethodType mt = MethodType.methodType( void.class, types );
        try {
            MethodHandle ctorH = lookup.findConstructor( entityType, mt );
            assembler = ( Object[] args ) -> {
                E result = null;
                try {
                    result = (E) ctorH.invokeWithArguments( args );
                } catch ( Throwable ex ) {
                    Logger.getLogger( AbstractMapper.class.getName() ).log( Level.SEVERE, null, ex );
                }
                return result;
            };
        } catch ( NoSuchMethodException | IllegalAccessException ex ) {
            Logger.getLogger( AbstractMapper.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    Function<Object[], E> assembler;

    Function<E, Object[]> disAssembler;

    /**
     * Reflect on fields and methods and try to find getters for all persistent
     * fields. Combine getter invocations to take the E apart.
     */
    final void deriveDisAssemblerFunction() {

        if ( tryAsParts() ) {
            return;
        }

        tryFieldAndMethodReflection();
    }

    /**
     * Create disassembler completely from getters.
     */
    /**
     * Create disassembler completely from getters.
     */
    final void tryFieldAndMethodReflection() {
        final int fieldCount = entityMetaData.typeMap.keySet().size();
        final List<MethodHandle> getters = getGetters( entityType.getDeclaredFields() );
        System.out.println( "getters=" + getters );
        disAssembler = ( E e ) -> {
            int i = 0;
            Object[] result = new Object[ fieldCount ];
            try {
                for ( MethodHandle getter : getters ) {
                    result[ i ] = getter.invoke( e );
                    i++;
                }
            } catch ( Throwable ex ) {
                Logger.getLogger( AbstractMapper.class.getName() ).log( Level.SEVERE, null, ex );
            }
            return result;
        };
    }

    private static final Object[] EMPTY_OBJ_ARRAY = new Object[ 0 ];

    /**
     * For the case that the user/programmer provides and asParts method, use
     * that to disassemble the entity. Using this method is assumed to be more
     * efficient in startup and execution.
     *
     * @return true if asPart method is available and cache reflectively created
     * disassembler.
     */
    final boolean tryAsParts() {
        MethodType mt = MethodType.methodType( Object[].class );

        try {
            final MethodHandle asPartsMH = MethodHandles.lookup().findVirtual( entityType, "asParts", mt );

            disAssembler = ( E e ) -> {
                Object[] result = EMPTY_OBJ_ARRAY;
                try {
                    result = (Object[]) asPartsMH.invoke( e );
                } catch ( Throwable ex ) {
                    Logger.getLogger( AbstractMapper.class.getName() ).log( Level.SEVERE, null, ex );
                }
                return result;
            };
            return true;
        } catch ( NoSuchMethodException | IllegalAccessException ex ) {
            Logger.getLogger( AbstractMapper.class.getName() ).log( Level.SEVERE, null, ex );
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
                = entityMetaData.typeMap.values().stream()
                        .map( c -> c.getSimpleName() )
                        .collect( Collectors.joining( ", " ) );
        return sb.append( params ).append( ')' ).toString();
    }

    /**
     * Get the names of the generated fields.
     *
     * @return the list of names.
     */
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
        TableName annotation = this.entityType().getAnnotation( TableName.class );
        if ( annotation != null ) {
            return annotation.value();
        }
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
     * Get the number for fields of the mapped entity.
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

    private List<MethodHandle> getGetters( Field[] fields ) {
        return Arrays.stream( fields )
                .filter( f -> normalField( f ) )
                .map( f -> getGetter( f ) )
                .collect( toList() );
    }

    /**
     * Get the public getter for a field. Turn throwing method into logging
     * method. This method tries to find for the declared filed type and if
     * fails and the field type is a wrapper type (e.g. Integer) a second
     * attempt is for a getter with the primitive return type.
     *
     * @param field filed to get getGetter for
     * @return the getter method handle for the field
     */
    MethodHandle getGetter( Field field ) {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle result = null;

        MethodType mt = MethodType.methodType( field.getType() );
        String getterName = getterNameFor( field );

        try {
            try { // 1st attempt
                result = lookup.findVirtual( entityType, getterName, mt );
            } catch ( NoSuchMethodException ex1 ) { // 2nd attempt
                Class<?> primType = wrapperToPrimitive.get( field.getType() );
                if ( null != primType ) {
                    mt = MethodType.methodType( primType );
                    result = lookup.findVirtual( field.getDeclaringClass(), getterName, mt );
                }
            }
        } catch ( IllegalAccessException | NoSuchMethodException ex ) {
            Logger.getLogger( field.getDeclaringClass().getName() ).log( Level.SEVERE, "for field " + field, ex );
        }
        return result;
    }

    /* to allow a second attempt when getter return type and field type differ by wrapping or unwrapping. */
    private static final Map<Class<?>, Class<?>> wrapperToPrimitive
            = Map.ofEntries(
                    entry( Integer.class, int.class ),
                    entry( int.class, Integer.class ),
                    entry( Long.class, long.class ),
                    entry( long.class, Long.class ),
                    entry( Boolean.class, boolean.class ),
                    entry( boolean.class, Boolean.class ),
                    entry( Character.class, char.class ),
                    entry( char.class, Character.class ),
                    entry( Short.class, Short.class ),
                    entry( short.class, short.class ),
                    entry( Double.class, double.class ),
                    entry( double.class, Double.class ),
                    entry( Float.class, float.class ),
                    entry( float.class, Float.class ),
                    entry( Byte.class, byte.class ),
                    entry( byte.class, Byte.class )
            );

    private static String getterNameFor( Field f ) {
        String name = f.getName();
        char[] nameChars = new char[ 3 + name.length() ];
        "get".getChars( 0, 3, nameChars, 0 );
        name.getChars( 0, name.length(), nameChars, 3 );
        nameChars[ 3 ] = Character.toUpperCase( nameChars[ 3 ] );
        return new String( nameChars );
    }

    private static boolean normalField( Field f ) {
        int modifiers = f.getModifiers();
        return !( Modifier.isStatic( modifiers ) || Modifier.isTransient( modifiers ) || f.isSynthetic() );
    }

}
