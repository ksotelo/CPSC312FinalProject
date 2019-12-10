//Photo used in search xml: https://www.youworkforthem.com/photo/169881/winter-vegetarian-food-cooking-ingredients-over-white-background-vertical-composition
package com.example.restaurantreviewandfind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
}
