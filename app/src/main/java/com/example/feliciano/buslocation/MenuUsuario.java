package com.example.feliciano.buslocation;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MenuUsuario extends FragmentActivity implements OnMapReadyCallback {
    private Region beacons = new Region("monitored region", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
    private BeaconManager beaconManager;

    private GoogleMap mMap;
    private Marker marcador = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //miUbicacion();

        final EditText texto = (EditText) findViewById(R.id.editText);
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                /*for(int i = 0; i < list.size(); i++) {
                    texto.setText("Beacon UUID: " + list.get(i).getProximityUUID() + " Major:" + list.get(i).getMajor() + " Minor:" + list.get(i).getMinor());
                }*/
                //texto.setText("Beacon UUID: " + list.get(0).getProximityUUID() + " Major:" + list.get(0).getMajor() + " Minor:" + list.get(0).getMinor());
                //texto.setText("Beacon: " + list);
                Toast.makeText(getApplicationContext(),"Se ha econtrado una poke parada", Toast.LENGTH_LONG).show();

                //System.out.println("Beacon "+list);
            }

            @Override
            public void onExitedRegion(Region region) {
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startMonitoring(beacons);
                } catch (Exception e) {
                }
            }
        });
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
        //17.085592920059185 - -96.7549830386878
        //LatLng ubicacion = miUbicacion();
        //mMap.addMarker(new MarkerOptions().position(ubicacion).title("Mi ubicacion"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.08559,-96.75498)).title("Mi ubicacion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(17.08559,-96.75498)));
        agregarMarcador(39.2334234,-94.2334234);
    }
    public LatLng miUbicacion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
                //return;
            }
        }
        else {
            miUbicacion();
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        System.out.println(location.getLatitude()+" - "+location.getLongitude());
        return new LatLng(location.getLatitude(),location.getLongitude());
        //actualizarUbicacion(location);
        //locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 15000, 0, locationListener);
    }
    public void agregarMarcador(double latitud, double longitude) {
        LatLng coordenadas = new LatLng(latitud, longitude);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if(marcador != null) {
            marcador.remove();
        }
        marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Yo").icon(BitmapDescriptorFactory.fromResource(R.drawable.bus2)));
        mMap.animateCamera(miUbicacion);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordenadas));
    }
}