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
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import net.bluxget.uiteur.listener.MyLocationServiceListener;
import net.bluxget.uiteur.listener.MySensorServiceListener;
import net.bluxget.uiteur.task.HTTPRequestTask;

import java.util.Arrays;

/**
 * Sensor & Location service
 *
 * @author Jonathan B
 */
public class MySensorService extends Service {

    private static final String LOG_TAG = HTTPRequestTask.class.getSimpleName();

    public final static String ACTION_SENSOR_TEMPERATURE = "sensor_temperature";
    public final static String ACTION_SENSOR_LIGHT = "sensor_light";
    public final static String ACTION_SENSOR_ACCELEROMETER = "sensor_accelerometer";
    public final static String ACTION_SENSOR_GYROSCOPE = "sensor_gyroscope";
    public final static String ACTION_SENSOR_LOCATION = "sensor_location";

    private int[] sensors = {Sensor.TYPE_GYROSCOPE};

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                intent.setAction(MySensorService.ACTION_SENSOR_TEMPERATURE);
                intent.putExtra("degrees", event.values[0]);
                break;
            case Sensor.TYPE_LIGHT:
                intent.setAction(MySensorService.ACTION_SENSOR_LIGHT);
                intent.putExtra("lux", event.values[0]);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                intent.setAction(MySensorService.ACTION_SENSOR_ACCELEROMETER);
                intent.putExtra("x", event.values[0]);
                intent.putExtra("y", event.values[1]);
                intent.putExtra("z", event.values[2]);
                break;
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
