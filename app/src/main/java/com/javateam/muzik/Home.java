package com.javateam.muzik;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import java.util.List;

public class Home extends AppCompatActivity {

    private final String TAG = "Home";
    private BottomNavigationBar bottomNavigationBar;
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
        initBottomNavigation();
        initBottomMusicController();
    }

    private void initBottomMusicController() {
        bottomMusicController = new BottomMusicController(this, TAG, findViewById(R.id.home_mcl));
    }

    private void initBottomNavigation() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_private_musics, R.string.title_private_musics))
                .addItem(new BottomNavigationItem(R.drawable.ic_playlists, R.string.title_playlists))
                .addItem(new BottomNavigationItem(R.drawable.ic_online_musics, R.string.title_online_musics))
                .addItem(new BottomNavigationItem(R.drawable.ic_albums, R.string.title_albums))
                .addItem(new BottomNavigationItem(R.drawable.ic_artists, R.string.title_artists))
                .setFirstSelectedPosition(2)
                .initialise();

        // prepare first fragment
        Fragment fragment = new OnlineMusicsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list_album", (Serializable) listAlbum);
        bundle.putSerializable("list_artist", (Serializable) listArtist);
        bundle.putSerializable("list_song", (Serializable) listSong);
        fragment.setArguments(bundle);
        loadFragment(fragment);

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                bundle.putSerializable("list_album", (Serializable) listAlbum);
                bundle.putSerializable("list_artist", (Serializable) listArtist);
                bundle.putSerializable("list_song", (Serializable) listSong);
                switch (position) {
                    case 0: fragment = new PrivateMusicsFragment(); break;
                    case 1: fragment = new PlaylistsFragment(); break;
                    case 3: fragment = new AlbumsFragment(); break;
                    case 4: fragment = new ArtistsFragment(); break;
                    default: fragment = new OnlineMusicsFragment();
                }
                fragment.setArguments(bundle);
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
            Bundle bundle = new Bundle();

            bundle.putSerializable(IK_LIST_SONG, (Serializable) listSong);
            bundle.putSerializable(IK_LIST_ALBUM, (Serializable) listAlbum);
            bundle.putSerializable(IK_LIST_ARTIST, (Serializable) listArtist);
            bundle.putSerializable(IK_LIST_CATEGORY, (Serializable) listCategory);
            intent.putExtras(bundle);

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}