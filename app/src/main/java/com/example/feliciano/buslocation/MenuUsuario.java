package com.example.feliciano.buslocation;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
import android.app.Notification;
import android.app.NotificationManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import android.location.Location;

public class MenuUsuario extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final String LOGTAG = "android-localizacion";

    private static final int PETICION_PERMISO_LOCALIZACION = 101;

    private GoogleApiClient apiClient;
    Location lastLocation;

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
                Toast.makeText(getApplicationContext(),"Se ha econtrado parada", Toast.LENGTH_LONG).show();
                showNotification("Se ha detectado una parada de autobus","Espera algo?");

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
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
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
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
        mMap.addMarker(new MarkerOptions().position(new LatLng(17.08559,-96.75498)).title("Mi ubicacion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(17.08559,-96.75498)));
        //agregarMarcador(39.2334234,-94.2334234);
    }
    public LatLng miUbicacion() {

        return new LatLng(lastLocation.getLongitude(),lastLocation.getLatitude());
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

    private void updateUI(Location loc) {
        if (loc != null) {
            //lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
            //lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
            System.out.println("latitud:"+String.valueOf(loc.getLatitude()));
            System.out.println("longitud:"+String.valueOf(loc.getLongitude()));
            agregarMarcador(loc.getLatitude(),loc.getLongitude());

        } else {
            //lblLatitud.setText("Latitud: (desconocida)");
            //lblLongitud.setText("Longitud: (desconocida)");
            System.out.println("Latitud y Longitud (desconocida)");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Servicio
        //////////////////
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }
    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}