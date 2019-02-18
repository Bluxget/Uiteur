package net.bluxget.uiteur;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * MediaPlayer service, basic music function (start/stop & next/previous)
 *
 * @author Jonathan B.
 */
public class MediaPlayerService extends Service {

    public static final String ACTION_PLAY = "mp_play", ACTION_PAUSE = "mp_pause", ACTION_PREVIOUS = "mp_previous", ACTION_NEXT = "mp_next";

    public MediaPlayerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] actions = {ACTION_PLAY, ACTION_PAUSE, ACTION_NEXT, ACTION_PREVIOUS};

        for (String action : actions){
            LocalBroadcastManager.getInstance(this).registerReceiver(new ReceiverMediaPlayerService(this), new IntentFilter(action));
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
