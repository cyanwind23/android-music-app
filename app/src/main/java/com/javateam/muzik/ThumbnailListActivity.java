package com.javateam.muzik;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.javateam.muzik.adapter.SongListAdapter;
import com.javateam.muzik.adapter.TLViewPagerAdapter;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Song;
import com.javateam.muzik.ui.layout.BottomMusicController;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.List;

public class ThumbnailListActivity extends AppCompatActivity {

    public static final String TAG = "ThumbnailListActivity";

    /** Warning: These keys must be the same as PlayActivity's IKs **/
    public static final String IK_TYPE_ALBUM = "album";
    public static final String IK_TYPE_ARTIST = "artist";
    public static final String IK_TYPE = "type";

    private List<Song> listSong;

    private RecyclerView recyclerViewSong;

    private ViewPager2 viewPager2;
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
                // putting extra directly to PlayActivity requires this activity's keys and PlayActivity's keys to be the same
                toPlayIntent.putExtras(intent.getExtras());
                toPlayIntent.putExtra(PlayActivity.KEY_REQUEST_SHUFFLE, true);
                startActivity(toPlayIntent);
            }
        });
    }

    private void prepareData() {
        intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        TLViewPagerAdapter viewPagerAdapter = new TLViewPagerAdapter(this, intent);
        viewPager2.setAdapter(viewPagerAdapter);
        dotsIndicator.setViewPager2(viewPager2);
        String type = intent.getStringExtra(IK_TYPE);
        if (type.equals(IK_TYPE_ALBUM)) {
            Album album = (Album) intent.getSerializableExtra(IK_TYPE_ALBUM);
            listSong = album.getSongs();
            if (actionBar != null)
                actionBar.setTitle(R.string.title_albums);
        } else {
            Artist artist = (Artist) intent.getSerializableExtra(IK_TYPE_ARTIST);
            listSong = artist.getSongs();
            if (actionBar != null)
                actionBar.setTitle(R.string.title_artists);
        }
        prepareSong();
    }

    private void prepareSong() {
        // For Song RCV
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerViewSong.setItemAnimator(new DefaultItemAnimator());
        SongListAdapter songListAdapter = new SongListAdapter(this, listSong);
        recyclerViewSong.setAdapter(songListAdapter);
    }

}