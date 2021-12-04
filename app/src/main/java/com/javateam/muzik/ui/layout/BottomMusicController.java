package com.javateam.muzik.ui.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.javateam.muzik.R;
import com.javateam.muzik.entity.Song;
import com.javateam.muzik.service.PlayerService;

public class BottomMusicController extends LinearLayout {

    private final String TAG = "BottomMusicController";
    private String instanceTag;
    private Song playingSong;
    private int musicAction;
    private boolean isPlaying;
    private LinearLayout musicControllerLayout;
    private ImageView mclPlayPauseBtn;
    private ImageView mclPrevBtn;
    private ImageView mclNextBtn;
    private ImageView mclSongImg;
    private TextView mclSongTitle;
    private TextView mclArtistTitle;
    private Context viewContext;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String clientTag = bundle.getString(PlayerService.KEY_TO_CLIENT);
            if (clientTag.equals(PlayerService.ALL_CLIENT) || clientTag.equals(instanceTag)) {
                playingSong = (Song) bundle.getSerializable(PlayerService.KEY_PLAYING_SONG);
                musicAction = bundle.getInt(PlayerService.KEY_MUSIC_ACTION);
                isPlaying = bundle.getBoolean(PlayerService.KEY_PLAYING_STATUS);
                if (playingSong != null) {
                    updateControllerLayout(musicAction);
                }
//            Log.e(TAG, "Receive");
            }
        }
    };

    // Constructor
    public BottomMusicController(Context context) {
        super(context);
        viewContext = context;
        LocalBroadcastManager.getInstance(context).
                registerReceiver(broadcastReceiver, new IntentFilter(PlayerService.ACTION_TAG));
    }
    public BottomMusicController(Context context, String tag, LinearLayout layout) {
        this(context);
        musicControllerLayout = layout;
        instanceTag = tag;
        initUi();
        setupUI();
        sendActionToService(PlayerService.REQUEST_STATUS);
    }
    private void initUi() {
        mclSongImg = musicControllerLayout.findViewById(R.id.mcl_song_img);
        mclPlayPauseBtn = musicControllerLayout.findViewById(R.id.mcl_play_pause_btn);
        mclSongTitle = musicControllerLayout.findViewById(R.id.mcl_song_title);
        mclArtistTitle = musicControllerLayout.findViewById(R.id.mcl_artist_title);
        mclPrevBtn = musicControllerLayout.findViewById(R.id.mcl_prev_btn);
        mclNextBtn = musicControllerLayout.findViewById(R.id.mcl_next_btn);
    }

    private void updateControllerLayout(int musicAction) {
        // first time
        if (musicControllerLayout.getVisibility() == View.GONE) {
            musicControllerLayout.setVisibility(View.VISIBLE);
        }

        int icon = R.drawable.ic_pause_btn;
        if (!isPlaying) {
            icon = R.drawable.ic_play_btn;
        }
        mclPlayPauseBtn.setImageResource(icon);
        loadSongToController();
    }

    private void loadSongToController() {
//        Log.e(TAG, instanceTag + ": Loading UI, with song is_null = " + (playingSong == null));
        if (playingSong != null) {
            Glide.with(viewContext)
                    .load(playingSong.getImgUrl())
                    .into(mclSongImg);
            mclSongTitle.setText(playingSong.getName());
            mclArtistTitle.setText(playingSong.getArtistsName());
        }
    }

    private void setupUI() {
        mclPlayPauseBtn.setOnClickListener(view -> sendActionToService(PlayerService.ACTION_TOGGLE_PLAY_PAUSE));

        mclPrevBtn.setOnClickListener(view -> sendActionToService(PlayerService.ACTION_PREV));

        mclNextBtn.setOnClickListener(view -> sendActionToService(PlayerService.ACTION_NEXT));

        mclSongTitle.setSelected(true);
        mclArtistTitle.setSelected(true);
    }

    void sendActionToService(int action) {
        Intent intent = new Intent(viewContext, PlayerService.class);
        intent.putExtra(PlayerService.INTENT_ACTION_CODE, action);
        intent.putExtra(PlayerService.INTENT_CLIENT_TAG, instanceTag);
        viewContext.startService(intent);
    }

    public void destroy() {
        LocalBroadcastManager.getInstance(viewContext).unregisterReceiver(broadcastReceiver);
    }
}
