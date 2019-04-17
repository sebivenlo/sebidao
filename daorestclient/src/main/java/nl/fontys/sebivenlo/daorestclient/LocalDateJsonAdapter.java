package nl.fontys.sebivenlo.daorestclient;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * Some java types are not available in either the json library or produced the wrong kind of (de)serialisation.
 * This is the case for LoacalDate, from which we want ISO8601, YYYY-MM-DD e.g. 2019-04-17.
 * 
 * The approach is to have a converter that translates in two ways.
 * 
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LocalDateJsonAdapter implements JsonSerializer<LocalDate>,
        JsonDeserializer<LocalDate> {

    public LocalDateJsonAdapter() {
    }

    @Override
    public JsonElement serialize( LocalDate src, Type typeOfSrc,
            JsonSerializationContext context ) {
        return new JsonPrimitive( src.toString() );
    }

    @Override
    public LocalDate deserialize( JsonElement json, Type typeOfT,
            JsonDeserializationContext context ) throws JsonParseException {
        return LocalDate.parse( json.getAsString() );
    }

}
