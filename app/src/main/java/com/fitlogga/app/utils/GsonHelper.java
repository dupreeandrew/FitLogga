package com.fitlogga.app.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GsonHelper {
    public static Map<String, Object> getMapFromJson(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }

    public static Map<String, List<Map<String, Object>>> getMapOfListsOfMaps(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<Map<String, Object>>>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }
}
