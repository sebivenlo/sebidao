package nl.fontys.sebivenlo.dao;

import java.lang.reflect.Field;
import static java.lang.reflect.Modifier.*;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.mapping;
import java.util.stream.Stream;

/**
 * To minimize the amount of reflection, keep a copy of the meta data on a the
 * reflectively found type information by caching it.
 *
 * This is the data that is cached, and is typically stored in a
 * {@code Map<Type,EntityData>}.
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
class EntityMetaData<K> {

    /**
     * Contains the name to type map for all (declared) fields. Use a map that 
     * iterates over the keys in insertion order.
     */
    final Map<String, Class<?>> typeMap = new LinkedHashMap<>();
    private Set<String> generatedFields;
    /**
     * What are these meta data about.
     */
    final Class<K> about;
    /**
     * The name of the if field.
     */
    private String idName;
    private boolean idGenerated;

    EntityMetaData( Class<K> about ) {
        this.about = about;
        register( about );
    }

    /**
     * Do the work to register a class.
     *
     * @param clz
     */
    final void register( Class<K> clz ) {
        Field[] declaredFields = clz.getDeclaredFields();

        for ( Field field : declaredFields ) {
            if ( normalField( field ) ) {
                Class<?> type = field.getType();
                String name = field.getName();
                typeMap.put( name, type );
                ID idAnnotation = field.getAnnotation( ID.class );
                if ( idAnnotation != null ) {
                    idName = name;
                    this.idGenerated = idAnnotation.generated();
                }
                Generated gAnnotation = field.getAnnotation( Generated.class );
                if ( gAnnotation != null ) {
                    setGenerated( name );
                }

            }
        }
    }

    String getIdName() {
        return idName;
    }

    private boolean normalField( Field f ) {
        return !f.isSynthetic() && ( ( f.getModifiers() & ( TRANSIENT | STATIC ) ) == 0 );
    }

    /**
     * Get the type of the id field.
     *
     * @return the type.
     */
    Class<?> getIdType() {
        return typeMap.get( getIdName() );
    }

    boolean isIDGenerated() {
        return this.idGenerated;
    }

    boolean isGenerated( String name ) {
        return this.generatedFields != null && this.generatedFields.contains( name );

    }

    private void setGenerated( String name ) {
        if ( null == generatedFields ) {
            generatedFields= new HashSet<>();
        }
        generatedFields.add( name );
    }

}
