package gsonclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import static gsonclient.StudentApp.PROP_FILENAME;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import static java.time.LocalDate.of;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.daorestclient.RestDAO;
import static nl.fontys.sebivenlo.daorestclient.RestDAO.gsonBuilder;
import nl.fontys.sebivenlo.daorestclient.RestDAOFactory;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class RetrievalServiceTest {

    static String baseUrl;
    final String endPoint = "/students/";
    RestDAOFactory dfac;

    @BeforeClass
    public static void setupClass() throws IOException {

        Properties props = new Properties();

        try {
            props.load( Files.newInputStream( Paths.get( PROP_FILENAME ) ) );
        } catch ( IOException ex ) {
            Logger.getLogger( StudentApp.class.getName() ).log( Level.SEVERE,
                    null, ex );
        }
        baseUrl = props.getProperty( "serviceurl",
                "http://localhost:8080/postrest-simpledemo/rest/v1.0" );
        shellaAsJosnString = readTestDataFile( "shellaclifton.json" );
        shella = gsonBuilder().create().fromJson( shellaAsJosnString,
                Student.class );

    }

    @Before 
    public void setup(){
        dfac= new RestDAOFactory(baseUrl+endPoint );
    
    }
    static int shellaSnummer = 3640450;
    static String shellaAsJosnString;
    static Student shella;

    @Test
    public void testGet() {
        String loc = baseUrl + endPoint;

        DAO<Integer, Student> dao = dfac.createDao(  Student.class );

        Optional<Student> os = dao.get( shellaSnummer );

        assertTrue( os.isPresent() );
        Student s = os.get();
        String email = s.getEmail();
        assertEquals( "shellaclifton@student.olifantys.nl", email );
        LocalDate dob = s.getDob();
        assertEquals( of( 1996, 12, 13 ), dob );
        assertEquals( "is it shella?", shella, s );

//        Assert.fail( 
//                "testGet ran to end. Review the code and comment or delete  line" );
    }

    @Test
    public void testGetAll() throws IOException {
        String studentJson = String.join( System.lineSeparator(), Files.
                readAllLines( Paths.get( "students.json" ) ) );

        String loc = baseUrl + endPoint;

        DAO<Integer, Student> dao = dfac.createDao(  Student.class );

        Collection<Student> all = dao.getAll();

        Gson gson = gsonBuilder().create();
        Type typeToken = TypeToken.getParameterized( ArrayList.class,
                Student.class ).
                getType();
        Reader sr = new StringReader( studentJson );
        List<Student> testStudents = gson.fromJson( sr, typeToken );
        Student[] testA = testStudents.toArray( new Student[ 0 ] );
        Assertions.assertThat( all ).containsExactlyInAnyOrder( testA );

//        Assert.fail(
//                "testGetAll not yet implemented. Review the code and comment or delete this line" );
    }

    @Test
    public void testSaveDelete() throws IOException {
        String harryJ = readTestDataFile( "potter.json" );
        String loc = baseUrl + endPoint;

        Student harry = gsonBuilder().create().fromJson( harryJ, Student.class );

        DAO<Integer, Student> dao = dfac.createDao(  Student.class );
        Student savedHarry = dao.save( harry );

        // cleanup before assert, to db clean.
        dao.delete( savedHarry );
        assertEquals( "Harry went through the looking glass", harry.email,
                savedHarry.email );

//        Assert.fail( 
//                "testSave not yet implemented. Review the code and comment or delete this line" );
    }

    private static String readTestDataFile( String fileName ) throws IOException {
        return String.join( System.lineSeparator(), Files.
                readAllLines( Paths.get( fileName ) ) );
    }
}
