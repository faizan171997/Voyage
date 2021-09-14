package shree.voyagemain;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rahul on 24/09/17.
 */

public class PostPanaDir extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleApiClient.ConnectionCallbacks {

    private final static int PERMISSION_REQUEST = 1;
    public static final String TAG = PostPanaDir.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    FirebaseUser user;
    String[] jsonData;

    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Group");
    DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference().child("Group");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Groups");

    private GoogleMap mMap;
    double lat = 0;
    double lng = 0, origLat = 0, origLng = 0;

    String gname = "", addr = "";

    TextView tv4;
    Button backbut, nextbu;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        setContentView(R.layout.postpanad);





        Bundle b = getIntent().getExtras();
        origLat = b.getDouble("origlat");
        origLng = b.getDouble("origlng");
        gname = b.getString("gname");
        addr = b.getString("address");

        setUpMapIfNeeded();

        user = FirebaseAuth.getInstance().getCurrentUser();
        tv4 = (TextView) findViewById(R.id.entrymaptext);
        backbut = (Button) findViewById(R.id.backbut);
        nextbu = (Button) findViewById(R.id.nextbu);

        nextbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr = dr.child(gname).child("details");
                dr1 = dr1.child(gname);
                GroupData gd = new GroupData(gname, origLng, origLat, user.getDisplayName(),user.getEmail(),user.getUid());
                ref2.push().setValue(gd);
                dr.push().setValue(gd);
                Toast.makeText(getApplicationContext(),"Group "+gname+" Created",Toast.LENGTH_SHORT).show();
                dr1.child("users").push().setValue(new UserData(user.getDisplayName(),user.getUid(),user.getEmail()));

                FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("groups").push().setValue(gd);


                Intent i=new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


            }
        });

        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {

        //Log.d(TAG, location.toString());


        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        //Glat.push().setValue(currentLatitude);
        //Glog.push().setValue(currentLongitude);

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        CircleOptions options = new CircleOptions().center(latLng);
        setUpMapIfNeeded();
        if (mMap != null) {
            //mMap.addMarker(options);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }

        LatLng origin = latLng;
        LatLng dest = new LatLng(origLat, origLng);

// Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();

// Start downloading json data from Google Directions API
        downloadTask.setMap(mMap, dest);

        downloadTask.execute(url);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
//             Try to obtain the map from the SupportMapFragment.
//            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
            mapFrag.getMapAsync(this);

            // Check if we were successful in obtaining the map.



        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        /*
        LatLng ll = new LatLng(lat,lng);

        MarkerOptions me = new MarkerOptions().position(ll).title("me");
        mMap.addMarker(me);*/
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){


        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }
    /*

    public void setDownloadedData(String... d){
        jsonData =d;
        JSONObject jObject;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        String distance = "", duration = "";

        try {
            jObject = new JSONObject(jsonData[0]);
            jRoutes = jObject.getJSONArray("routes");
            jLegs = ( (JSONObject)jRoutes.get(0)).getJSONArray("legs");

            distance = (String)((JSONObject)((JSONObject)jLegs.get(0)).get("distance")).get("text");
            duration = (String)((JSONObject)((JSONObject)jLegs.get(0)).get("duration")).get("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            //tv4.setText("ETA : " + distance + ", " + duration);
        }catch(Exception e){
            Log.d("ERROR", e.toString());
        }

        //Toast.makeText(getApplicationContext(),"ETA : " + distance + ", " + duration, Toast.LENGTH_LONG).show();

        Log.d("Distance" ,distance);
        Log.d("Duration", duration);
    }

    private void downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            Log.d("PreJSON",data);

            br.close();

        }catch(Exception e){
            Log.d("Exception in url", e.toString());
        }finally{
            //iStream.close();
            urlConnection.disconnect();
        }

        //ppd.setDownloadedData(data);
        setDownloadedData(data);
    }*/
}


