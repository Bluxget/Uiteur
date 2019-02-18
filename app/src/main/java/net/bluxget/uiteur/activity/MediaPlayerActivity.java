package net.bluxget.uiteur.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.bluxget.uiteur.R;
import net.bluxget.uiteur.service.MediaPlayerService;


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
}
