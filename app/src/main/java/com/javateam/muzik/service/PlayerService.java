package com.javateam.muzik.service;

import static com.javateam.muzik.MuzikApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.javateam.muzik.PlayActivity;
import com.javateam.muzik.R;
import com.javateam.muzik.entity.Song;
import com.javateam.muzik.receiver.PlayerReceiver;

public class PlayerService extends Service {

    private final String TAG = "PlayerService";
    private final String SESSION_TAG = "MUSIC_NOTIFICATION";
    public static final String ACTION_TAG = "PLAYER_SERVICE_ACTION";

    public static final String KEY_MUSIC_ACTION = "MUSIC_ACTION";
    public static final String KEY_PLAYING_STATUS = "PLAYING_STATUS";
    public static final String KEY_PLAYING_SONG = "PLAYING_SONG";
    public static final String KEY_TO_CLIENT = "TO_CLIENT";

    public static final int ACTION_CLEAR = 0;
    public static final int ACTION_TOGGLE_PLAY_PAUSE = 1;
    public static final int ACTION_NEXT = 2;
    public static final int ACTION_PREV = 3;

    public static final int REQUEST_STATUS = 10;

    public static final String INTENT_ACTION_CODE = "ACTION_CODE";
    public static final String INTENT_CLIENT_TAG = "CLIENT_TAG";
    public static final String ALL_CLIENT = "ALL_CLIENT";

    private final int MUSIC_NOTIFICATION_ID = 1;

    private boolean isPlaying;
    private Song playingSong;
    private SimpleExoPlayer simpleExoPlayer;
    private final MyBinder myBinder = new MyBinder();
    private NotificationManager notificationManager;

    public class MyBinder extends Binder {
        public PlayerService getNotificationService() {
            return PlayerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * @notes: if INTENT_ACTION_CODE is ACTION_NEXT || ACTION_PREV, it may cause 3 state changes:
     * - stop Exo
     * - next || prev Window
     * - play Exo
     * => send notification and call sendActionToActivity() 3 times
     * TODO: fix logic here in future
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int actionCode = intent.getIntExtra(INTENT_ACTION_CODE, -1);
        String clientTag = intent.getStringExtra(INTENT_CLIENT_TAG);
        if (actionCode != -1) {
            handleAction(actionCode, clientTag);
        }

        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "destroy");
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        simpleExoPlayer = null;
        notificationManager.cancelAll();
        super.onDestroy();
    }

    public SimpleExoPlayer getSimpleExoPlayer(Context context) {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
        simpleExoPlayer = new SimpleExoPlayer.Builder(context).build();
        return simpleExoPlayer;
    }

    public void createNotification(@NonNull Song song) {
        playingSong = song;
        Glide.with(this)
                .asBitmap()
                .load(Uri.parse(song.getImgUrl()))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        sendNotification(song, resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
    }

    private void sendNotification(@NonNull Song song, Bitmap img) {
        if (simpleExoPlayer == null) {return;}

        Intent intent = new Intent(this, PlayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, MUSIC_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        int iconPlayPause = R.drawable.ic_pause_btn;
        isPlaying = true;
        if (!simpleExoPlayer.isPlaying()) {
            iconPlayPause = R.drawable.ic_play_btn;
            isPlaying = false;
        }

        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, SESSION_TAG);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(song.getName())
                .setContentText(song.getArtistsName())
                .setSmallIcon(R.drawable.ic_play_btn)
                .setContentIntent(pendingIntent)
                .setSilent(true)
                .setLargeIcon(img)
                .addAction(R.drawable.ic_prev_btn, "Previous", getPendingIntent(this, ACTION_PREV))
                .addAction(iconPlayPause, "PlayPause", getPendingIntent(this, ACTION_TOGGLE_PLAY_PAUSE))
                .addAction(R.drawable.ic_next_btn, "Next", getPendingIntent(this, ACTION_NEXT))
                .addAction(R.drawable.ic_close, "Close", getPendingIntent(this, ACTION_CLEAR))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2, 3)
                        .setMediaSession(mediaSessionCompat.getSessionToken())
                )
                .build();

//      .setSound(null) // turn off default notification sound - for android sdk < 26
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(MUSIC_NOTIFICATION_ID, notification);

        Log.e(TAG, "Send notification");
        sendActionToActivity(ACTION_TOGGLE_PLAY_PAUSE);
    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(context, PlayerReceiver.class);
        intent.putExtra(INTENT_ACTION_CODE, action);

        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void handleAction(int action, String clientTag) {
        if (action < 10) {
            switch (action) {
                case ACTION_TOGGLE_PLAY_PAUSE:
                    togglePlayPauseMusic(); break;

                case ACTION_PREV:
                    prevMusic(); break;

                case ACTION_NEXT:
                    nextMusic(); break;

                case ACTION_CLEAR:
                    clearMusic(); break;
            }
        } else {
            switch (action) {
                case REQUEST_STATUS:
                    sendActionToActivity(REQUEST_STATUS, clientTag); break;
            }
        }

    }

    private void togglePlayPauseMusic() {
        if (simpleExoPlayer == null) {
            return;
        }
        if (simpleExoPlayer.isPlaying()) {
            // pause
            simpleExoPlayer.pause();
        } else {
            // play
            simpleExoPlayer.play();
        }
    }

    private void nextMusic() {
        if (simpleExoPlayer != null && simpleExoPlayer.hasNextWindow()) {
            simpleExoPlayer.seekToNextWindow();
            sendActionToActivity(ACTION_NEXT);
        }
    }

    private void prevMusic() {
        if (simpleExoPlayer != null && simpleExoPlayer.hasPreviousWindow()) {
            simpleExoPlayer.seekToPreviousWindow();
            sendActionToActivity(ACTION_PREV);
        }
    }

    private void clearMusic() {
        simpleExoPlayer.stop();
        notificationManager.cancelAll();
        Log.e(TAG, "Close notification - Stop service");
    }

    public void sendActionToActivity(int action, String clientTag) {

        Intent intent = new Intent(ACTION_TAG);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PLAYING_SONG, playingSong);
        bundle.putInt(KEY_MUSIC_ACTION, action);
        bundle.putBoolean(KEY_PLAYING_STATUS, isPlaying);
        bundle.putString(KEY_TO_CLIENT, clientTag);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void sendActionToActivity(int action) {
        sendActionToActivity(action, ALL_CLIENT);
    }
}
