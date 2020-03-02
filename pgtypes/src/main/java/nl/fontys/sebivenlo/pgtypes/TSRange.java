/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.pgtypes;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandehombergh@fontysvenlo.org}
 */
public class TSRange extends LocalDateTimeRange implements SQLData {

    public TSRange( LocalDateTime start, Duration length ) {
        super( start, length );
    }

    public TSRange( LocalDateTime start, LocalDateTime end ) {
        super( start, end );
    }

    public TSRange() {
        super( LocalDateTime.now(), LocalDateTime.MAX );
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return "tsrange";
    }

    @Override
    public void readSQL( SQLInput stream, String typeName ) throws SQLException {
        if ( !typeName.equalsIgnoreCase( "tsrange" ) ) {
            throw new SQLException( "do not understand type request " + typeName );
        }
        String r = stream.readString().replaceAll( "[\\[\\)\"]", "" );
        String[] part = r
                .split( "," );
        System.out.println( "part[0] = " + part[ 0 ] );
        System.out.println( "part[1] = " + part[ 1 ] );
        this.start = LocalDateTime.parse( part[ 0 ].replace( " ", "T" ) );
        this.end = LocalDateTime.parse( part[ 1 ].replace( " ", "T" ) );
    }

    @Override
    public void writeSQL( SQLOutput stream ) throws SQLException {
        String r = "[" + start.toString().replace( "T", " " ) + "," + end
                .toString().replace( "T", " " ) + ")";
        stream.writeString( r );
    }

    public static TSRange fromUntil( LocalDateTime start, LocalDateTime end ) {
        return new TSRange( start, end );
    }
}
