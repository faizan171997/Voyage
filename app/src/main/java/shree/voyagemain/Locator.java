package shree.voyagemain;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Config;
import android.widget.Toast;

import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by rahul on 17/09/17.
 */



public class Locator{

    private String provider;
    private String mProviderName;
    private LocationManager locManager;
    private Location lastLocation;






    private final LocationListener locListener = new LocationListener() {
        public void onLocationChanged(Location loc) {
            updateLocation(loc);
        }
        public void onProviderEnabled(String provider) {
            updateLocation();
        }
        public void onProviderDisabled(String provider) {
            updateLocation();
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void updateLocation() {
        // Trigger a UI update without changing the location
        updateLocation(lastLocation);
    }

    private void updateLocation(Location location) {
        boolean locationEnabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean waitingForLocation = locationEnabled && !validLocation(location);
        boolean haveLocation = locationEnabled && !waitingForLocation;




        if (haveLocation) {


            lastLocation = location;
        }
    }

    private double getLatitude(Location location) {
        return location.getLatitude();
    }

    private double getLongitude(Location location) {
        return location.getLongitude();
    }

    private boolean validLocation(Location location) {
        if (location == null) {
            return false;
        }

        // Location must be from less than 30 seconds ago to be considered valid
        if (Build.VERSION.SDK_INT < 17) {
            return System.currentTimeMillis() - location.getTime() < 30e3;
        } else {
            return SystemClock.elapsedRealtime() - location.getElapsedRealtimeNanos() < 30e9;
        }

    }

}
