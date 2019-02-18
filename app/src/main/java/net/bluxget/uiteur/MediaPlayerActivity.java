package net.bluxget.uiteur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


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

    public void onClickPreviousBtn(View v) {
        Log.d("test", "previous");
    }

    public void onClickStateBtn(View v) {
        Button stateBtn = findViewById(R.id.mp_state);

        if (this.mPlay) {
            Log.d("test", "stop");
            this.mPlay = false;
            stateBtn.setBackgroundResource(android.R.drawable.ic_media_play);
        } else {
            Log.d("test", "play");
            this.mPlay = true;
            stateBtn.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    public void onClickNextBtn(View v) {
        Log.d("test", "next");
    }
}
