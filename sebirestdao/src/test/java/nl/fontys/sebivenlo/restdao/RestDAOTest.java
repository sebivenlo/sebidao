package nl.fontys.sebivenlo.restdao;

import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import static java.time.LocalDate.of;
import static java.time.Month.JULY;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAOException;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import static org.assertj.core.api.Assertions.*;
import org.junit.Assert;
/**
 * Test the rest implementation of DAO.
 * This test mocks the server using wire-mock as far as possible, to avoid having to set up a rest server just for the purpose of 
 * testing this implementation.
 * 
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class RestDAOTest {

    @ClassRule
    public static WireMockClassRule wireMockRule
            = new WireMockClassRule( 18080 );

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;

    
    
    static class Hi {

        String hi;

        @Override
        public String toString() {
            return hi;
        }

    }
    static int shellaSnummer = 3640450;
    static String shellaAsJosnString;
    static Student shella;

    @BeforeClass
    public static void setupClass() throws IOException {
        shellaAsJosnString = String.join( System.lineSeparator(), Files.
                readAllLines( Paths.get( "shellaclifton.json" ) ) );
        shella = RestDAO.gsonBuilder.create().fromJson( shellaAsJosnString, Student.class );
    }

    /**
     * Honing my skills with wiremock.
     * @throws MalformedURLException well look at the name
     * @throws IOException another way of HttpURLConnection to express its discomfort with something.
     */
    @Test
    public void testPing() throws MalformedURLException, IOException {
        String hello = "{\"hi\":\"Hello world!\"}";
        stubFor( get( urlEqualTo( "/some/thing" ) )
                .willReturn( aResponse()
                        .withHeader( "Content-Type", "application/json" )
                        .withBody( hello ) ) );

        URL url = new URL( wireMockRule.baseUrl() + "/some/thing" );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int responseCode = con.getResponseCode();
        System.out.println( "responseCode = " + responseCode );
        String r = readconnection( con );

        System.out.println( "content = " + r );
        assertEquals( "ping ", hello, r );
        Gson g = new Gson();
        Hi hi = g.fromJson( r, RestDAOTest.Hi.class );

        System.out.println( "hi = " + hi );
        assertEquals( "Hello world!", hi.toString() );
//        Assert.fail( "test method testPing reached its end, you can remove this line when you aggree." );
    }

    /**
     * Can I read a json object from some url using wire mock?
     * @throws MalformedURLException see ping
     * @throws IOException see ping
     * @throws JSONException an this is JSONAssert. That could have been dealt with an AssertError.
     */
    @Test
    public void testPing2() throws MalformedURLException, IOException, JSONException {
        stubFor( get( urlEqualTo( "/some/thing" ) )
                .willReturn( aResponse()
                        .withHeader( "Content-Type", "application/json" )
                        .withBody( shellaAsJosnString ) ) );

        URL url = new URL( wireMockRule.baseUrl() + "/some/thing" );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int responseCode = con.getResponseCode();
        System.out.println( "responseCode = " + responseCode );
        String r = readconnection( con );

        System.out.println( "content = " + r );
        Gson g = RestDAO.gsonBuilder.create();
        Student hi = g.fromJson( r, Student.class );

        System.out.println( "hi = " + hi );
        JSONAssert.assertEquals( shellaAsJosnString, r, JSONCompareMode.STRICT );
    }

    private String readconnection( HttpURLConnection con ) throws IOException {
        BufferedReader in = readerFor( con );
        StringBuilder r = new StringBuilder();
        String inputLine;
        while ( ( inputLine = in.readLine() ) != null ) {
            r.append( inputLine );
        }
        return r.toString();
    }

    private BufferedReader readerFor( HttpURLConnection con ) throws IOException {
        InputStream openStream = con.getInputStream();
        BufferedReader in = new BufferedReader( new InputStreamReader(
                openStream ) );
        return in;
    }

    @Test
    public void testGet() {

        Integer snummer = shellaSnummer;
        String baseUrl = wireMockRule.baseUrl();
        String endPoint = "/students/";
        stubFor( 
                get( WireMock.urlEqualTo("/students/"+shellaSnummer ) )
                        .willReturn( aResponse()
                                .withHeader( "Content-Type", "application/json" ).
                                withBody( shellaAsJosnString ) ) );

        String loc = baseUrl + endPoint;
        System.out.println( "loc = " + loc);

        RestDAO<Integer, Student> dao = new RestDAO<>( loc, Student.class );

        Optional<Student> os = dao.get( snummer );

        assertTrue( os.isPresent() );
        Student s = os.get();
        String email = s.getEmail();
        assertEquals( "shellaclifton@student.olifantys.nl", email );
        LocalDate dob = s.getDob();
        assertEquals( of( 1996, 12, 13 ), dob );
        assertEquals("is it shella?",shella,s);
    }

    
    /**
     * Test get all using as test data the students.json file.
     */
    @Test
    public void testGetAll() throws IOException {
        String studentJson = String.join( System.lineSeparator(), Files.
                readAllLines( Paths.get( "students.json" ) ) );
        
        // get the json as list of students, for later comparison
        Gson gson = RestDAO.gsonBuilder.create();
         Type typeToken = TypeToken.getParameterized( ArrayList.class, Student.class ).
                    getType();
        Reader sr = new StringReader(studentJson);
        List<Student> testStudents = gson.fromJson( sr, typeToken );
        Student[] testA = testStudents.toArray( new Student[0] );
        
        
        String endPoint = "/students/";
        stubFor( 
                get( WireMock.urlEqualTo(endPoint ) )
                        .willReturn( aResponse()
                                .withHeader( "Content-Type", "application/json" ).
                                withBody( studentJson ) ) );
        String baseUrl = wireMockRule.baseUrl();

        String loc = baseUrl + endPoint;
        System.out.println( "loc = " + loc);
        
        RestDAO<Integer, Student> dao = new RestDAO<>( loc, Student.class );
        
        
        Collection<Student> all = dao.getAll();
        
        System.out.println( "all = " + all );
        
        Assertions.assertThat( all).containsExactlyInAnyOrder( testA);
        List<ServeEvent> allServeEvents = WireMock.getAllServeEvents();
        
//        System.out.println( "allServeEvents = "  );
//        allServeEvents.forEach( System.out::println);
//        Assert.fail( "test method testGetAll reached its end, you can remove this line when you aggree." );
    }
    @Test
    public void testPost() {
        
        Assert.fail( "test method testPost reached its end, you can remove this line when you aggree." );
    }
}
