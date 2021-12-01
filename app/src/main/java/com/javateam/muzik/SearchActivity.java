package com.javateam.muzik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.javateam.muzik.adapter.AlbumListAdapter;
import com.javateam.muzik.adapter.ArtistListAdapter;
import com.javateam.muzik.adapter.SongListAdapter;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private int criterion = 0;
    private static final int SONG_CRITERION = 1;
    private static final int ARTIST_CRITERION = 2;
    private static final int ALBUM_CRITERION = 3;
    

    private SongListAdapter songListAdapter;
    private AlbumListAdapter albumListAdapter;
    private ArtistListAdapter artistListAdapter;

    private RecyclerView recyclerView;
    private Map<Integer, Button> mapBtn;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Search");

        // get data
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        List<Song> listSong = (List<Song>) bundle.get(Home.IK_LIST_SONG);
        List<Artist> listArtist = (List<Artist>) bundle.get(Home.IK_LIST_ARTIST);
        List<Album> listAlbum = (List<Album>) bundle.get(Home.IK_LIST_ALBUM);

        songListAdapter = new SongListAdapter(this, listSong);
        artistListAdapter = new ArtistListAdapter(this, listArtist);
        albumListAdapter = new AlbumListAdapter(this, listAlbum);

        // UI mapping
        mapBtn = new HashMap<>();
        mapBtn.put(SONG_CRITERION, (Button) findViewById(R.id.song_criterion));
        mapBtn.put(ARTIST_CRITERION, (Button) findViewById(R.id.artist_criterion));
        mapBtn.put(ALBUM_CRITERION, (Button) findViewById(R.id.album_criterion));
        
        // Default search with song
        criterion = SONG_CRITERION;
        recyclerView = findViewById(R.id.rcv_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(songListAdapter);
        
    }

    private void createEventListenerForBtn() {
        for (Map.Entry<Integer, Button> entry : mapBtn.entrySet()) {
            Button button = entry.getValue();
            button.setOnClickListener(view -> {
                String keyword = searchView.getQuery().toString();
                int btnId = view.getId();
                if (btnId == R.id.artist_criterion) {
                    criterion = ARTIST_CRITERION;
                    artistListAdapter.getFilter().filter(keyword);
                    recyclerView.setAdapter(artistListAdapter);
                } else if (btnId == R.id.album_criterion) {
                    criterion = ALBUM_CRITERION;
                    albumListAdapter.getFilter().filter(keyword);
                    recyclerView.setAdapter(albumListAdapter);
                } else {
                    criterion = SONG_CRITERION;
                    songListAdapter.getFilter().filter(keyword);
                    recyclerView.setAdapter(songListAdapter);
                }

                Objects.requireNonNull(mapBtn.get(criterion)).setTextColor(Color.parseColor("#00BCD4"));
                for (int i = 1; i <= 3; i++) {
                    if (i == criterion) continue;
                    Objects.requireNonNull(mapBtn.get(i)).setTextColor(Color.rgb(255, 255, 255));
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnCloseListener(() -> true); // true to prevent default, not collapse search view

        // after init search view, create listener for criteria buttons
        createEventListenerForBtn();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                switch (criterion) {
                    case ARTIST_CRITERION:
                        artistListAdapter.getFilter().filter(query); break;
                    case ALBUM_CRITERION:
                        albumListAdapter.getFilter().filter(query); break;
                    default: // default search with song
                        songListAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (criterion) {
                    case ARTIST_CRITERION:
                        artistListAdapter.getFilter().filter(newText); break;
                    case ALBUM_CRITERION:
                        albumListAdapter.getFilter().filter(newText); break;
                    default: // default search with song
                        songListAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search_back) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }
    }
}