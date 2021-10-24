package com.javateam.muzik.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.javateam.muzik.service.PlayerService;

public class PlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int actionCode = intent.getIntExtra(PlayerService.INTENT_ACTION_CODE, -1);

        Intent toService = new Intent(context, PlayerService.class);
        toService.putExtra(PlayerService.INTENT_ACTION_CODE, actionCode);

        context.startService(toService);
    }
}
