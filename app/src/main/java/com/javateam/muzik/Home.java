package com.javateam.muzik;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.javateam.muzik.adapter.JSONParser;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Category;
import com.javateam.muzik.entity.Song;
import com.javateam.muzik.service.SongService;
import com.javateam.muzik.ui.fragment.AlbumsFragment;
import com.javateam.muzik.ui.fragment.ArtistsFragment;
import com.javateam.muzik.ui.fragment.OnlineMusicsFragment;
import com.javateam.muzik.ui.fragment.PlaylistsFragment;
import com.javateam.muzik.ui.fragment.PrivateMusicsFragment;
import com.javateam.muzik.ui.layout.BottomMusicController;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {

    public static final String TAG = "Home";
    private List<Album> listAlbum;
    private List<Artist> listArtist;
    private List<Category> listCategory;
    private List<Song> listSong;
    private BottomMusicController bottomMusicController;

    /** Intent keys **/
    public static final String IK_LIST_SONG = "list_song";
    public static final String IK_LIST_ALBUM = "list_album";
    public static final String IK_LIST_ARTIST = "list_artist";
    public static final String IK_LIST_CATEGORY = "list_category";

    /** Bundle to transfer **/
    private Bundle bundleToTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get data
        Intent intent = getIntent();
        try {
            listAlbum = JSONParser.parse(intent.getStringExtra("json_album"), Album.class);
            listArtist = JSONParser.parse(intent.getStringExtra("json_artist"), Artist.class);
            listCategory = JSONParser.parse(intent.getStringExtra("json_category"), Category.class);
            String listSongString = intent.getStringExtra("json_song");
            listSong = SongService.parseFull(listSongString, listArtist, listAlbum, listCategory);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

/*
        For debug
        listSong.size();
        listArtist.size();
        listAlbum.size();
        listCategory.size();
*/
        // Create bundle
        bundleToTransfer = new Bundle();
        bundleToTransfer.putSerializable(IK_LIST_SONG, (Serializable) listSong);
        bundleToTransfer.putSerializable(IK_LIST_ALBUM, (Serializable) listAlbum);
        bundleToTransfer.putSerializable(IK_LIST_ARTIST, (Serializable) listArtist);
        bundleToTransfer.putSerializable(IK_LIST_CATEGORY, (Serializable) listCategory);

        initBottomNavigation();
        initBottomMusicController();
    }

    private void initBottomMusicController() {
        bottomMusicController = new BottomMusicController(this, TAG, findViewById(R.id.home_mcl));
    }

    private void initBottomNavigation() {
        ArrayList<Integer> fragmentTitles = new ArrayList<>(Arrays.asList(
                R.string.title_private_musics,
                R.string.title_playlists,
                R.string.app_name,
                R.string.title_albums,
                R.string.title_artists
        ));
        BottomNavigationBar bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_private_musics, fragmentTitles.get(0))) // 0
                .addItem(new BottomNavigationItem(R.drawable.ic_playlists, fragmentTitles.get(1))) // 1
                .addItem(new BottomNavigationItem(R.drawable.ic_online_musics, R.string.title_online_musics)) // 2
                .addItem(new BottomNavigationItem(R.drawable.ic_albums, fragmentTitles.get(3))) // 3
                .addItem(new BottomNavigationItem(R.drawable.ic_artists, fragmentTitles.get(4))) // 4
                .setFirstSelectedPosition(2)
                .initialise();

        // prepare first fragment
        Fragment fragment = new OnlineMusicsFragment();
        fragment.setArguments(bundleToTransfer);
        loadFragment(fragment);

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Fragment fragment;
                switch (position) {
                    case 0: fragment = new PrivateMusicsFragment(); break;
                    case 1: fragment = new PlaylistsFragment(); break;
                    case 3: fragment = new AlbumsFragment(); break;
                    case 4: fragment = new ArtistsFragment(); break;
                    default: // default 2
                        fragment = new OnlineMusicsFragment();
                }

                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(fragmentTitles.get(position));
                }
                fragment.setArguments(bundleToTransfer);
                loadFragment(fragment);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bottomMusicController.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actions_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtras(bundleToTransfer);

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}