/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
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
//        System.out.println( "json = " + json);
//        JsonPrimitive asJsonPrimitive = json.getAsJsonPrimitive();
//        System.out.println( "toString = " + asJsonPrimitive );
        return LocalDate.parse( json.getAsString() );
        //throw new UnsupportedOperationException( "Not supported yet." );
    }

}
