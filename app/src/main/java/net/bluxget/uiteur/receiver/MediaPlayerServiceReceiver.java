package net.bluxget.uiteur.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.bluxget.uiteur.service.MediaPlayerService;

/**
 * MediaPlayerService receiver
 *
 * @author Jonathan B.
 */
public class MediaPlayerServiceReceiver extends BroadcastReceiver {

    private MediaPlayerService mService;

    public MediaPlayerServiceReceiver(MediaPlayerService service) {
        this.mService = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case MediaPlayerService.ACTION_PLAY:
                this.mService.play(intent.getIntExtra("playid", 0));

                break;
            case MediaPlayerService.ACTION_PAUSE:
                this.mService.pause();

                break;
        }
    }
}
