package com.javateam.muzik.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Song {
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

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getAuthorsName() {
        StringBuilder sb = new StringBuilder("");
        if (artists != null) {
            for (Artist artist : artists) {
                sb.append(", " + artist.getName());
            }
        }
        if (sb.length() > 0) {
            return sb.substring(1).toString();
        } else {
            return "";
        }
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
