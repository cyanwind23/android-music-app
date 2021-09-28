package com.javateam.muzik.entity;

import java.util.List;

public class Song {
    private Long id;
    private String name;
    private String img_url;
    private String song_url;
    private Long frequency;

    // Relationships
    private Album album;
    private List<Artist> artists;
    private List<Category> categories;
    private List<Playlist> playlists;

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img_url='" + img_url + '\'' +
                ", song_url='" + song_url + '\'' +
                ", frequency=" + frequency +
                ", album=" + album +
                ", artists=" + artists +
                ", categories=" + categories +
                ", playlists=" + playlists +
                '}';
    }
}
