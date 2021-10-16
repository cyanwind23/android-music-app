package com.javateam.muzik.entity;

import com.google.gson.annotations.SerializedName;
import com.javateam.muzik.R;
import com.javateam.muzik.config.AppConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Song implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("img_url")
    private String imgUrl;
    @SerializedName("song_url")
    private String songUrl;
    @SerializedName("frequency")
    private Long frequency;
    @SerializedName("self_link")
    private String selfLink;

    // Relationships
    private Album album;
    private List<Artist> artists;
    private List<Category> categories;
    private List<Playlist> playlists;

    public Song() {
        artists = new ArrayList<>();
        categories = new ArrayList<>();
        playlists = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return AppConfig.SERVER_URL + imgUrl;
    }

    public String getArtistsName() {
        StringBuilder sb = new StringBuilder("");
        if (artists != null) {
            for (Artist artist : artists) {
                if (artist != null) {
                    sb.append(", " + artist.getName());
                }
            }
        }
        if (sb.length() > 0) {
            return sb.substring(1).toString();
        } else {
            return "Nghệ sĩ không xác định";
        }
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getSongUrl() {
        return AppConfig.SERVER_URL + songUrl;
    }
    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", songUrl='" + songUrl + '\'' +
                ", frequency=" + frequency +
                ", selfLink='" + selfLink + '\'' +
                '}';
    }

}
