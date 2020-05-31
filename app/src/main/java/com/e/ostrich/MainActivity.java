package com.e.ostrich;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    int ALL_PERMISSIONS = 100;
    TextView adText;
    BroadcastReceiver broadcastReceiver;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    adText = findViewById(R.id.address);

    permission();
    GPS();
//        Intent serviceIntent = new Intent(getApplicationContext(), GPSService.class);
//        startService(serviceIntent);

    popUp();
}

//권한 받기
public void permission(){
    final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS);
}

//GPS 기능 구현
public void GPS(){
    final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

 if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
 ==PackageManager.PERMISSION_GRANTED) {
     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 100, gpsLocationListener);
     locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, gpsLocationListener);
 }
}
    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            double latitude, longitude;
            final String nowAddress;

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //좌표 기반 주소값
            Geocoder mGeoCoder = new Geocoder(MainActivity.this, Locale.KOREA);
            List<Address> address;

            map(latitude, longitude);

            try {
                if (mGeoCoder != null) {
                    address = mGeoCoder.getFromLocation(latitude, longitude, 1);
                    if (address != null && address.size() > 0) {
                        String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                        nowAddress = currentLocationAddress;

                        Log.d("위치", String.valueOf(latitude) + ", " + String.valueOf(longitude));
                        Log.d("주소 ", nowAddress);
                        adText.setText(nowAddress);
                    }
                }
            } catch (IOException e) {
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

public void map(final double nowLat, final double nowLng){
    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng now = new LatLng(nowLat, nowLng);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(now));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(nowLat, nowLng))
                    .radius(3)
                    .strokeColor(0x000000)
                    .fillColor(Color.parseColor("#A6ed7d31"))
            );
        }
    });
}

@Override
protected  void onResume(){
    super.onResume();
        }

@Override
protected void onPause(){
    super.onPause();
    }

@Override
protected  void onDestroy(){
        super.onDestroy();
        }

public void popUp(){
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("이용수칙");
    builder.setMessage("이용수칙 적어야 해요");
    builder.setCancelable(true);
    builder.setPositiveButton(android.R.string.ok,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });



    AlertDialog alertDialog = builder.create();
    alertDialog.show();
        }
}
