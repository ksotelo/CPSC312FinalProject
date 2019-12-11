package com.example.restaurantreviewandfind;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final int MY_LOCATION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        latitude= intent.getDoubleExtra("latitude",0);
        longitude=intent.getDoubleExtra("longitude",0);
        restaurantList = fillList(intent);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("TEST", "MAP READY");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        LatLng curLocation = new LatLng(latitude,longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(curLocation,15.0f);
        setPin(latitude,longitude, null);
        mMap.moveCamera(cameraUpdate);
        for(int i = 0; i < restaurantList.size(); i++){
            double lat = restaurantList.get(i).getLatitude();
            double lng = restaurantList.get(i).getLongitude();
            setPin(lat, lng, restaurantList.get(i));
        }
        //Check we have the users permission to access their location
        // if we dont have it, we have to request it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);

        } else {
            //we dont have permission, so we have to ask for it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
            //run asychronously.. show an alert dialog
            //user can choose allow or deny
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_LOCATION_REQUEST_CODE){
            if(permissions.length == 1 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //we got the users permission, they said allow!
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    //sets a pin
    public void setPin(double latitude, double longitude, Restaurant restaurant){

        LatLng latLng = new LatLng(latitude,longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        if (restaurant != null) {
            markerOptions.title(restaurant.getName());
            if (restaurant.acceptsBulldogBucks()) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
        }
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);
    }

    public List<Restaurant> fillList(Intent intent){
        Log.d("TEST", "FILLING LIST");
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        ArrayList<String> placeIds = intent.getStringArrayListExtra("placeIds");
        ArrayList<String> names = intent.getStringArrayListExtra("names");
        ArrayList<String> addresses = intent.getStringArrayListExtra("addresses");
        //ArrayList<String[]> restaurantHours = intent.getStringArrayListExtra(
        ArrayList<String> websites = intent.getStringArrayListExtra("websites");
        ArrayList<String> priceLevels = intent.getStringArrayListExtra("priceLevels");
        boolean[] bulldogBucks = intent.getBooleanArrayExtra("bulldogBucks");
        double[] latitudes = intent.getDoubleArrayExtra("latitudes");
        double[] longitudes = intent.getDoubleArrayExtra("longitudes");


        Log.d("TEST", "CREATED LISTS");
        int length = placeIds.size();
        for(int i = 0; i < length; i++){
            Restaurant newRestaurant = new Restaurant(placeIds.get(i));
            newRestaurant.setName(names.get(i));
            newRestaurant.setAddress(addresses.get(i));
            newRestaurant.setWebsite(websites.get(i));
            //Log.d("TEST", "PRICELEVEL:" + );
            newRestaurant.setPriceLevel(priceLevels.get(i));
            newRestaurant.setLatitude(latitudes[i]);
            newRestaurant.setLongitude(longitudes[i]);
            restaurants.add(newRestaurant);
        }
        Log.d("TEST", "FILLED LISTS");
        Log.d("TEST", "" + restaurants);
        return restaurants;
    }
}