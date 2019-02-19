package net.bluxget.uiteur.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bluxget.uiteur.R;
import net.bluxget.uiteur.data.PlayItem;
import net.bluxget.uiteur.data.access.DataHandler;
import net.bluxget.uiteur.receiver.MediaPlayerActivityReceiver;
import net.bluxget.uiteur.service.ApiService;
import net.bluxget.uiteur.service.MediaPlayerService;
import net.bluxget.uiteur.service.MySensorService;

import java.io.File;
import java.util.ArrayList;

/**
 * MediaPlayer main interface, play/stop & next/previous buttons
 *
 * @author Jonathan B.
 */
public class MediaPlayerActivity extends AppCompatActivity {

    private static final String LOG_TAG = ApiService.class.getSimpleName();

    public static final String ACTION_PLAY = "mp_activity_play";

    private boolean mPlay = false;

    private DataHandler mDataHandler;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    private ArrayList<PlayItem> mPlayList;

    private String mPlayListName;
    private static int mPlayListId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String url = intent.getStringExtra("url");

        mDataHandler = new DataHandler(this);

        mPlayListName = intent.getStringExtra("playlist");
        mPlayListId = intent.getIntExtra("playlistid", -1);

        mPlayList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.playlist);

        mAdapter = new RecyclerViewAdapter(this, mPlayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent mediaPlayer = new Intent(this, MediaPlayerService.class);

        startService(mediaPlayer);

        if(mPlayListId > -1) {
            mPlayList.clear();

            mDataHandler.open();
            mPlayList.addAll(mDataHandler.getPlayList());
            mDataHandler.close();
        } else {
            Intent intent = new Intent(this, ApiService.class);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }

        Intent intentSensor = new Intent(this, MySensorService.class);

        startService(intentSensor);

        String[] sensorFilter = { MySensorService.ACTION_SENSOR_GYROSCOPE, MySensorService.ACTION_SENSOR_LOCATION };

        IntentFilter sensorIntentFilter = new IntentFilter();

        for(String action : sensorFilter) {
            sensorIntentFilter.addAction(action);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(new MediaPlayerActivityReceiver(this), sensorIntentFilter);

        LocalBroadcastManager.getInstance(this).registerReceiver(new MediaPlayerActivityReceiver(this), new IntentFilter(ACTION_PLAY));

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
        //LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MediaPlayerService.ACTION_PREVIOUS));
    }

    public void onClickNextBtn(View v) {
        //LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MediaPlayerService.ACTION_NEXT));
    }

    public void clickStateBtn() {
        Button stateBtn = findViewById(R.id.mp_state);

        this.mPlay = false;

        stateBtn.callOnClick();
    }

    public void clickNextBtn() {
        Button nextBtn = findViewById(R.id.mp_next);

        nextBtn.callOnClick();
    }
}
