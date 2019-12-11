
package com.example.restaurantreviewandfind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class RestaurantInfoActivity extends AppCompatActivity {

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
            String priceRep = "$: " +priceLevel;
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
                String newPriceRep = "$: " +priceLevel;
                price.setText(newPriceRep);
            }
            bulldogBucksAccepted.setText(buckString);
        }
        fabListener();
        buttonToGoToViews();
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
