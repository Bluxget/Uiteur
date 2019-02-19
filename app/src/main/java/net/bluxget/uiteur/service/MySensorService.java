package net.bluxget.uiteur.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import net.bluxget.uiteur.listener.MyLocationServiceListener;
import net.bluxget.uiteur.listener.MySensorServiceListener;

/**
 * Sensor & Location service
 *
 * @author Jonathan B
 */
public class MySensorService extends Service {

    private static final String LOG_TAG = MySensorService.class.getSimpleName();

    public final static String ACTION_SENSOR_GYROSCOPE = "sensor_gyroscope";
    public final static String ACTION_SENSOR_LOCATION = "sensor_location";

    private int[] sensors = {Sensor.TYPE_GYROSCOPE};

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        MySensorServiceListener sensorServiceListener = new MySensorServiceListener(this);

        for (int sensor : this.sensors) {
            sensorManager.registerListener(sensorServiceListener, sensorManager.getDefaultSensor(sensor), SensorManager.SENSOR_DELAY_NORMAL);
        }

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        MyLocationServiceListener locationServiceListener = new MyLocationServiceListener(this);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationServiceListener);
        } catch (SecurityException ex) {
            Log.e(LOG_TAG, ex.getMessage());
        }

        return START_STICKY;
    }

    public void notifySensor(SensorEvent event) {
        Intent intent = new Intent();

        switch (event.sensor.getType()) {
            case Sensor.TYPE_GYROSCOPE:
                intent.setAction(MySensorService.ACTION_SENSOR_GYROSCOPE);
                intent.putExtra("x", event.values[0]);
                intent.putExtra("y", event.values[1]);
                intent.putExtra("z", event.values[2]);
                break;
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void notifyLocation(Location location) {
        Intent intent = new Intent(ACTION_SENSOR_LOCATION);

        intent.putExtra("latitude", location.getLatitude());
        intent.putExtra("longitude", location.getLongitude());
        intent.putExtra("altitude", location.getAltitude());

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
