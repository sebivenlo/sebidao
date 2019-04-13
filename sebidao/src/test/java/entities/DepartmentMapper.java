/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import nl.fontys.sebivenlo.dao.Mapper;
import nl.fontys.sebivenlo.dao.SimpleEntity;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class DepartmentMapper implements Mapper<Integer, Department> {

    @Override
    public Class<Department> entityType() {
        return Department.class;
    }

    @Override
    public Object[] explode( Department e ) {
        return e.asParts();
    }

    @Override
    public Department implode( Object[] parts ) {
        return new Department( parts );
    }

    @Override
    public Function<Department, Integer> keyExtractor() {
        return Department::getDepartmentid;
    }
    static final Set<String> FIELD_NAMES
            = new LinkedHashSet<>( Arrays.asList(
                    "departmentid",
                    "name",
                    "description",
                    "email"
            ) );

    @Override
    public Set<String> persistentFieldNames() {
        return FIELD_NAMES;
    }

}
