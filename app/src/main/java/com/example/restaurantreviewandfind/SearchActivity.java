//Photo used in search xml: https://www.youworkforthem.com/photo/169881/winter-vegetarian-food-cooking-ingredients-over-white-background-vertical-composition
package com.example.restaurantreviewandfind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Restaurant> restaurantList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        restaurantList = fillList(intent);
        //Log.d("MyFirebase", "Intent Received!");
        //restaurantList = new ArrayList<>();
        //restaurantList.add(new Restaurant());

        listView = new ListView(this);
        setContentView(listView);

        Log.d("TEST", "" + restaurantList.get(0));

        ArrayAdapter<Restaurant> arrayAdapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_2,android.R.id.text1,restaurantList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView,parent);
                Restaurant restaurant = restaurantList.get(position);
                //set values for the view
                TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
                tv1.setText(restaurant.getName());
                TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
                if(restaurant.getPriceLevel()==null){
                    tv2.setText("price level not available");
                }else {
                    //String newPriceRep = convertPrice(restaurant.getPriceLevel());
                    String newPriceRep = "$: " +restaurant.getPriceLevel();
                    tv2.setText(newPriceRep);
                }
                return view;
            }
        };
        listView.setAdapter(arrayAdapter);
        listClick();
    }

//    public String convertPrice(String price){
//        String newPriceRep = "";
//        if(price.equals(1)){
//            newPriceRep = "$";
//        }if(price.equals(2)){
//            newPriceRep = "$$";
//        }
//        if(price.equals(3)){
//            newPriceRep = "$$$";
//        }if(price.equals(4)){
//            newPriceRep = "$$$$";
//        }
//        return newPriceRep;
//    }



    public List<Restaurant> fillList(Intent intent){
        Log.d("TEST", "FILLING LIST");
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        ArrayList<String> placeIds = intent.getStringArrayListExtra("placeIds");
        ArrayList<String> names = intent.getStringArrayListExtra("names");
        ArrayList<String> addresses = intent.getStringArrayListExtra("addresses");
        //ArrayList<String[]> restaurantHours = intent.getStringArrayListExtra(
        ArrayList<String> websites = intent.getStringArrayListExtra("websites");
        ArrayList<String> priceLevels = intent.getStringArrayListExtra("priceLevels");
        boolean[] bulldogBucks = intent.getBooleanArrayExtra("bulldogBucks");

        Log.d("TEST", "CREATED LISTS");
        int length = placeIds.size();
        for(int i = 0; i < length; i++){
            Restaurant newRestaurant = new Restaurant(placeIds.get(i));
            newRestaurant.setName(names.get(i));
            newRestaurant.setAddress(addresses.get(i));
            newRestaurant.setWebsite(websites.get(i));
            //Log.d("TEST", "PRICELEVEL:" + );
            newRestaurant.setPriceLevel(priceLevels.get(i));
            restaurants.add(newRestaurant);
        }
        Log.d("TEST", "FILLED LISTS");
        return restaurants;
    }

    //on the click in the list, it goes to another intent
    public void listClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Restaurant restaurantSelected = (Restaurant) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(SearchActivity.this, RestaurantInfoActivity.class);
                //here we gotta put the serializable i think
                String name = restaurantSelected.getName();
                String addy = restaurantSelected.getAddress();
                String website = restaurantSelected.getWebsite();
                String price = restaurantSelected.getPriceLevel();
                boolean bulldogBucks =restaurantSelected.acceptsBulldogBucks();
                intent.putExtra("restaurant",name);
                intent.putExtra("addy",addy);
                intent.putExtra("website",website);
                intent.putExtra("price",price);
                intent.putExtra("bulldogBucks",bulldogBucks);
                startActivity(intent);
            }
        });
    }

}
