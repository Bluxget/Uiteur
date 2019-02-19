package net.bluxget.uiteur.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import net.bluxget.uiteur.data.access.DataHandler;
import net.bluxget.uiteur.receiver.MediaPlayerServiceReceiver;

import java.io.File;

/**
 * MediaPlayer service, basic music function (start/stop & next/previous)
 *
 * @author Jonathan B.
 */
public class MediaPlayerService extends Service {

    private static final String LOG_TAG = ApiService.class.getSimpleName();

    public static final String ACTION_PLAY = "mp_play", ACTION_PAUSE = "mp_pause";

    private DataHandler mDataHandler;
    private MediaPlayer mMediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] actions = {ACTION_PLAY, ACTION_PAUSE};

        mDataHandler = new DataHandler(this);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

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

    public void play(int playId) {
        this.mDataHandler.open();
        String filename = this.mDataHandler.getPlayFile(playId);
        this.mDataHandler.close();

        if(filename.length() > 0) {
            try {
                File file = new File(getFilesDir(), filename);
                this.mMediaPlayer.setDataSource(getApplicationContext(), Uri.fromFile(file));
                this.mMediaPlayer.prepare();
                this.mMediaPlayer.start();
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Unable to play sound");
            }
        } else {
            Log.d(LOG_TAG, "No file found for play: " + playId);
        }
    }

    public void pause() {
        this.mMediaPlayer.pause();
    }

    /*private int end = 0;
    public void playlist(Document document) {
        Log.d("test", XMLToString(document));

        if(this.end == 0) {
            final String url = "http://uiteur.struct-it.fr/playlist.php";

            new HTTPRequestTask(this).execute(url);
            this.end = 1;
        }
    }

    public static String XMLToString(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }*/
}
