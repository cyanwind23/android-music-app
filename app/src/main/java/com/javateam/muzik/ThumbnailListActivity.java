package com.javateam.muzik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.javateam.muzik.adapter.SongListAdapter;
import com.javateam.muzik.adapter.TLViewPagerAdapter;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Song;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.List;

public class ThumbnailListActivity extends AppCompatActivity {

    private Album album;
    private Artist artist;
    private List<Song> listSong;

    private RecyclerView recyclerViewSong;
    private SongListAdapter songListAdapter;

    private ViewPager2 viewPager2;
    private TLViewPagerAdapter viewPagerAdapter;
    private DotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail_list);

        prepareData();
    }

    private void prepareData() {
        Intent intent = getIntent();

        viewPager2 = findViewById(R.id.tl_viewpager);
        viewPagerAdapter = new TLViewPagerAdapter(this, intent);
        viewPager2.setAdapter(viewPagerAdapter);
        dotsIndicator = findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager2(viewPager2);


        String type = intent.getStringExtra("type");
        if (type.equals("album")) {
            album = (Album) intent.getSerializableExtra("album");
            listSong = album.getSongs();
        } else {
            artist = (Artist) intent.getSerializableExtra("artist");
            listSong = artist.getSongs();
        }
        prepareSong();
    }

    private void prepareSong() {
        // For Song RCV
        recyclerViewSong = findViewById(R.id.tl_rcv_song);

        recyclerViewSong.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerViewSong.setItemAnimator(new DefaultItemAnimator());
        songListAdapter = new SongListAdapter(this, listSong);
        recyclerViewSong.setAdapter(songListAdapter);

    }
}