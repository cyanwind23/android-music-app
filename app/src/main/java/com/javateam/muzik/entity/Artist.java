package com.javateam.muzik.entity;

import java.util.Date;
import java.util.List;

public class Artist {
    private Long id;
    private String name;
    private Date birthday;
    private String img_url;
    private String description;

    // Relationships
    private List<Song> songs;

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", img_url='" + img_url + '\'' +
                ", description='" + description + '\'' +
                ", songs=" + songs +
                '}';
    }
}
