package net.bluxget.uiteur;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * MediaPlayerService receiver
 *
 * @author Jonathan B.
 */
public class ReceiverMediaPlayerService extends BroadcastReceiver {

    private MediaPlayerService mService;

    public ReceiverMediaPlayerService(MediaPlayerService service) {
        this.mService = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case MediaPlayerService.ACTION_PLAY:
                this.mService.play();

                break;
            case MediaPlayerService.ACTION_PAUSE:
                this.mService.pause();

                break;
            case MediaPlayerService.ACTION_PREVIOUS:
                this.mService.previous();

                break;
            case MediaPlayerService.ACTION_NEXT:
                this.mService.next();

                break;
        }
    }
}
