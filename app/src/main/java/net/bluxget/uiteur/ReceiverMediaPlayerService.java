package net.bluxget.uiteur;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverMediaPlayerService extends BroadcastReceiver {

    private MediaPlayerService mService;

    public ReceiverMediaPlayerService(MediaPlayerService service) {
        this.mService = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case MediaPlayerService.ACTION_PLAY:
                Log.d("test", "receive play");

                break;
            case MediaPlayerService.ACTION_PAUSE:
                Log.d("test", "receive pause");

                break;
            case MediaPlayerService.ACTION_PREVIOUS:
                Log.d("test", "receive previous");

                break;
            case MediaPlayerService.ACTION_NEXT:
                Log.d("test", "receive next");

                break;
        }
    }
}
