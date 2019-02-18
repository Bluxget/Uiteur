package net.bluxget.uiteur.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import net.bluxget.uiteur.service.MySensorService;

/**
 * Location listener
 *
 * @author Jonathan B
 */
public class MyLocationServiceListener implements LocationListener {

    private MySensorService service;

    public MyLocationServiceListener(MySensorService service) {
        this.service = service;
    }

    public void onLocationChanged(Location location) {
        this.service.notifyLocation(location);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }
}
