//Photo used in search xml: https://www.youworkforthem.com/photo/169881/winter-vegetarian-food-cooking-ingredients-over-white-background-vertical-composition
package com.example.restaurantreviewandfind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.Resource;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search);
        restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant());

        ListView listView = new ListView(this);
        setContentView(listView);

        ArrayAdapter<Restaurant> arrayAdapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_2,android.R.id.text1,restaurantList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView,parent);
                Drawable d = getDrawable(R.drawable.backgroundfood);
                view.setBackground(d);
                Restaurant restaurant = restaurantList.get(position);
                //set values for the view
                TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
                tv1.setText(restaurant.getName());
                TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
                tv2.setText(restaurant.getPriceLevel());
                return view;
            }
        };


    }

}
