package net.bluxget.uiteur.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.bluxget.uiteur.activity.MediaPlayerActivity;
import net.bluxget.uiteur.service.MediaPlayerService;
import net.bluxget.uiteur.service.MySensorService;

/**
 * MediaPlayerActivity receiver
 *
 * @author Jonathan B.
 */
public class MediaPlayerActivityReceiver extends BroadcastReceiver {

    private MediaPlayerActivity mActivity;

    public MediaPlayerActivityReceiver(MediaPlayerActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case MySensorService.ACTION_SENSOR_ACCELEROMETER:
                this.mActivity.clickPreviousBtn();

                break;
        }
    }
}
