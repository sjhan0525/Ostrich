package com.e.ostrich;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSService extends Service {
    public GPSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");


    } @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(GPSService.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 100, gpsLocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, gpsLocationListener);
        }
        return START_REDELIVER_INTENT;
    }
    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            double latitude, longitude;
            final String nowAddress;

            latitude=location.getLatitude();
            longitude=location.getLongitude();

            Geocoder mGeoCoder = new Geocoder(GPSService.this, Locale.KOREA);
            List<Address> address;

            try{
                if(mGeoCoder !=null){
                    address = mGeoCoder.getFromLocation(latitude, longitude, 1);
                    if (address != null && address.size() > 0) {
                        String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                        nowAddress = currentLocationAddress;

                        Log.d("위치", String.valueOf(latitude) + ", " + String.valueOf(longitude));
                        Log.d("주소 ", nowAddress);
                        Toast.makeText(GPSService.this, String.valueOf(latitude) + ", " + String.valueOf(longitude) + ", " + nowAddress, Toast.LENGTH_SHORT).show();

                      Intent intent = new Intent(GPSService.this, MainActivity.class);
                      intent.putExtra("data", "TEST");
                      startActivity(intent);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
