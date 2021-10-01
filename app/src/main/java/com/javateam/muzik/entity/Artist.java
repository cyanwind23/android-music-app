package com.javateam.muzik.entity;

import com.google.gson.annotations.SerializedName;
import com.javateam.muzik.config.AppConfig;

import java.util.Date;
import java.util.List;


public class Artist {
    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("birthday")
    private Date birthday;
    @SerializedName("img_url")
    private String imgUrl;
    @SerializedName("description")
    private String description;
    @SerializedName("self_link")
    private String selfLink;
    // Relationships
    private List<Song> songs;

    public void setId(Long id) {
        this.id = id;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return AppConfig.SERVER_URL + imgUrl;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", imgUrl='" + imgUrl + '\'' +
                ", description='" + description + '\'' +
                ", selfLink='" + selfLink + '\'' +
                '}';
    }
}
