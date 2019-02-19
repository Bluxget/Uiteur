package net.bluxget.uiteur.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.FloatMath;
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
    private boolean mNoLight;

    private double mAccel;
    private double mAccelCurrent;
    private double mAccelLast;

    public MediaPlayerActivityReceiver(MediaPlayerActivity activity) {
        this.mActivity = activity;
        this.mNoLight = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case MySensorService.ACTION_SENSOR_GYROSCOPE:
                Log.d("Gyroscope", "x: "+ intent.getFloatExtra("x", 0) +" - y: "+ intent.getFloatExtra("y", 0) +" - z: "+ intent.getFloatExtra("z", 0));

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
