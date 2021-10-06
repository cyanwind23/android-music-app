package com.javateam.muzik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.javateam.muzik.entity.Song;

public class PlayActivity extends AppCompatActivity {
    static SimpleExoPlayer simpleExoPlayer;
    static PlayerView playerControlView;
    Intent intent;
    Song playingSong;
    TextView songName;
    TextView artistNames;
    ImageView songImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Get song infor
        intent = getIntent();
        playingSong = (Song) intent.getSerializableExtra("playing_song");

        displaySongInfor();
        preparePlayer();
    }

    private void displaySongInfor() {
        songName = findViewById(R.id.play_tv_song_name);
        songName.setSelected(true);
        artistNames = findViewById(R.id.play_tv_artist_name);
        artistNames.setSelected(true);
        songImg = findViewById(R.id.play_iv_song_img);

        songName.setText(playingSong.getName());
        artistNames.setText(playingSong.getArtistsName());

        Glide.with(getApplicationContext())
                .load(playingSong.getImgUrl())
                .into(songImg);
    }

    private void preparePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
        playerControlView = findViewById(R.id.exo_player_view);
        playerControlView.setControllerShowTimeoutMs(0);
        simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        playerControlView.setPlayer(simpleExoPlayer);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "app"));
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(playingSong.getSongUrl()));

        MediaSource audioSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
        simpleExoPlayer.setMediaSource(audioSource);
        simpleExoPlayer.prepare();
        simpleExoPlayer.setPlayWhenReady(true);
    }
}