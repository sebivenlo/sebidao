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
import java.sql.SQLType;
import java.time.Duration;
import java.time.LocalDateTime;
import org.postgresql.util.PGobject;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandehombergh@fontysvenlo.org}
 */
public class TSRange extends LocalDateTimeRange implements SQLData, SQLType {

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
        String[] part = stringToParts( stream.readString() );
        this.start = LocalDateTime.parse( part[ 0 ] );
        this.end = LocalDateTime.parse( part[ 1 ] );
    }

    private static String[] stringToParts( String r ) {
        String r2 = r.replaceAll( "[\\[\\)\"]", "" );
        String[] part = r2.split( "," );
        part[ 0 ] = part[ 0 ].replace( " ", "T" );
        part[ 1 ] = part[ 1 ].replace( " ", "T" );
        return part;
    }

    private static LocalDateTime[] stringToBeginEnd( String r ) {
        LocalDateTime[] result = new LocalDateTime[ 2 ];
        String[] p = stringToParts( r );
        result[ 0 ] = LocalDateTime.parse( p[ 0 ] );
        result[ 1 ] = LocalDateTime.parse( p[ 1 ] );
        return result;
    }

    private TSRange( LocalDateTime[] be ) {
        this( be[ 0 ], be[ 1 ] );
    }

    public TSRange( PGobject pgo ) {
        this( stringToBeginEnd( pgo.getValue() ) );
    }

//    public static TSRange fromString( String in ) {
//        String[] p = stringToParts( in );
//        return new TSRange( LocalDateTime.parse( p[ 0 ] ), LocalDateTime
//                            .parse( p[ 1 ] ) );
//    }
//
//    public static TSRange fromPGobject( PGobject in ) {
//        return fromString( in.getValue() );
//    }


    @Override
    public void writeSQL( SQLOutput stream ) throws SQLException {
        String r = "[" + start.toString().replace( "T", " " ) + "," + end
                .toString().replace( "T", " " ) + ")";
        stream.writeString( r );
    }

    public static TSRange fromUntil( LocalDateTime start, LocalDateTime end ) {
        return new TSRange( start, end );
    }

    @Override
    public String getName() {
        return "tsrange";
//        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getVendor() {
        return "postgresql";
//        throw new UnsupportedOperationException( "Not supported yet." ); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getVendorTypeNumber() {
        return Integer.valueOf( 1111 );
    }
}
