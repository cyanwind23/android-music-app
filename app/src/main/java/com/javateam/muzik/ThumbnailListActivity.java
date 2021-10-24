package com.javateam.muzik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.javateam.muzik.adapter.SongListAdapter;
import com.javateam.muzik.adapter.TLViewPagerAdapter;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Song;
import com.javateam.muzik.service.PlayerService;
import com.javateam.muzik.ui.layout.BottomMusicController;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.List;

public class ThumbnailListActivity extends AppCompatActivity {

    private final String TAG = "ThumbnailListActivity";

    private Album album;
    private Artist artist;
    private List<Song> listSong;

    private RecyclerView recyclerViewSong;
    private SongListAdapter songListAdapter;

    private ViewPager2 viewPager2;
    private TLViewPagerAdapter viewPagerAdapter;
    private DotsIndicator dotsIndicator;
    private Button randPlayButton;
    private Intent intent;

    private BottomMusicController bottomMusicController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail_list);

        mappingUI();
        setupUI();
        prepareData();
    }

    private void mappingUI() {
        randPlayButton = findViewById(R.id.tl_rand_play_btn);
        viewPager2 = findViewById(R.id.tl_viewpager);
        dotsIndicator = findViewById(R.id.dots_indicator);
        recyclerViewSong = findViewById(R.id.tl_rcv_song);

        bottomMusicController = new BottomMusicController(this, TAG, findViewById(R.id.tl_mcl));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomMusicController.destroy();
    }

    private void setupUI() {

        randPlayButton.setOnClickListener(view -> {
            if (listSong == null || listSong.size() < 1) {
                Toast.makeText(getApplicationContext(), R.string.no_song_available, Toast.LENGTH_SHORT).show();
            } else {
                Intent toPlayIntent = new Intent(getApplicationContext(), PlayActivity.class);
                toPlayIntent.putExtras(intent.getExtras());
                toPlayIntent.putExtra(PlayActivity.KEY_REQUEST_SHUFFLE, true);
                startActivity(toPlayIntent);
            }
        });
    }

    private void prepareData() {
        intent = getIntent();
        viewPagerAdapter = new TLViewPagerAdapter(this, intent);
        viewPager2.setAdapter(viewPagerAdapter);
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
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerViewSong.setItemAnimator(new DefaultItemAnimator());
        songListAdapter = new SongListAdapter(this, listSong);
        recyclerViewSong.setAdapter(songListAdapter);
    }

}