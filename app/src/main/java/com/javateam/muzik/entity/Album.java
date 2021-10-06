package com.javateam.muzik.entity;

import com.google.gson.annotations.SerializedName;
import com.javateam.muzik.config.AppConfig;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {
    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("img_url")
    private String imgUrl;
    @SerializedName("description")
    private String description;
    @SerializedName("self_link")
    private String selfLink;

    private List<Song> songs;

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getImgUrl() {
        return AppConfig.SERVER_URL + imgUrl;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", description='" + description + '\'' +
                ", selfLink='" + selfLink + '\'' +
                '}';
    }
}
