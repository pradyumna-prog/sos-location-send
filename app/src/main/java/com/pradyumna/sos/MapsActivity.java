package com.pradyumna.sos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Permission;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000; // it is in miliseconds
    private final float MIN_DISTANCE = 5;
    private LatLng latLng;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.SEND_SMS,Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("You are here, "+location.getLatitude() +", "+location.getLongitude()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    sendMessage(location);

                }catch (SecurityException e) {e.printStackTrace();}
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        }catch (SecurityException e){
            e.printStackTrace();
        }

    }

    private void sendMessage(Location location){
        //accquiring phone number

        Bundle extras = getIntent().getExtras();
        phoneNumber = extras.getString("PhoneNumber");

        Toast.makeText(getApplicationContext(), "Sending Message to "+phoneNumber, Toast.LENGTH_SHORT).show();

        if(phoneNumber != null){
            String lat = String.valueOf(location.getLatitude());
            String lng = String.valueOf(location.getLongitude());
            StringBuffer message = new StringBuffer();
            message.append("Latitude = ");message.append(lat);
            message.append("\nLongitude = ");message.append(lng);
            message.append("\nhttps://google.com/maps?q="+lat+","+lng);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message.toString(), null, null);
            Toast.makeText(getApplicationContext(), "Location Sent Successfully", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Failed to Send Location", Toast.LENGTH_LONG).show();
        }

    }

}
