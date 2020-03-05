/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.pgtypes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class IntegerRangeTest extends RangeTestBase<Integer, Range<Integer>> {

    public IntegerRangeTest( boolean expected, String label ) {
        super( expected, label );
    }

    @Override
    Integer a() {
        return 42;
    }

    @Override
    Integer b() {
        return 51;
    }

    @Override
    Integer c() {
        return 55;
    }

    @Override
    Integer d() {
        return 1023;
    }

    @Override
    Range<Integer> createRange( Integer start, Integer end ) {
        return new IntegerRange( start, end );
    }

    @Override
    Object measurementUnit() {
        return null;
    }

}
