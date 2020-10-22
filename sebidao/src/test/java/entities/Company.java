package entities;

import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.dao.ID;
import nl.fontys.sebivenlo.dao.TableName;

/**
 * Entity with non numeric primary key.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
@TableName( "companies" )
public class Company implements Entity2<String> {

    private String name;
    private String country;
    private String city;
    private String address;
    @ID( generated = false )
    private String ticker;
    private String postcode;
    private Integer someInt;
    private Integer someInteger;

    public Company( String name, String country, String city,
            String address, String ticker, String postcode ) {
        this( name, country, city, address, ticker, postcode, 0, null );
    }

    public Company( String name, String country, String city,
            String address, String ticker, String postcode, Integer i, Integer j ) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.ticker = ticker;
        this.postcode = postcode;
        this.someInt = i;
        this.someInteger = j;
    }

    @Override
    public String toString() {
        return "Company{" + "ticker=" + ticker + ", name=" + name + ", country="
                + country + ", city=" + city + ", address=" + address
                + ", postcode=" + postcode + ",somint =" + someInt + ", somInteger=" + someInteger + '}';
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

        return new Company( (String) parts[ 0 ], (String) parts[ 1 ], (String) parts[ 2 ], (String) parts[ 3 ], (String) parts[ 4 ],
                (String) parts[ 5 ], (Integer) parts[ 6 ], (Integer) parts[ 7 ] );
    }

    Object[] asParts() {

        return new Object[] {
            name,
            country,
            city,
            address,
            ticker,
            postcode, someInt, someInteger
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

    public int getSomeInt() {
        return someInt;
    }

    public void setSomeInt( int someInt ) {
        this.someInt = someInt;
    }

    public Integer getSomeInteger() {
        return someInteger;
    }

    public void setSomeInteger( Integer someInteger ) {
        this.someInteger = someInteger;
    }

}
