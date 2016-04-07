package com.example.user.mappane;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
//import android.location.LocationListener;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapPane extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // public LatLng sydney;
    static Double lon;
    static Double latt;
    static Location y;
    static Double f, g;
    private GoogleMap mMap;
    private final String Tag = "BenzinebApp";
    private TextView Text;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Cursor c=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_pane);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
      DataBaseHelper myDbHelper = new DataBaseHelper(MapPane.this);


        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            try {
                throw sqle;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Toast.makeText(MapPane.this, "success", Toast.LENGTH_SHORT).show();
        c = myDbHelper.query("agency", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Toast.makeText(MapPane.this,
                        "fare_id=" + c.getString(0)+
                                "route_id=" + c.getString(1)
                               /* "origin=" + c.getString(2)+
                                "currency_type=" + c.getString(3)+
                                "payment_method=" + c.getString(4)+
                                     "transfers=" + c.getString(5)*/
                                     /*  "transfers=" + c.getString(6)+
                                        "transfers=" + c.getString(7)+
                                               "transfers=" + c.getString(8)*/
                        , Toast.LENGTH_LONG).show();
            } while (c.moveToNext());

        }
    }






    @Override
    public void onLocationChanged(Location location) {

        lon = location.getLongitude();
        latt = location.getLatitude();
        Toast.makeText(this, "longitude" + lon + "lattitude" + latt, Toast.LENGTH_LONG).show();
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       List<LatLng> directionPoint=new ArrayList<LatLng>();
        LatLng gare = new LatLng(35.5008333078298, 11.0644082609385);
        LatLng Mahdia = new LatLng(35.5008333078298, 11.0644082609385);
        directionPoint.add(gare);
        directionPoint.add(Mahdia);

        Marker marker =mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tr))
                .position(gare)
                .flat(true)
                .rotation(320));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(gare)
                .zoom(20)
                .bearing(90)
                .build();

        // Animate the change in camera view over 2 seconds
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(35.500863, 11.064995), 13));
      //  animateMarker(mMap,marker, (ArrayList) directionPoint,false);
        mMap.addMarker(new MarkerOptions()
                .title("La Gare")
                .snippet("The most wonderful.")
                .position(Mahdia));
        mMap.addMarker(new MarkerOptions()
                .title("EZZAHRA")
                .snippet("The most wonderful.")
                .position(new LatLng(35.5000434191629, 11.0483183619427)));
        mMap.addMarker(new MarkerOptions()
                .title("borj arif")
                .snippet("The most wonderful.")
                .position(new LatLng(35.5061928462048, 11.0303670358646)));
        mMap.addMarker(new MarkerOptions()
                .title("sidi messaoud")
                .snippet("The most wonderful.")
                .position(new LatLng(35.5210715684533, 11.02721426692)));
        // Polylines are useful for marking paths and routes on the map.
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(35.5008333078298, 11.0644082609385))  // La gare
                .add(new LatLng(35.496206, 11.059541))
                .add(new LatLng(35.5000434191629, 11.0483183619427))  // EZZAHRA
                .add(new LatLng(35.5061928462048, 11.0303670358646))  // borj arif
                .add(new LatLng(35.5210715684533, 11.02721426692))  // sidi massaoud
                .add(new LatLng(35.536965, 11.027438))  // Mahdia ZT

                .add(new LatLng(35.570263, 11.017860))  // Baghdadi
                .add(new LatLng(35.615490, 10.989364))  // Bekalta
                .add(new LatLng(35.638043, 10.961635))  // Teboulba
                .add(new LatLng(35.634067, 10.943520))//Teboulba Z.I
                .add(new LatLng(35.631642, 10.915663))//Moknine
                .add(new LatLng(35.638110, 10.907206))//Moknine Gribaa
                .add(new LatLng(35.645657, 10.900740))//K.Helal
                .add(new LatLng(35.666809, 10.888635))//Sayyada
                .add(new LatLng(35.672467, 10.867080))//Bouhdjar
                .add(new LatLng(35.680144, 10.842539))//Ksiba
                .add(new LatLng(35.709114, 10.812092))//khnis
                .add(new LatLng(35.731192, 10.812589))//Frina
                .add(new LatLng(35.740075, 10.820714))//Monastir Z.I
                .add(new LatLng(35.760395, 10.807117))//La Facult�
                .add(new LatLng(35.770921, 10.825613))//La gare El Monastir
                .add(new LatLng(35.760395, 10.807117))//La Facult�

                .add(new LatLng(35.752273, 10.794700))//foret
                .add(new LatLng(35.757845, 10.787834))//foret
                .add(new LatLng(35.769546, 10.787491))//foret
                .add(new LatLng(35.772192, 10.781654))//foret
                .add(new LatLng(35.762802, 10.754043))//L'a�roport
                .add(new LatLng(35.760155, 10.745117))//Les hotels
                .add(new LatLng(35.757369, 10.716277))//Sahline Sabkha
                .add(new LatLng(35.782301, 10.668212))//Sousse Z.I
                .add(new LatLng(35.792744, 10.655338))//Depot
                .add(new LatLng(35.800846, 10.649651))//Sousse Sud
                .add(new LatLng(35.816052, 10.642952))//Med V
                .add(new LatLng(35.822787, 10.641704))//beb Jdid

        );
    }
  /*  public static void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final Bitmap bitmap) {


        Marker marker = myMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoint.get(0))
                .flat(true));

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), 10));

        animateMarker(myMap, marker, directionPoint, false);
    }*/


 /*   public  void animateMarker(GoogleMap mMap,final  Marker marker,final ArrayList directionPoint,
                                      final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        final long duration = 30000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                if (i < directionPoint.size())
                    marker.setPosition((LatLng) directionPoint.get(i));
                i++;


                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }

        });
    }
*/
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Toast.makeText(this, "client connect�", Toast.LENGTH_SHORT).show();
    }

    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

}