package com.example.authenticationapp.OwnerInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authenticationapp.R;
import com.example.authenticationapp.DriverInterface.UserLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddListingMaps extends AppCompatActivity {

    private static final String TAG = "";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9001;
    private static final int ERROR_DIALOG_REQUEST = 9002;
    private static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;
    private GoogleMap mgoogleMap;
    private MapView mapView;
    boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private UserLocation mUserLocation;
    private EditText mSearchDestination;
    private LatLng currentLatLng;
    private String lotname;
    private Integer number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing_maps);

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mSearchDestination = findViewById(R.id.searchDestination);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


    }

    private void init(){
        Log.d(TAG, "init: initializing");
        mSearchDestination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute method for searching
                    geoLocate();
                }
                return false;
            }
        });

    }

    private void geoLocate(){
        String searchString = mSearchDestination.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException" + e.getMessage() );
        }
        if(list.size()>0){
            Address address = list.get(0);

            //Toast.makeText(getActivity(), address.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), 15,address.getAddressLine(0));

        }
    }
    private void moveCamera(LatLng latLng, float zoom, String title){
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title)
                .draggable(true);

        mgoogleMap.addMarker(options);


    }

    private void getUserDetails(){
        if(mUserLocation == null){
            mUserLocation = new UserLocation();

            DocumentReference userRef = fStore.collection("users").document(userID);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: successfully got the user details.");
                        getLastKnownLocation();

                    }
                }
            });
        }
    }

    private void saveUserLocation(){
        if(mUserLocation != null){
            DocumentReference locationRef = fStore.collection("User Locations").document(userID);

            locationRef.set(mUserLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "saveUserLocation: \n inserted user location into database."
                                + "\n latitude: " + mUserLocation.getGeo_point().getLatitude()
                                + "\n longitude: " + mUserLocation.getGeo_point().getLongitude());
                    }
                }
            });
        }
    }

    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation: called");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Location location = task.getResult();
                    currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                    mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,13));
                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "onComplete: latitude: " + geoPoint.getLatitude());
                    Log.d(TAG, "onComplete: longitude: " + geoPoint.getLongitude());

                    mUserLocation.setGeo_point(geoPoint);
                    mUserLocation.setTimestamp(null);
                    saveUserLocation();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mgoogleMap = googleMap;
        mgoogleMap.setMyLocationEnabled(true);

        //function to get last known location
        getLastKnownLocation();
        //initialize search bar
        init();

        //Add marker on click and save data to database

        mgoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

                DocumentReference userRef = fStore.collection("users").document(userID);

                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            lotname = documentSnapshot.getString("Lot name");
                            String slot = documentSnapshot.getString("Exact number of slots");
                            List<String> cartype = (List<String>) documentSnapshot.get("Supported cars");
                            //String lotdescription = documentSnapshot.getString("lot description");
                            String address = documentSnapshot.getString("Address");
                            String start = documentSnapshot.getString("Starting Time");
                            String end = documentSnapshot.getString("End Time");
                            String contactinfo = documentSnapshot.getString("Phone");

                            DocumentReference locationRef = fStore.collection("Garage Locations").document(lotname);
                            Map<String, Object> garageInfo = new HashMap<>();
                            garageInfo.put("Garage name", lotname);
                            garageInfo.put("Slots available",slot);
                            garageInfo.put("Supported cars",cartype);
                            garageInfo.put("Address",address);
                            garageInfo.put("Starting Time",start);
                            garageInfo.put("End Time",end);
                            garageInfo.put("Phone",contactinfo);

                            locationRef.set(garageInfo , SetOptions.merge());

                            DocumentReference docRef2 = fStore.collection("users").document(userID);
                            docRef2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String slot = documentSnapshot.getString("Exact number of slots");
                                    String value = "1";
                                    try {
                                        number = Integer.parseInt(slot);

                                    } catch (NumberFormatException ex) {
                                        ex.printStackTrace();
                                    }
                                    for (int i = 1; i <= number; i++) {
                                        DocumentReference docRef = fStore.collection("Garage Locations").document(lotname);
                                        Map<String, Object> slotstatus = new HashMap<>();
                                        slotstatus.put("Slot " + i, value);

                                        docRef.set(slotstatus, SetOptions.merge());
                                    }
                                }
                            });

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude+" : " + latLng.longitude);
                mgoogleMap.clear();
                mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
                mgoogleMap.addMarker(markerOptions);

                GeoPoint geoPoint = new GeoPoint(latLng.latitude,latLng.longitude);

                DocumentReference locationRef2 = fStore.collection("Garage Locations").document(lotname);
                Map<String, Object> geopoint = new HashMap<>();
                geopoint.put("Geopoint", geoPoint);

                locationRef2.set(geopoint, SetOptions.merge());



                        }
                    }
                });


            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(checkMapServices()){
            if(mLocationPermissionGranted){
                mapView.getMapAsync(this::onMapReady);
                getUserDetails();
                mapView.onResume();
            }
            else{
                getLocationPermission();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mapView.getMapAsync(this::onMapReady);
            getUserDetails();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
                    getUserDetails();
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }


}