package com.javateam.muzik.entity;

import com.google.gson.annotations.SerializedName;
import com.javateam.muzik.R;
import com.javateam.muzik.config.AppConfig;

import java.io.Serializable;
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
