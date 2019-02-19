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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * HTTPRequestTask task, take an XML from website
 *
 * @author Jonathan B.
 */
public class HTTPRequestTask extends AsyncTask<String, Void, Boolean> {

    private static final String LOG_TAG = HTTPRequestTask.class.getSimpleName();

    private MediaPlayerService mService;
    private Document mDocument;

    public HTTPRequestTask(MediaPlayerService service) {
        this.mService = service;
        this.mDocument = null;
    }

    /**
     * Connect to website and get an XML
     *
     * @param urls i = 0 -> Website URL
     * @return true if success, false if error (who'll show in Logcat)
     */
    protected Boolean doInBackground(String... urls) {
        URL serviceURL;

        try {
            serviceURL = new URL(urls[0]);
        } catch (MalformedURLException ex) {
            Log.e(LOG_TAG, ex.getMessage());

            return false;
        }

        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) serviceURL.openConnection();
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.getMessage());

            return false;
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            Log.e(LOG_TAG, ex.getMessage());

            return false;
        }

        connection.setDoOutput(true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(5000);

        CookieManager cookieManager = CookieManager.getInstance();
        String cookieUiteur = cookieManager.getCookie(urls[0]);
        if (cookieUiteur != null) {
            connection.setRequestProperty("Cookie", cookieUiteur);
        }

        try {
            connection.connect();
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.getMessage());

            return false;
        }

        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Map<String, List<String>> headerFields = connection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get("Set-Cookie");

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        cookieManager.setCookie(urls[0], cookie);
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

            return false;
        }

        return true;
    }

    protected void onPostExecute(Boolean result) {
        if(result) {
            this.mService.playlist(this.mDocument);
        }
    }
}
