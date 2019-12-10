
package com.example.restaurantreviewandfind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class RestaurantInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
    }

    //Goes to make a new activity
    public void fabListener(){
        FloatingActionButton fab = findViewById(R.id.addReviewButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Going to Make a Review Screen", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(RestaurantInfoActivity.this, ReviewActivity.class);
                startActivity(intent);
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
}
