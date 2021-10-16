package com.javateam.muzik.adapter;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {

    public static <T> List<T> parse(String jsonString, Class<T> type) throws JSONException {
        if (jsonString == null) {
            return null;
        }
        Gson gson = new Gson();
        JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");

        List<T> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObjectTmp = (JSONObject) jsonArray.get(i);
            JSONObject jsonObject = jsonObjectTmp.getJSONObject("attributes");
            jsonObject.put("id", Long.parseLong(jsonObjectTmp.getString("id")));
            jsonObject.put("self_link", jsonObjectTmp.getJSONObject("links").getString("self"));
            T object = (T) gson.fromJson(jsonObject.toString(), type);
            list.add(object);
        }
        return list;
    }

    public static <V> Map<Long, V> parseAsMap(String jsonString, Class<V> type) throws JSONException {
        Gson gson = new Gson();
        JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");

        Map<Long, V> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObjectTmp = (JSONObject) jsonArray.get(i);
            JSONObject jsonObject = jsonObjectTmp.getJSONObject("attributes");
            Long objId = Long.parseLong(jsonObjectTmp.getString("id"));
            jsonObject.put("id", objId);
            jsonObject.put("self_link", jsonObjectTmp.getJSONObject("links").getString("self"));
            V object = (V) gson.fromJson(jsonObject.toString(), type);
            map.put(objId, object);
        }
        return map;
    }
}
