package gsonclient;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import nl.fontys.sebivenlo.dao.Entity2;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <E> type to be shown
 */
public class TableViewBuilder<K extends Serializable, E extends Entity2<K>> {

    private final Class<E> rowClass;
    private String[] columnsToInclude = new String[] {};
    private double[] columnFractions = new double[] {};
    private final Map<String, String> columnNameMap = new HashMap<>();
    private String url;

    public TableViewBuilder( Class<E> rowClass ) {
        this.rowClass = rowClass;
    }

    public TableViewBuilder withColumns( String... columnsToInclude ) {
        this.columnsToInclude = columnsToInclude;
        return this;
    }

    public TableViewBuilder withColumnFractions( double... columnFractions ) {
        this.columnFractions = columnFractions;
        return this;
    }
    public TableViewBuilder fromUrl( String url ) {
        this.url = url;
        return this;
    }

    public TableViewBuilder withColumnNameMapping( String... names ) {
        if ( names.length % 2 != 0 ) {
            throw new IllegalArgumentException( "Need even number of names" );
        }
        for ( int i = 0; i < names.length; i += 2 ) {
            columnNameMap.put( names[ i ], names[ i + 1 ] );
        }
        return this;
    }

    private static void columnFraction( Region view, TableColumnBase column,
            double f1 ) {
        column.prefWidthProperty().bind( view.widthProperty().multiply( f1 ) );
    }

    public TableView<E> build() throws IOException {
        TableView<E> tableView = new TableView<>();
        final List<String> columnNameList = Arrays.asList( columnsToInclude );
        List<TableColumn<E, Object>> columns = new ArrayList<>();
        System.out.println( "rowClass = " + rowClass );
        Field[] declaredFields = rowClass.getDeclaredFields();
        List<String> declaredFieldNames = Arrays.stream( declaredFields )
                .map( Field::getName ).collect( toList() );
        List<Field> fieldsToShow
                = columnNameList.stream()
                        .filter( f -> declaredFieldNames.contains( f ) )
                        .map( this::getFieldByName )
                        .collect( Collectors.toList() );
        int colNr = 0;
        double fractionRemaining = 1.0d;
        for ( Field field : fieldsToShow ) {
            String fName = field.getName();
            String header = columnNameMap.get( fName );
            if ( null == header ) {
                header = fName;
            }
            TableColumn<E, Object> col = new TableColumn<>( header );
            col.setCellValueFactory( new PropertyValueFactory<>( fName ) );
            if ( field.getType().isPrimitive() ) {
                col.setStyle( "-fx-alignment: CENTER-RIGHT" );
            }

            double fraction = 0;
            if ( columnFractions.length > colNr ) {
                fraction = columnFractions[ colNr ];
                fractionRemaining -= fraction;
                colNr++;
            } else {
                fraction = fractionRemaining / ( fieldsToShow.size() - colNr );
            }
            columnFraction( tableView, col, fraction );
            columns.add( col );
        }
        tableView.getColumns().setAll( columns );
        tableView.setItems( getObservableList( rowClass ) );
        return tableView;
    }

    private Field getFieldByName( String n ) {
        try {
            return rowClass.getDeclaredField( n );
        } catch ( NoSuchFieldException nfe ) {
            throw new RuntimeException( nfe );
        }
    }

    ObservableList<E> getObservableList( Class<E> type ) throws IOException {
        Service<ObservableList<E>> service = new RetrievalService<>( url, type );//, creator );

        ObservableList<E> answer = FXCollections.observableArrayList();
        service.stateProperty().addListener( ( Observable observable ) -> {
            Worker.State state = service.getState();
            switch (state) {
                case SUCCEEDED:
                    answer.addAll( service.getValue() );
                    System.out.println( "at state " + state );
                    break;
                case FAILED:
                    System.err.println( "service failed" );
                    break;
                default:
                    System.out.println( "at state " + state );
                    break;
            }

        } );
        service.start();
        return answer;
    }

}
