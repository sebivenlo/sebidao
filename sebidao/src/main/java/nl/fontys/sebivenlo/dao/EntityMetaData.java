package nl.fontys.sebivenlo.dao;

import java.lang.reflect.Field;
import static java.lang.reflect.Modifier.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * To minimize the amount of reflection, keep a copy of the meta data on a the
 * reflectively found type information by caching it.
 *
 * This is the data that is cached, and is typically stored in a
 * {@code Map<Type,EntityData>}. This class is not Thread Safe.
 *
 * @param <E> entity type.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
class EntityMetaData<E> {

    /**
     * Contains the name to type map for all (declared) fields. Use a map that
     * iterates over the keys in insertion order.
     */
    final Map<String, Class<?>> typeMap = new LinkedHashMap<>();
    private Set<String> generatedFieldNames;
    /**
     * What are these meta data about.
     */
    final Class<E> about;
    /**
     * The name of the if field.
     */
    private String idName;
    private boolean idGenerated;
    private List<Field> fields = new ArrayList<>();
    private List<Field> publicFields = Collections.unmodifiableList( fields );
    EntityMetaData( Class<E> about ) {
        this.about = about;
        register( about );
    }

    /**
     * Do the work to register a class.
     *
     * @param clz of entity
     */
    final void register( Class<E> clz ) {
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
                fields.add( field );
            }
        }
    }

    /**
     * Get the declared fields as a list.
     * @return the list, unmodifiable
     */
    List<Field> getFields(){
        return publicFields;
    }
    /**
     * Get a field by position.
     * @param i the field position (order) in the entity.
     * @return the field.
     */
    public Field getField(int i){
        return fields.get( i );
    }

    /**
     * Get a field type by position.
     * @param i the field position (order) in the entity.
     * @return the field.
     */
    public Class<?> getFieldType(int i){
        return fields.get( i ).getType();
    }
    
    String getIdName() {
        return idName;
    }

    private boolean normalField( Field f ) {
        return !f.isSynthetic()
                && ( f.getModifiers() & ( TRANSIENT | STATIC ) ) == 0;
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
        return this.generatedFieldNames != null && this.generatedFieldNames.contains(
                name );

    }

    private void setGenerated( String name ) {
        if ( null == generatedFieldNames ) {
            generatedFieldNames = new HashSet<>();
        }
        generatedFieldNames.add( name );
    }

    @Override
    public String toString() {
        return "EntityMetaData{" + "typeMap=" + typeMap + ", \ngeneratedFields="
                + generatedFieldNames + "\n, about=" + about + ",\n idName="
                + idName + ", \nidGenerated=" + idGenerated + '}';
    }

    
    
}
