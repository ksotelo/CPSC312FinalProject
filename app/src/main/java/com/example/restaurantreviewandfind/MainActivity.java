/**
 * This program holds the main home app functionality.
 * CPSC 312-02, Fall 2019
 * Final Project
 * Picture used in main xml: https://en.wikipedia.org/wiki/Gonzaga_University
 *
 * @author Kat Sotelo and Anna Smith
 * @version v1.0 12/11/19
 */



package com.example.restaurantreviewandfind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.restaurantreviewandfind.PlacesAPI.API_KEY;
import static com.example.restaurantreviewandfind.R.id.spinner3;
import static com.example.restaurantreviewandfind.R.id.start;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MyFirebase";
    static final int SIGN_IN_REQUEST = 1;
    static final int LOCATION_REQUEST_CODE = 3;
    private double latitude;
    private double longitude;
    private boolean searchActivityActivated = false;
    static final String API_KEY = "AIzaSyCFY_urP6VDBIWwsYFtKg2B8c-L6lirwXo";
    String userName = "Anonymous";
    private FusedLocationProviderClient myFusedLocationProviderClient;

    private FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference reviewDatabaseReference;
    ChildEventListener myReviewChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFirebase();

        Spinner priceSpinner = (Spinner) findViewById(spinner3);
        String[] types = getResources().getStringArray(R.array.priceArray);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, types);
        priceSpinner.setAdapter(arrayAdapter);


        myFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        setupLastKnownLocation();
        setupUserLocationUpdates();

        // Initialize Places.
        Places.initialize(getApplicationContext(), API_KEY);

        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchActivityActivated = true;
                CheckBox bulldogBucksCheck = (CheckBox) findViewById(R.id.bulldogBucksCheck);
                CheckBox openNowCheck = (CheckBox) findViewById(R.id.openNowCheck);
                Spinner spinner = (Spinner) findViewById(spinner3);
                String price = spinner.getSelectedItem().toString();
                boolean wantBulldogBucks = bulldogBucksCheck.isChecked();
                boolean wantOpenNow = openNowCheck.isChecked();
                EditText keywordEditText = (EditText) findViewById(R.id.editText2);
                String keywords = keywordEditText.getText().toString();
                Log.d("TEST", "STARTING SEARCH" );

                PlacesAPI placesAPI = new PlacesAPI(MainActivity.this);
                placesAPI.fetchPlaces(latitude, longitude, keywords, getPriceLevel(price), wantBulldogBucks, wantOpenNow);
            }
        });

        ImageButton mapButton = (ImageButton) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchActivityActivated = false;
                Log.d("TEST", "STARTING SEARCH" );
                CheckBox bulldogBucksCheck = (CheckBox) findViewById(R.id.bulldogBucksCheck);
                CheckBox openNowCheck = (CheckBox) findViewById(R.id.openNowCheck);
                Spinner spinner = (Spinner) findViewById(spinner3);
                String price = spinner.getSelectedItem().toString();
                boolean wantBulldogBucks = bulldogBucksCheck.isChecked();
                boolean wantOpenNow = openNowCheck.isChecked();

                EditText keywordEditText = (EditText) findViewById(R.id.editText2);
                String keywords = keywordEditText.getText().toString();
                Log.d("TEST", "STARTING SEARCH" );
                PlacesAPI placesAPI = new PlacesAPI(MainActivity.this);
                placesAPI.fetchPlaces(latitude, longitude, keywords, getPriceLevel(price), wantBulldogBucks, wantOpenNow);

            }
        });

    }

    public void receivedRestaurant(String resultStr){


    }

    public void receivedRestaurantSearch(List<Restaurant> restaurants) {
        if(restaurants.size() == 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("No Places Found")
                    .setMessage("No places with those requirements found in your area. Try entering different parameters.")
                    .setPositiveButton("Ok", null)
                    .setNegativeButton("Back", null);
            alertDialog.show();
        } else {
            ArrayList<String> placeIds = new ArrayList<String>();
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<String> addresses = new ArrayList<String>();
            ArrayList<String[]> restaurantHours = new ArrayList<String[]>();
            ArrayList<String> websites = new ArrayList<String>();
            ArrayList<String> priceLevels = new ArrayList<String>();
            boolean[] bulldogBucks = new boolean[restaurants.size()];
            //ArrayList<Boolean> bulldogBucks = new ArrayList<Boolean>();
            double[] latitudes = new double[restaurants.size()];
            double[] longitudes = new double[restaurants.size()];

            for (int i = 0; i < restaurants.size(); i++) {
                placeIds.add(restaurants.get(i).getPlaceId());
                names.add(restaurants.get(i).getName());
                addresses.add(restaurants.get(i).getAddress());
                restaurantHours.add(restaurants.get(i).getHours());
                websites.add(restaurants.get(i).getWebsite());
                priceLevels.add(restaurants.get(i).getPriceLevel());
                bulldogBucks[i] = (restaurants.get(i).acceptsBulldogBucks());
                latitudes[i] = restaurants.get(i).getLatitude();
                longitudes[i] = restaurants.get(i).getLongitude();
            }

            if (searchActivityActivated) {
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                searchIntent.putStringArrayListExtra("placeIds", placeIds);
                searchIntent.putStringArrayListExtra("names", names);
                searchIntent.putStringArrayListExtra("addresses", addresses);
                //searchIntent.putStringArrayListExtra("restaurantHours", restaurantHours);
                searchIntent.putStringArrayListExtra("websites", websites);
                searchIntent.putStringArrayListExtra("priceLevels", priceLevels);
                searchIntent.putExtra("bulldogBucks", bulldogBucks);
                Log.d("TEST", "FINISHED SEARCH, STARTING ACTIVITY");
                Log.d("TEST", "" + names);

                startActivity(searchIntent);
            } else {
                Intent searchIntent = new Intent(MainActivity.this, MapsActivity.class);
                searchIntent.putStringArrayListExtra("placeIds", placeIds);
                searchIntent.putStringArrayListExtra("names", names);
                searchIntent.putStringArrayListExtra("addresses", addresses);
                //searchIntent.putStringArrayListExtra("restaurantHours", restaurantHours);
                searchIntent.putStringArrayListExtra("websites", websites);
                searchIntent.putStringArrayListExtra("priceLevels", priceLevels);
                searchIntent.putExtra("bulldogBucks", bulldogBucks);
                searchIntent.putExtra("latitudes", latitudes);
                searchIntent.putExtra("longitudes", longitudes);
                searchIntent.putExtra("latitude", latitude);
                searchIntent.putExtra("longitude", longitude);

                Log.d("TEST", "FINISHED SEARCH, STARTING ACTIVITY");
                Log.d("TEST", "" + names);

                startActivity(searchIntent);
            }
        }
    }

    public int getPriceLevel(String price){
        if(price.equals("$$$$")) {
            return 4;
        } else if (price.equals("$$$")) {
            return 3;
        } else if (price.equals("$$")) {
            return 2;
        } else if (price.equals("$")) {
            return 1;
        } else {
            return -1;
        }
    }



    private void setupLastKnownLocation(){
        // requests permissions if we don't already have it
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            Task<Location> locationTask = myFusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d(TAG, "location: " + latitude + ", " + longitude);

                        Log.d(TAG, "curr location: " + latitude + ", " + longitude);
                    }
                }
            });
        }
    }

    private void setupUserLocationUpdates(){
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // requests an update every 10 seconds
        locationRequest.setFastestInterval(5000); // handle at most updates every 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                LocationCallback locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Log.d(TAG, "onLocationResult: ");
                        for (Location location : locationResult.getLocations()) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d(TAG, "onSuccess: " + latitude + ", " + longitude);                        }
                    }
                };
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_REQUEST_CODE);
                } else {
                    Log.d(TAG, "onSuccess: We have the user's permission");
                    myFusedLocationProviderClient.requestLocationUpdates(locationRequest,
                            locationCallback, null);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // this method executes when the user responds to the permissions dialog
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // we have the user's permission!!
                setupLastKnownLocation();
            }
        }
    }


    private void setupFirebase(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        reviewDatabaseReference = mFirebaseDatabase.getReference().child("review");
        myReviewChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: " + s);
                Review review = dataSnapshot.getValue(Review.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setupUserSignedIn(user);
                } else {
                    Intent intent = AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build())).build();
                    startActivityForResult(intent, SIGN_IN_REQUEST);
                }
            }
        };
    }


    private void setupUserSignedIn(FirebaseUser user) {
        // get the user's name
        userName = user.getDisplayName();
        // listen for database changes with childeventlistener
        // wire it up!
        reviewDatabaseReference.addChildEventListener(myReviewChildEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "You are now signed in", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    //Removes the authstate listeners
    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    //automatically called because you are overriding a method from ur super class
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_icon,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //override a callback that executes when the user presses a menu action(item)
    //kind of like an onbutton click. executes when an option is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.logOutButton:
                FirebaseAuth.getInstance().signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
