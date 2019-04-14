package gsonclient;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import nl.fontys.sebivenlo.dao.Entity2;
import nl.fontys.sebivenlo.daorestclient.RestDAOFactory;

/**
 *
 * @author Johan Vos (Gluon)
 * @author Pieter van den Hombergh, adapted to gson.
 * @param <E> type to retrieve.
 */
public class RetrievalService<K extends Serializable, E extends Entity2<K>>
        extends Service<ObservableList<E>> {

    private final String baseUrl;
    private final Class<E> type;
    private final RestDAOFactory dfac;

    public RetrievalService( String url, Class<E> type ) {
        this.baseUrl = url;
        this.type = type;
        dfac= new RestDAOFactory(baseUrl );
    }

    @Override
    protected Task<ObservableList<E>> createTask() {
        return new Task<ObservableList<E>>() {

            @Override
            protected ObservableList<E> call() throws Exception {
                return getAll();
            }

        };
    }

    ObservableList<E> getAll() throws JsonSyntaxException, JsonIOException,
            MalformedURLException, IOException {
        Collection<E> all = dfac.createDao( type  ).getAll();
        return FXCollections.<E>observableArrayList( (List) all );
    }

    E save( E entity ) throws MalformedURLException, IOException {
        return dfac.createDao( type  ).save( entity );
    }

    E update( E entity ) {
        return dfac.createDao( type  ).update( entity );

    }

    void delete( E entity ) {
        dfac.createDao( type  ).delete( entity );
    }
}
