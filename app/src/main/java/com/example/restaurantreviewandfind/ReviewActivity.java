package com.example.restaurantreviewandfind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.core.Tag;

public class ReviewActivity extends AppCompatActivity {

    static final String TAG = "REVIEW_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent intent = getIntent();

    }

    //called when the add review Button is called
    public void addReviewButton(View view){

        final EditText restaurantTitle = (EditText)findViewById(R.id.restaurantID);
        final RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        final EditText review = (EditText)findViewById(R.id.reviewRestaurantText);

        String restaurant = restaurantTitle.getText().toString();
        long starRating = rating.getNumStars();
        String reviewText = review.getText().toString();


        if(restaurantTitle.length() == 0 && review.length() == 0 ){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            Intent infoIntent = new Intent();
            infoIntent.putExtra("restaurant", restaurant);
            infoIntent.putExtra("rating", starRating);
            infoIntent.putExtra("review", reviewText);
            Toast.makeText(this," "+ restaurant + starRating + reviewText, Toast.LENGTH_LONG).show();
            //Log.d(TAG, " "+ restaurantTitle + rating + reviewText);
            setResult(RESULT_OK, infoIntent);
            finish();
        }
    }
}
