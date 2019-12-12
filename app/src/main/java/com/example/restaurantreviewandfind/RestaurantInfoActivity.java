/**
 *
 * CPSC 312-02, Fall 2019
 * Final Project
 * Nothing to cite
 *
 * @author Kat Sotelo and Anna Smith
 * @version v1.0 12/11/19
 */

package com.example.restaurantreviewandfind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RestaurantInfoActivity extends AppCompatActivity {

    static final int GET_REVIEW_REQUEST = 2;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference reviewDatabaseReference;
    ChildEventListener myReviewChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        Intent intent = getIntent();
        if(intent != null){

            String restaurant = intent.getStringExtra("restaurant");
            String addy = intent.getStringExtra("addy");
            String website = intent.getStringExtra("website");
            String priceLevel = intent.getStringExtra("price");
            boolean bulldogBucks = intent.getBooleanExtra("bulldogBucks",false);

            String newAddyRep = "Address: "+addy;
            String newWebsiteRep = "Website: " + website;
            //String priceRep = "$: " +priceLevel;
            String buckString = convertBulldogString(bulldogBucks);

            TextView restaurantText = (TextView) findViewById(R.id.restaurantTitle);
            TextView addressId = (TextView)findViewById(R.id.addressID);
            TextView websiteId = (TextView)findViewById(R.id.websiteID);
            TextView price = (TextView)findViewById(R.id.priceLevel);
            TextView bulldogBucksAccepted = (TextView)findViewById(R.id.bulldogBucksAcceptance);

            restaurantText.setText(restaurant);
            addressId.setText(newAddyRep);
            websiteId.setText(newWebsiteRep);
            if(priceLevel==null){
                price.setText("price level not available");
            }else {
                String newPriceRep = convertPrice(priceLevel);
                //String newPriceRep = "$: " +priceLevel;
                price.setText(newPriceRep);
            }
            bulldogBucksAccepted.setText(buckString);
        }
        fabListener();
        buttonToGoToViews();
        setupFirebase();
    }

    public String convertBulldogString(boolean bucksAcceptance){
        String buckString = "";
        if(bucksAcceptance){
            buckString = "Accepts Bulldog Bucks: Yes";
        } else{
            buckString = "Accepts Bulldog Bucks: No";
        }
        return buckString;
    }

    public String convertPrice(String price){
        String newPriceRep = "";
        if(price.equals("1")){
            newPriceRep = "$";
        }if(price.equals("2")){
            newPriceRep = "$$";
        }
        if(price.equals("3")){
            newPriceRep = "$$$";
        }if(price.equals("4")){
            newPriceRep = "$$$$";
        }
        return newPriceRep;
    }

    //Goes to make a new activity
    public void fabListener(){
        FloatingActionButton fab = findViewById(R.id.addReviewButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Going to Make a Review Screen", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //Starts an activity to make a review
                    Intent intent = new Intent(RestaurantInfoActivity.this, ReviewActivity.class);
                    startActivityForResult(intent,GET_REVIEW_REQUEST);

            }
        });

    }

    public void buttonToGoToViews(){
        Button goToReviews = (Button) findViewById(R.id.goToWrittenReviews);
        goToReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantInfoActivity.this, ViewReviewsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setupFirebase(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        reviewDatabaseReference = mFirebaseDatabase.getReference().child("review");
        myReviewChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Review review = dataSnapshot.getValue(Review.class);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_REVIEW_REQUEST) {
            if (resultCode == RESULT_OK) {
                Review newReview = (Review) data.getSerializableExtra("review");
                reviewDatabaseReference.push().setValue(newReview);
            }
        }
    }
}
