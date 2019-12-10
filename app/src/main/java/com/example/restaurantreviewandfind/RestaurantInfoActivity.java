//xml background photo: https://pngtree.com/freebackground/restaurant-menu-background-poster_401323.html

package com.example.restaurantreviewandfind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}
