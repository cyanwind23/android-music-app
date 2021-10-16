package com.javateam.muzik.service;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Category;
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

    public static List<Song> parseFull(String jsonString, List<Artist> artists,
                                       List<Album> albums, List<Category> categories) throws JSONException {
        if (jsonString == null) {
            return null;
        }

        // Init
        Gson gson = new Gson();
        List<Song> list = new ArrayList<>();

        // Convert Artists List to Map
        // For Artist
        Map<Long, Artist> mapArtist = artists.stream()
                .collect(Collectors.toMap(Artist::getId, Function.identity()));
        // For Album
        Map<Long, Album> mapAlbum = albums.stream()
                .collect(Collectors.toMap(Album::getId, Function.identity()));
        // For Category
        Map<Long, Category> mapCategory = categories.stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        // Convert JSON API to List
        JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonSong = (JSONObject) jsonArray.get(i);

            // Fetch all attributes and mapping to Song object
            JSONObject jsonObject = jsonSong.getJSONObject("attributes");
            jsonObject.put("id", Long.parseLong(jsonSong.getString("id")));
            jsonObject.put("self_link", jsonSong.getJSONObject("links").getString("self"));
            Song song = gson.fromJson(jsonObject.toString(), Song.class);

            JSONObject jsonRelationship = jsonSong.getJSONObject("relationships");

            JSONObject jsonAlbum = jsonRelationship.getJSONObject("album");
            if (jsonAlbum.isNull("data")) {
                jsonAlbum = null;
            } else {
                jsonAlbum = jsonAlbum.getJSONObject("data");
            }
            Album album;
            if (jsonAlbum != null) {
                album = mapAlbum.get(Long.parseLong(jsonAlbum.getString("id")));
                addSongToList(album.getSongs(), song);
                song.setAlbum(album);
            }

            JSONArray jsonArtists = jsonRelationship.getJSONObject("artists").getJSONArray("data");
            JSONArray jsonCategories = jsonRelationship.getJSONObject("categories").getJSONArray("data");
            connectArtist(song, mapArtist, jsonArtists);
            connectCategory(song, mapCategory, jsonCategories);

            list.add(song);
        }
        return list;
    }

    private static void connectArtist(Song song, Map<Long, Artist> map, @NonNull JSONArray jsonArtists) {
        List<Artist> listArtist = new ArrayList<>();
        for (int i = 0; i < jsonArtists.length(); i++) {
            Artist artist;
            try {
                JSONObject jsonArtist = (JSONObject) jsonArtists.get(i);
                artist = map.get(Long.parseLong(jsonArtist.getString("id")));
                listArtist.add(artist);
                if (artist != null) {
                    addSongToList(artist.getSongs(), song);
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        song.setArtists(listArtist);
    }

    private static void connectCategory(Song song, Map<Long, Category> map, @NonNull JSONArray jsonCategories) {
        List<Category> listCategory = new ArrayList<>();
        for (int i = 0; i < jsonCategories.length(); i++) {
            Category category;
            try {
                JSONObject jsonCategory = (JSONObject) jsonCategories.get(i);
                category = map.get(Long.parseLong(jsonCategory.getString("id")));
                listCategory.add(category);
                if (category != null) {
                    addSongToList(category.getSongs(), song);
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        song.setCategories(listCategory);
    }
    private static void addSongToList(List<Song> listSong, Song song) {
        if (listSong == null) {
            listSong = new ArrayList<>();
        }
        listSong.add(song);
    }
}
