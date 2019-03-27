/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.dao;

import java.util.Set;

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
    public abstract Set<String> persistentFieldNames();


}
