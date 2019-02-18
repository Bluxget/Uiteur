package net.bluxget.uiteur.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import net.bluxget.uiteur.receiver.MediaPlayerServiceReceiver;
import net.bluxget.uiteur.task.AuthTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * MediaPlayer service, basic music function (start/stop & next/previous)
 *
 * @author Jonathan B.
 */
public class MediaPlayerService extends Service {

    public static final String ACTION_PLAY = "mp_play", ACTION_PAUSE = "mp_pause", ACTION_PREVIOUS = "mp_previous", ACTION_NEXT = "mp_next";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] actions = {ACTION_PLAY, ACTION_PAUSE, ACTION_NEXT, ACTION_PREVIOUS};

        for (String action : actions) {
            LocalBroadcastManager.getInstance(this).registerReceiver(new MediaPlayerServiceReceiver(this), new IntentFilter(action));
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void play() {
        Log.d("test", "play");

        final String url = "http://uiteur.struct-it.fr/login.php?user="+ "uiteur" +"&pwd="+ "test";

        URL serviceURL = null;

        try {
            serviceURL = new URL(url);
        } catch (MalformedURLException ex) {
            Log.e("test", ex.getMessage());
        }

        new AuthTask().execute(serviceURL);
    }

    public void pause() {
        Log.d("test", "pause");
    }

    public void previous() {
        Log.d("test", "previous");
    }

    public void next() {
        Log.d("test", "next");
    }
}