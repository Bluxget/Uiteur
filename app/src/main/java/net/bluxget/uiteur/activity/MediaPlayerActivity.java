package net.bluxget.uiteur.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.bluxget.uiteur.R;
import net.bluxget.uiteur.receiver.MediaPlayerActivityReceiver;
import net.bluxget.uiteur.service.MediaPlayerService;
import net.bluxget.uiteur.service.MySensorService;


/**
 * MediaPlayer main interface, play/stop & next/previous buttons
 *
 * @author Jonathan B.
 */
public class MediaPlayerActivity extends AppCompatActivity {

    private boolean mPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent mediaPlayer = new Intent(this, MediaPlayerService.class);

        startService(mediaPlayer);

        Intent intentSensor = new Intent(this, MySensorService.class);

        startService(intentSensor);

        String[] sensorFilter = { MySensorService.ACTION_SENSOR_GYROSCOPE, MySensorService.ACTION_SENSOR_LOCATION };

        IntentFilter sensorIntentFilter = new IntentFilter();

        for(String action : sensorFilter) {
            sensorIntentFilter.addAction(action);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(new MediaPlayerActivityReceiver(this), sensorIntentFilter);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 200);
            }
        }
    }

    public void onClickStateBtn(View v) {
        Button stateBtn = findViewById(R.id.mp_state);
        String action;

        if (this.mPlay) {
            stateBtn.setBackgroundResource(android.R.drawable.ic_media_play);
            this.mPlay = false;

            action = MediaPlayerService.ACTION_PAUSE;
        } else {
            stateBtn.setBackgroundResource(android.R.drawable.ic_media_pause);
            this.mPlay = true;

            action = MediaPlayerService.ACTION_PLAY;
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
    }

    public void onClickPreviousBtn(View v) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MediaPlayerService.ACTION_PREVIOUS));
    }

    public void onClickNextBtn(View v) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MediaPlayerService.ACTION_NEXT));
    }

    public void clickNextBtn() {
        Button nextBtn = findViewById(R.id.mp_next);

        nextBtn.callOnClick();
    }
}
