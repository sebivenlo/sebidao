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
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public abstract class AbstractMapper<K,E> implements Mapper<K,E>{

    
    protected final Class<E> entityType;
    protected final Class<K> keyType;
    
    EntityMetaData<E> entityMetaData;

    public AbstractMapper( Class<E> entityType, Class<K> keyType ) {
        this.entityType = entityType;
        this.keyType = keyType;
        entityMetaData = new EntityMetaData<>(entityType);
    }

    @Override
    public Class<E> entityType() {
        return this.entityType;
    }

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
        return Mapper.super.generateKey(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String idName() {
        return Mapper.super.idName(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String naturalKeyName() {
        return Mapper.super.naturalKeyName(); //To change body of generated methods, choose Tools | Templates.
    }

}
