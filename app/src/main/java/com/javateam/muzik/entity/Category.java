package com.javateam.muzik.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Category implements Serializable {
    private Long id;
    private String name;
    private String description;

    // Relationships
    private List<Song> songs;

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", songs=" + songs +
                '}';
    }
}
