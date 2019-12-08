package com.example.restaurantreviewandfind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MyFirebase";
    static final int SIGN_IN_REQUEST = 1;
    static final int GET_REVIEW_REQUEST = 2;
    String userName = "Anonymous";

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

        Button logOutButton = (Button)findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

    }

    private void setupFirebase(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        reviewDatabaseReference = mFirebaseDatabase.getReference().child("review");
        myReviewChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // called for each message already in our db
                // called for each new message add to our db
                // dataSnapshot stores the ChatMessage
                Log.d(TAG, "onChildAdded: " + s);
                Review review = dataSnapshot.getValue(Review.class);
                // add it to our list and notify our adapter
//                chatMessageList.add(chatMessage);
//                arrayAdapter.notifyDataSetChanged();
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

        Log.d(TAG, "Request Code: "+requestCode);
        if (requestCode == GET_REVIEW_REQUEST) {
            Log.d(TAG, "In getReview Request");

            if (resultCode == RESULT_OK) {
                String restaurant = data.getStringExtra("restaurant");
                float rating = data.getFloatExtra("rating",0);
                String review = data.getStringExtra("review");
                Review newReview = new Review(restaurant, rating, review);
                Log.d(TAG, "newReview to Database" +newReview);
                reviewDatabaseReference.push().setValue(newReview);
                //myList.add(new Note(title, noteType, noteContent));
                //arrayAdapter.notifyDataSetChanged();

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
            case R.id.addMenuItem:
                makeAReview();
                return true; //we consumed/handled the event
            //task: finish the two other cases, show toast messages
            case R.id.searchMenuItems:
                Toast.makeText(this, "TODO: search menu",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.favRec:
                Toast.makeText(this, "TODO: fav reccomendations",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Starts an actiity to make a review
    public void makeAReview(){
        Intent intent = new Intent(this, ReviewActivity.class);
        startActivityForResult(intent,GET_REVIEW_REQUEST);
    }


}
