//xml background used: https://www.123rf.com/photo_29008218_light-blue-vertical-abstract-background-texture.html
package com.example.restaurantreviewandfind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewReviewsActivity extends AppCompatActivity {
    private String TAG = "VIEW_REVIEW_ACTIVITY";
    private List<Review> reviewList;
    private String restaurant;
    private String rating;
    private String reviewContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);
        reviewList = new ArrayList<>();
        connectToFirebase();



    }

    public void connectToFirebase(){
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference().child("review").push();

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rating = dataSnapshot.child("rating").getValue().toString();
                restaurant = dataSnapshot.child("restaurant").getValue().toString();
                reviewContent = dataSnapshot.child("reviewText").getValue().toString();
                Toast.makeText(ViewReviewsActivity.this, "review" + restaurant + rating + reviewContent, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setArrayAdapter(String restaurant, float rating, String reviewContent){

    }

}
