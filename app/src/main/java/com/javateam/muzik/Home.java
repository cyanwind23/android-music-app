package com.javateam.muzik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.javateam.muzik.ui.fragment.AlbumsFragment;
import com.javateam.muzik.ui.fragment.ArtistsFragment;
import com.javateam.muzik.ui.fragment.OnlineMusicsFragment;
import com.javateam.muzik.ui.fragment.PlaylistsFragment;
import com.javateam.muzik.ui.fragment.PrivateMusicsFragment;

public class Home extends AppCompatActivity {
    BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initBottomNavigation();

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
        loadFragment(new OnlineMusicsFragment());
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0: fragment = new PrivateMusicsFragment(); break;
                    case 1: fragment = new PlaylistsFragment(); break;
                    case 3: fragment = new AlbumsFragment(); break;
                    case 4: fragment = new ArtistsFragment(); break;
                    default: fragment = new OnlineMusicsFragment();
                }
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
}