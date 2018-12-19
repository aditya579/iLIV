package com.example.women_safety_24.iliv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Location location;
    private GoogleMap mMap;
    //--location service
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private int count = 0;

    //------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Toolbar myToolbar = findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        //myToolbar.setLogo(R.drawable.i_liv);

        Intent intent = getIntent();
        String data = String.valueOf(intent.getData());
        TextView tv1 = findViewById(R.id.textView);
        tv1.setText("hello");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        Button bttn = findViewById(R.id.bttn1);
        bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMarker1(mMap, location.getLatitude(), location.getLongitude());

            }
        });

    }

    //--------------------------------------------------------------------------------------------------------------------------------------
    //location services
    @Override
    public void onResume() {
        mGoogleApiClient.connect();
        super.onResume();
    }

    public void onPause() {
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    /* permission check code
    */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            /*   ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,int[] grantResults)
            //  ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
            //          Integer.parseInt(Manifest.permission.ACCESS_COARSE_LOCATION));
            // to handle the case where the user grants the permission. See the documentation
             for ActivityCompat#requestPermissions for more details.
           */
            final int REQUEST_LOCATION = 2;

            //String[] permissions = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);

            return;
        }else {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if(location==null){
             Log.d(TAG,"some error");

        }

        else
            {
                    TextView tv1 = findViewById(R.id.text1);
                    tv1.setText("LATITUDE: "+location.getLatitude()+"    "+"LONGITUDE: "+location.getLongitude());
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
//--------------------------------------------------------map activity--------------------------------------------------------------------------------
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
        mMap.setMapType(googleMap.MAP_TYPE_HYBRID);
        // Add a marker in Sydney and move the camera
        LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLong).title("Your location"));
        float zoom = 18.00f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, zoom));

    }
//----------------------------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------marker addition-------------------------------------------------------------------------------------------
    static void addMarker1(GoogleMap gmap, double lat,double lng){

        LatLng latLong1 = new LatLng(lat,lng);
        gmap.addMarker(new MarkerOptions().position(latLong1).title("PROBLEM"));
        float zoom = 18.00f;
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong1, zoom));

    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------
//------------------------incident reporting on key_press----------------------------------------------------------------------------------------------------------
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            count++;
            if (count == 2){
                System.out.print("recorded");
                count =0;
                addMarker1(mMap,location.getLatitude(),location.getLongitude());
            }
                   }
                   return true;
      }
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------incident updating function---------------------------------------------------------------------------------------------


}
