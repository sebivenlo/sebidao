package nl.fontys.sebivenlo.restdao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sebivenlo.dao.DAO;
import nl.fontys.sebivenlo.dao.DAOException;
import nl.fontys.sebivenlo.dao.Entity2;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 * @param <K> key type
 * @param <E> entity
 */
public class RestDAO<K extends Serializable, E extends Entity2<K>> implements
        DAO<K, E> {

    private final String baseUrl;
    private final Class<E> type;

    public RestDAO( String baseUrl, Class<E> type ) {
        this.baseUrl = baseUrl.endsWith( "/" ) ? baseUrl : baseUrl + '/';
        this.type = type;

    }
    private final String USER_AGENT = "Mozilla/5.0";
    static final GsonBuilder gsonBuilder
            = new GsonBuilder().registerTypeAdapter( LocalDate.class, new LocalDateJsonAdapter() );

    @Override
    public Optional<E> get( K key ) {
        Gson gson = gsonBuilder.create();
        String eLoc = baseUrl + key;
        try {

            HttpURLConnection con = get( eLoc );

            int responseCode = con.getResponseCode();
            return readEntity( con, gson );
        } catch ( IOException ex ) {
            Logger.getLogger( RestDAO.class.getName() ).log( Level.SEVERE, null,
                    ex );
            throw new DAOException( ex.getMessage(), ex );
        }
    }

    private HttpURLConnection get( String eLoc ) throws ProtocolException, MalformedURLException, IOException {
        URL url = new URL( eLoc );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod( "GET" );
        return con;
    }

    private Optional<E> readEntity( HttpURLConnection con, Gson gson ) throws JsonSyntaxException, JsonIOException, IOException {
        Optional<E> result = Optional.empty();
        try (
                InputStream inputStream = con.getInputStream();
                Reader reader = new InputStreamReader( inputStream ); ) {
            E entity = gson.fromJson( reader, type );
            if ( entity != null ) {
                result = Optional.of( entity );
            }
        }
        return result;
    }

    @Override
    public Collection<E> getAll() {
        try {
//            System.out.println( "QRS start for url '" + baseUrl + "'" );
            URL url = new URL( baseUrl );

            Type typeToken = TypeToken.getParameterized( ArrayList.class, type ).
                    getType();
            Reader reader = new InputStreamReader( url.openConnection().
                    getInputStream(), StandardCharsets.UTF_8 );
            Gson gson = gsonBuilder.create();
            List<E> p = gson.fromJson( reader, typeToken );
            return p;
        } catch ( MalformedURLException ex ) {
            Logger.getLogger( RestDAO.class.getName() ).log( Level.SEVERE, null, ex );
        } catch ( IOException ex ) {
            Logger.getLogger( RestDAO.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return Collections.EMPTY_LIST;

    }

    private HttpURLConnection post( String eLoc, int bodySize ) throws ProtocolException, MalformedURLException, IOException {
        URL url = new URL( eLoc );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod( "POST" );
        con.setInstanceFollowRedirects( false );
        con.setRequestProperty( "Content-Type", "application/json" );
        con.setRequestProperty( "charset", "utf-8" );
        con.setUseCaches( false );
        con.setDoOutput( true );
        con.setRequestProperty( "Accept", "application/json" );

        con.setRequestProperty( "Content-Length", "" + bodySize );

        return con;
    }

    private HttpURLConnection delete( String eLoc, int bodySize  ) throws ProtocolException, MalformedURLException, IOException {
        URL url = new URL( eLoc );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod( "DELETE" );
        con.setInstanceFollowRedirects( false );
        con.setRequestProperty( "Content-Type", "application/json" );
        con.setRequestProperty( "charset", "utf-8" );
        con.setUseCaches( false );
        con.setDoOutput( true );
        con.setRequestProperty( "Accept", "application/json" );
        con.setRequestProperty( "Content-Length", "" + bodySize );

        return con;
    }

    @Override
    public E save( E e ) {
        E result = null;
        try {
            Gson gson = gsonBuilder.create();
            String toJson = gson.toJson( e, type );
            int length = toJson.length();
            HttpURLConnection con = post( baseUrl, length );

            try ( OutputStream out = con.getOutputStream();
                    DataOutputStream wr = new DataOutputStream( out ); ) {
                wr.write( toJson.getBytes() );
                wr.flush();
                wr.close();
            }
            try ( BufferedReader br = new BufferedReader(
                    new InputStreamReader( con.getInputStream(), "utf-8" ) ) ) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ( ( responseLine = br.readLine() ) != null ) {
                    response.append( responseLine.trim() );
                }
                System.out.println( "response=" + response.toString() );
                result = gson.fromJson( response.toString(), type );
            }
        } catch ( MalformedURLException ex ) {
            Logger.getLogger( RestDAO.class.getName() ).log( Level.SEVERE, null, ex );
        } catch ( IOException ex ) {
            Logger.getLogger( RestDAO.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return result;
    }

    @Override
    public E update( E e ) {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void delete( E e ) {
        try {
            Gson gson = gsonBuilder.create();
            String toJson = gson.toJson( e, type );
            int length = toJson.length();
            HttpURLConnection con = delete( baseUrl, length );

            try ( OutputStream out = con.getOutputStream();
                    DataOutputStream wr = new DataOutputStream( out ); ) {
                wr.write( toJson.getBytes() );
                wr.flush();
                wr.close();
            }
            int responseCode = con.getResponseCode();
            if ( responseCode != 200 ) {
                throw new DAOException( "delete failed for entity " + e );
            }
        } catch ( MalformedURLException ex ) {
            Logger.getLogger( RestDAO.class.getName() ).log( Level.SEVERE, null, ex );
        } catch ( IOException ex ) {
            Logger.getLogger( RestDAO.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

}
