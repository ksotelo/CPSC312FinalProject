//xml background used: https://www.123rf.com/photo_29008218_light-blue-vertical-abstract-background-texture.html
package com.example.restaurantreviewandfind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_view_reviews);
        listView = new ListView(this);
        setContentView(listView);
        reviewList = new ArrayList<>();
        connectToFirebase();

    }

    public void connectToFirebase(){
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("review");

        // Attach a listener to read the data at our posts reference
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if(data != null){
                        //Toast.makeText(ViewReviewsActivity.this, data.toString(),Toast.LENGTH_LONG).show();
                    }
                    rating = data.child("rating").getValue().toString();
                    restaurant = data.child("restaurant").getValue().toString();
                    reviewContent = data.child("reviewText").getValue().toString();
                    float floatRating = Float.parseFloat(rating);
                    Review newReview = new Review(restaurant, floatRating, reviewContent);
                    reviewList.add(newReview);
                    setArrayAdapter();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setArrayAdapter(){
        ArrayAdapter<Review> arrayAdapter = new ArrayAdapter<Review>(this, android.R.layout.simple_list_item_2,android.R.id.text1,reviewList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView,parent);
                Review review = reviewList.get(position);

                TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
                tv1.setText(review.getRestaurant());
                TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
                String reviewAndRating = "Review: "+review.getReviewText() +" Rating: " +review.getRating();
                tv2.setText(reviewAndRating);
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
    }

}