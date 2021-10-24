package com.javateam.muzik;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.javateam.muzik.entity.Album;
import com.javateam.muzik.entity.Artist;
import com.javateam.muzik.entity.Song;
import com.javateam.muzik.service.PlayerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    private SimpleExoPlayer simpleExoPlayer;
    private final String TAG = "PlayActivity";
    public static final String KEY_REQUEST_SHUFFLE = "SHUFFLE_REQUEST";

    private Intent intent;
    private TextView songName;
    private TextView artistNames;
    private ImageView songImg;
    private Map<String, Song> playList;
    private ImageView repeatBtn;
    private ImageView shuffleBtn;
    private boolean shuffleRequest;
    private Song playingSong;

    private PlayerView playerControlView;
    private PlayerService playerService;
    private boolean isServiceConnected;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayerService.MyBinder myBinder = (PlayerService.MyBinder) iBinder;
            playerService = myBinder.getNotificationService();
            isServiceConnected = true;
            // only after connecting with service, this activity is ready to do main work
            doMainWork();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            playerService = null;
            isServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        if (!isServiceConnected) {
            connectPlayService();
        }
    }

    private void doMainWork() {

        mappingUI();
        setupUI();
        play();
    }

    private void mappingUI() {
        songName = findViewById(R.id.play_tv_song_name);
        artistNames = findViewById(R.id.play_tv_artist_name);
        songImg = findViewById(R.id.play_iv_song_img);

        // controller
        repeatBtn = findViewById(R.id.player_repeat);
        shuffleBtn = findViewById(R.id.player_shuffle);
        playerControlView = findViewById(R.id.exo_player_view);
    }
    private void connectPlayService() {
        Intent intent = new Intent(this, PlayerService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setupUI() {
        songName.setSelected(true);
        artistNames.setSelected(true);
        playerControlView.setControllerShowTimeoutMs(0);

        // Repeat control
        repeatBtn.setOnClickListener(view -> {
            int repeatMode = simpleExoPlayer.getRepeatMode();
            if (repeatMode == Player.REPEAT_MODE_OFF) {
                simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
                repeatBtn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_repeat, getTheme()));
            } else if (repeatMode == Player.REPEAT_MODE_ALL) {
                simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
                repeatBtn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_repeat_one, getTheme()));
            } else {
                simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
                repeatBtn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_repeat_disable, getTheme()));
            }
        });

        // Shuffle control
        shuffleBtn.setOnClickListener(view -> {
            boolean shuffleEnable =  simpleExoPlayer.getShuffleModeEnabled();
            if (!shuffleEnable) {
                shuffleBtn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_shuffle, getTheme()));
            } else {
                shuffleBtn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_shuffle_disable, getTheme()));
            }
            simpleExoPlayer.setShuffleModeEnabled(!shuffleEnable);
        });
    }

    private void play() {
        simpleExoPlayer = playerService.getSimpleExoPlayer(this);
        playerControlView.setPlayer(simpleExoPlayer);

        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Log.e(TAG, "song change with reason: " + reason);
                // TODO: need to handle with media change reason
                if (mediaItem != null) {
                    playingSong = playList.get(mediaItem.mediaId);
                    if (playingSong != null) {
                        updateUi(playingSong);
                        if (!simpleExoPlayer.isPlaying()) {
                            simpleExoPlayer.play();
                        }
                    }
                    if (reason == SimpleExoPlayer.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                        playerService.createNotification(playingSong);
                    }
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (playingSong != null) {
                    playerService.createNotification(playingSong);
                }
            }
        });

        // load playlist
        prepareDataToPlay();

        // start play
        simpleExoPlayer.prepare();
        simpleExoPlayer.setPlayWhenReady(true);

    }

    private void prepareDataToPlay() {
        // Prepare song data here
        intent = getIntent();
        playList = new HashMap<>();

        // load to playlist - Map type
        String type = intent.getStringExtra("type");
        if (type.equals("album")) {
            Album album = (Album) intent.getSerializableExtra("album");
            List<Song> listSong = album.getSongs();
            for (Song song : listSong) {
                playList.put(song.getId().toString(), song);
            }
        } else if (type.equals("artist")) {
            Artist artist = (Artist) intent.getSerializableExtra("artist");
            List<Song> listSong = artist.getSongs();
            for (Song song : listSong) {
                playList.put(song.getId().toString(), song);
            }
        } else { // single song
            Song song = (Song) intent.getSerializableExtra("song");
            playList.put(song.getId().toString(), song);
        }

        List<MediaItem> listItem = new ArrayList<>();
        for (Map.Entry<String, Song> item: playList.entrySet()) {
            listItem.add(new MediaItem.Builder().setUri(
                    Uri.parse(item.getValue().getSongUrl())
            ).setMediaId(item.getKey()).build());
        }
        processShuffleRequest(listItem);
        simpleExoPlayer.setMediaItems(listItem,true);
    }

    private void processShuffleRequest(List<MediaItem> listItem) {
        shuffleRequest = intent.getBooleanExtra(KEY_REQUEST_SHUFFLE, false);
        if (shuffleRequest) {
            Collections.shuffle(listItem, new Random(System.currentTimeMillis()));
            simpleExoPlayer.setShuffleModeEnabled(true);
            shuffleBtn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_shuffle, getTheme()));
        } else {
            // TODO: may be deprecated
            simpleExoPlayer.setShuffleModeEnabled(false);
            shuffleBtn.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_shuffle_disable, getTheme()));
        }
    }
    private void updateUi(@NonNull Song song) {
        songName.setText(song.getName());
        artistNames.setText(song.getArtistsName());

        Glide.with(getApplicationContext())
                .load(song.getImgUrl())
                .into(songImg);
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "Destroy");
        unbindService(serviceConnection);
        super.onDestroy();
    }
}