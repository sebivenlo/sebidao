/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.dao;

import java.util.Set;
import java.util.function.Function;

/**
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
     * @param entityType for entity
     * @param keyType for key
     */
    public AbstractMapper(  Class<K> keyType,Class<E> entityType ) {
        this.entityType = entityType;
        this.keyType = keyType;
        entityMetaData = new EntityMetaData<>( entityType );
    }

    /**
     * Get entity type.
     * @return the type.
     */
    @Override
    public Class<E> entityType() {
        return this.entityType;
    }

    /**
     * Get the column names.
     * @return the set of names.
     */
    @Override
    public Set<String> persistentFieldNames() {
        return entityMetaData.typeMap.keySet();
    }

    @Override
    public Function<E, K> keyExtractor() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public boolean generateKey() {
        return Mapper.super.generateKey(); 
    }

    @Override
    public String idName() {
        return Mapper.super.idName(); 
    }

    @Override
    public String naturalKeyName() {
        return Mapper.super.naturalKeyName(); 
    }

}
