package gsonclient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Simple table retrieval app.
 * @author Pieter van den Hombergh (879417) {@code pieter.van.den.hombergh@gmail.com}
 */
public class StudentApp extends Application {

    TableView<Student> tableView;
    static final String PROP_FILENAME = "resource.properties";
    @Override
    public void start( Stage primaryStage ) throws IOException {
        Properties props = new Properties();
        
        try {
            props.load(Files.newInputStream(Paths.get(PROP_FILENAME ) ) );
        } catch ( IOException ex ) {
            Logger.getLogger( StudentApp.class.getName() ).log( Level.SEVERE, null, ex );
        }
        String url = props.getProperty("serviceurl", "http://localhost:8080/postrest-simpledemo/rest/v1.0/students");
        System.out.println( "url = " + url ); System.out.println( "using pooling data source" );


        tableView = new TableViewBuilder<>( Student.class )
                .fromUrl( url )
                .withColumns( "snummer", "lastname", "firstname",
                        "email","dob" )
                .withColumnNameMapping( "snummer", "Student ID #" )
                .withColumnNameMapping( "dob", "Birthday" )
                .withColumnNameMapping( "lastname", "Lastname" )
                .withColumnNameMapping( "firstname", "Firstname" )
                .withColumnFractions( 0.3d)
                .build();
        
        StackPane root = new StackPane();
        root.getChildren().add( tableView );

        Scene scene = new Scene( root, 800, 600 );

        primaryStage.setTitle( "Fantys University Student Service" );
        primaryStage.setScene( scene );
        primaryStage.show();
    }

    void columnFraction( TableColumnBase column, double f1 ) {
        column.prefWidthProperty().bind( tableView.widthProperty().
                multiply( f1 ) );
    }

    /**
     * Start .
     *
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        launch( args );
    }

}
