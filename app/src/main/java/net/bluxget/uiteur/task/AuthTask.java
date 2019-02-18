package net.bluxget.uiteur.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class AuthTask extends AsyncTask<URL, Void, Long> {

    protected Long doInBackground(URL... urls) {
        URL serviceURL = urls[0];

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) serviceURL.openConnection();

            Log.d("test", "Connection OK");
        } catch (IOException ex) {
            Log.e("test", ex.getMessage());
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            Log.e("test", ex.getMessage());
        }

        connection.setDoOutput(true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(5000);

        try {
            connection.connect();
        } catch (IOException ex) {
            Log.e("test", ex.getMessage());
        }

        try {
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("test", "OK");
            }
        } catch (IOException ex) {
            Log.e("test", ex.getMessage());
        }

        return (long) 0;
    }

    protected void onPostExecute(Long result) {
        Log.d("test", "OK");
    }
}
