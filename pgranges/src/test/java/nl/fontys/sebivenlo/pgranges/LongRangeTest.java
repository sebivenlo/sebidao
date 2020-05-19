/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.pgranges;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LongRangeTest  extends RangeTestBase<Long, Range<Long>>{

    public LongRangeTest( boolean expected, String label ) {
        super( expected, label );
    }

    @Override
    Long a() {
        return 420_000L;
    }

    @Override
    Long b() {
        return 510_000L;
    }

    @Override
    Long c() {
        return 550_000L;
    }

    @Override
    Long d() {
        return 10_230_000L;
    }

    @Override
    Range<Long> createRange( Long start, Long end ) {
        return new LongRange(start,end);
    }

    @Override
    Object measurementUnit() {
        return null; // not required
    }
    
    
}
