package com.javateam.muzik.entity;

import java.io.Serializable;
import java.util.List;

public class Playlist implements Serializable {
    private Long id;
    private String name;
    private String img_url;
    private String description;

    // Relationships
    private List<Song> songs;

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img_url='" + img_url + '\'' +
                ", description='" + description + '\'' +
                ", songs=" + songs +
                '}';
    }
}
