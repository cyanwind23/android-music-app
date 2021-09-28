package com.javateam.muzik.entity;

import java.util.Date;
import java.util.List;

public class Category {
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
