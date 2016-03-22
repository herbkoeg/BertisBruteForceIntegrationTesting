/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit.db.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.xml.datatype.XMLGregorianCalendar;

public class GsonHelper {

    private static Gson getGson() {
        GsonBuilder gson_builder = new GsonBuilder();
        gson_builder.registerTypeAdapter(XMLGregorianCalendar.class,
                new XMLGregorianCalendarConverter.Serializer());
        gson_builder.registerTypeAdapter(XMLGregorianCalendar.class,
                new XMLGregorianCalendarConverter.Deserializer());
        return gson_builder.create();
    }

    public static String getGsonString(Object object) {
        return getGson().toJson(object);
    }

    public static Object getObjectFromString(String jsonString, Class clazz) {
        return getGson().fromJson(jsonString,clazz);
    }

}
