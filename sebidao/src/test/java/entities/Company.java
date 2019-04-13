package entities;

import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.ID;

/**
 * Entity with non numeric primary key.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class Company implements Entity2<String> {

    private String name;
    private String country;
    private String city;
    private String address;
    @ID( generated = false )
    private String ticker;
    private String postcode;

    public Company( String name, String country, String city,
            String address, String ticker,String postcode ) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.ticker = ticker;
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return "Company{" + "ticker=" + ticker + ", name=" + name + ", country="
                + country + ", city=" + city + ", address=" + address
                + ", postcode=" + postcode + '}';
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker( String ticker ) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry( String country ) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode( String postcode ) {
        this.postcode = postcode;
    }

    static Company fromParts( Object[] parts ) {
        String[] str = new String[ parts.length ];
        int i = 0;
        for ( Object part : parts ) {
            str[ i++ ] = String.class.cast( part );
        }

        return new Company( str[ 0 ], str[ 1 ], str[ 2 ], str[ 3 ], str[ 4 ],
                str[ 5 ] );
    }

    Object[] asParts() {

        return new Object[] {
            name,
            country,
            city,
            address,
            ticker,
            postcode
        };
    }

    @Override
    public String getNaturalId() {
        return ticker;
    }

    @Override
    public int getId() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
    
    
}
