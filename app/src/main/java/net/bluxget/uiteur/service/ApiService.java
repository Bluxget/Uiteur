package net.bluxget.uiteur.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import net.bluxget.uiteur.activity.LoginActivity;
import net.bluxget.uiteur.activity.MediaPlayerActivity;
import net.bluxget.uiteur.data.PlayItem;
import net.bluxget.uiteur.data.access.DataHandler;
import net.bluxget.uiteur.task.GetDataTask;
import net.bluxget.uiteur.task.GetFileTask;
import net.bluxget.uiteur.task.UserLoginTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * API service
 *
 * @author rcasteran
 */
public class ApiService extends Service {
    private static final String LOG_TAG = ApiService.class.getSimpleName();

    private static final int NOTIFICATION_CHANNEL_ID = 101;
    private static final String NOTIFICATION_CHANNEL_NAME = "LOCATION_CHANNEL";

    private DataHandler mDataHandler;

    private enum SERVICE_STATE {
        NOT_LOGGED_IN,
        LOGGED_IN}

    private static SERVICE_STATE mState = SERVICE_STATE.NOT_LOGGED_IN;

    private String mId;
    private String mURL;

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "Creating...");

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Integer.toString(NOTIFICATION_CHANNEL_ID),
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            Notification.Builder notificationBuilder = new Notification.Builder(this,
                    Integer.toString(NOTIFICATION_CHANNEL_ID));
            Notification notification = notificationBuilder.build();
            startForeground(NOTIFICATION_CHANNEL_ID, notification);

            this.mDataHandler = new DataHandler(this);

            this.mId = "Unknown";
            this.mURL = "Unknown";
        } //Else do nothing
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        Log.i(LOG_TAG, "Starting...");

        switch(mState) {
            case NOT_LOGGED_IN:
                UserLoginTask loginTask = new UserLoginTask(this);
                loginTask.execute(intent.getStringExtra("name"),
                        intent.getStringExtra("pwd"));
                break;

            case LOGGED_IN:
                if(this.mURL != "Unknown") {
                    GetDataTask dataTask = new GetDataTask(this);
                    dataTask.execute(this.mURL + "playlist.php");
                } // Else do nothing : Default values are used for test purpose
                break;

            default:
                break;
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notifyUserLogin(Boolean success, Document doc) {
        try {
            if(doc != null) {
                mState = SERVICE_STATE.LOGGED_IN;

                doc.getDocumentElement().normalize();
                Element root = doc.getDocumentElement();
                this.mId = root.getAttribute("id");
                this.mURL = root.getAttribute("url");

                Intent intent = new Intent(this, MediaPlayerActivity.class);
                intent.putExtra("id", this.mId);
                intent.putExtra("url", this.mURL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if(!success) {
                mState = SERVICE_STATE.NOT_LOGGED_IN;

                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Default values are used for test purpose
                mState = SERVICE_STATE.LOGGED_IN;

                Intent intent = new Intent(this, MediaPlayerActivity.class);
                intent.putExtra("id", this.mId);
                intent.putExtra("url", this.mURL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }
    }

    public void notifyData(Boolean success, Document doc) {
        try {
            if(doc != null) {
                doc.getDocumentElement().normalize();
                Element root = doc.getDocumentElement();
                String playListName = root.getAttribute("name");
                int playListId = Integer.parseInt(root.getAttribute("id"));

                NodeList nodePlayList = root.getChildNodes();
                for(int i=0; i < nodePlayList.getLength(); i++) {
                    if (nodePlayList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element playElement = (Element) nodePlayList.item(i);
                        int fileId = Integer.parseInt(playElement.getAttribute("id"));
                        String fileUrl = playElement.getAttribute("url");
                        mDataHandler.open();
                        PlayItem playItem = new PlayItem(
                                fileId,
                                playElement.getAttribute("name"),
                                playElement.getAttribute("author"),
                                playElement.getAttribute("record"),
                                fileUrl,
                                "");
                        mDataHandler.addPlayItem(playItem);
                        mDataHandler.close();

                        GetFileTask fileTask = new GetFileTask(this, playItem);
                        fileTask.execute(this.mURL + fileUrl, "" + playListId);
                    } // Else do nothing
                }

                Intent intent = new Intent(this, MediaPlayerActivity.class);
                intent.putExtra("id", this.mId);
                intent.putExtra("url", this.mURL);
                intent.putExtra("playlist", playListName);
                intent.putExtra("playlistid", playListId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if(!success) {
                mState = SERVICE_STATE.NOT_LOGGED_IN;

                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Default values are used for test purpose
                Intent intent = new Intent(this, MediaPlayerActivity.class);
                intent.putExtra("id", this.mId);
                intent.putExtra("url", this.mURL);
                intent.putExtra("playlist", "Unknown");
                intent.putExtra("playlistid", 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "XML bad format received");
        }
    }

    public void notifyFile(Boolean success, PlayItem item) {
        if(success) {
            mDataHandler.open();
            mDataHandler.addPlayItem(item);
            mDataHandler.close();
        } else {
            Log.w(LOG_TAG, "Unable to create file: " + item.getFile());
        }
    }
}
