package net.bluxget.uiteur.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
            /*case MySensorService.ACTION_SENSOR_ACCELEROMETER:
                this.mActivity.clickPreviousBtn();

                break;*/
            case MySensorService.ACTION_SENSOR_GYROSCOPE:
                float gyroscope = intent.getFloatExtra("x", 0) + intent.getFloatExtra("y", 0) + intent.getFloatExtra("z", 0);

                if(gyroscope > 5 || gyroscope < -5) {
                    this.mActivity.clickStateBtn();
                }

                break;
            case MediaPlayerActivity.ACTION_PLAY:
                this.mActivity.clickStateBtn();

                break;
            case MySensorService.ACTION_SENSOR_LOCATION:
                Log.d("Location", "Latitude:"+ intent.getDoubleExtra("latitude", 0) +" - Longitude: "+ intent.getDoubleExtra("longitude", 0) +" - Altitude: "+ intent.getDoubleExtra("altitude", 0));

                break;
        }
    }
}
