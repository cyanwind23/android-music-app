package com.javateam.muzik.service;

import com.google.gson.Gson;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SongService {
    public static List<Song> parseWithArtists(String jsonString, List<Artist> artists) throws JSONException {
        // Init
        Gson gson = new Gson();
        List<Song> list = new ArrayList<>();

        // Convert Artists List to Map
        Map<Long, Artist> mapArtist = artists.stream()
                                            .collect(Collectors.toMap(Artist::getId, Function.identity()));

        // Convert JSON API to List
        JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObjectTmp = (JSONObject) jsonArray.get(i);

            // Fetch all attributes and mapping to Song object
            JSONObject jsonObject = jsonObjectTmp.getJSONObject("attributes");
            jsonObject.put("id", Long.parseLong(jsonObjectTmp.getString("id")));
            jsonObject.put("self_link", jsonObjectTmp.getJSONObject("links").getString("self"));
            Song song = (Song) gson.fromJson(jsonObject.toString(), Song.class);

            // Fetch artists for song
            List<Artist> listArtist = new ArrayList<>();
            JSONArray jsonArtists = jsonObjectTmp.getJSONObject("relationships").getJSONObject("artists").getJSONArray("data");
            for (int j = 0; j < jsonArtists.length(); j++) {
                JSONObject artistData = (JSONObject) jsonArtists.get(j);
                listArtist.add(mapArtist.get(Long.parseLong(artistData.getString("id"))));
            }
            song.setArtists(listArtist);
            list.add(song);
        }
        return list;
    }
}
