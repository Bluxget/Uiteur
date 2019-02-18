package net.bluxget.uiteur.task;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.CookieManager;

import net.bluxget.uiteur.service.MediaPlayerService;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Auth task
 *
 * @author Jonathan B.
 */
public class AuthTask extends AsyncTask<URL, Void, Long> {

    private static final String LOG_TAG = AuthTask.class.getSimpleName();

    private MediaPlayerService mService = null;
    private Document mDocument = null;

    public AuthTask(MediaPlayerService service) {
        this.mService = service;
    }

    protected Long doInBackground(URL... urls) {
        URL serviceURL = urls[0];

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) serviceURL.openConnection();

            Log.d(LOG_TAG, "Connection OK");
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

        connection.setDoOutput(true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(5000);

        CookieManager cookieManager = CookieManager.getInstance();
        String cookieUiteur = cookieManager.getCookie("http://uiteur.struct-it.fr/");
        if (cookieUiteur != null) {
            connection.setRequestProperty("Cookie", cookieUiteur);
        }

        try {
            connection.connect();
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get("Set-Cookie");

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        cookieManager.setCookie("http://uiteur.struct-it.fr/", cookie);
                    }
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer buffer = new StringBuffer();
                String inputLine;

                while ((inputLine = reader.readLine()) != null) {
                    buffer.append(inputLine);
                }

                reader.close();

                Log.d(LOG_TAG, "Answer: " + buffer.toString());

                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                InputSource inputSource = new InputSource(new StringReader(buffer.toString()));
                this.mDocument = documentBuilder.parse(inputSource);
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

        return (long) 0;
    }

    protected void onPostExecute(Long result) {
        this.mService.playlist(this.mDocument);
    }
}
