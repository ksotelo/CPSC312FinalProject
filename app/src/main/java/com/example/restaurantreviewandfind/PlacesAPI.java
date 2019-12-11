package com.example.restaurantreviewandfind;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.widget.ProgressBar;


public class PlacesAPI {
    static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    static final String BASE_URL_2 = "https://maps.googleapis.com/maps/api/place/details/json?";
    static final String API_KEY = "AIzaSyCFY_urP6VDBIWwsYFtKg2B8c-L6lirwXo";
    static final String TAG = "NearMeTag";
    static boolean acceptBulldogBucks = false;

    MainActivity mainActivity;

    public PlacesAPI(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void fetchPlaces(double latitude, double longitude, String keywords, int priceLevel, boolean bulldogBucks, boolean openNow) {
        acceptBulldogBucks = bulldogBucks;
        String url = createPlacesURL(latitude, longitude, keywords, priceLevel, openNow);
        FetchInterestingPhotosAsyncTask asyncTask = new FetchInterestingPhotosAsyncTask();
        asyncTask.execute(url);
    }

    public String createPlacesURL(double latitude, double longitude, String keywords, int priceLevel, boolean openNow) {
        String url = BASE_URL;
        url += "&location=" + latitude + "," + longitude;
        url += "&rankby=distance";
        if(keywords.equals("")){
            url += "&keyword=restaurant";
        } else {
            url += "&keyword=restaurant," + keywords;
        }
        if (priceLevel != -1){
            url += "&minprice=" + priceLevel;
            url += "&maxprice=" + priceLevel;
        }
        if(openNow){
            url += "&opennow";
        }
        url += "&key=" + API_KEY;
        Log.d(TAG, "URL " + url);
        return url;
    }

    public String createPlaceRequestURL(String placeId) {
        String url = BASE_URL_2;
        url += "&place_id=" + placeId;
        url += "&fields=name,formatted_address,price_level,opening_hours,website";
        url += "&key=" + API_KEY;
        Log.d(TAG, "places Request: " + url);
        return url;
    }

    class FetchInterestingPhotosAsyncTask extends AsyncTask<String, Void, List<Restaurant>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Restaurant> doInBackground(String... strings) {
            String urlStr = strings[0]; // var args, treat like an array
            String resultStr = getJSONString(urlStr);
            List<Restaurant> restaurants = parseJSON(resultStr);
            return restaurants;
        }

        @Override
        protected void onPostExecute(List<Restaurant> restaurants) {
            super.onPostExecute(restaurants);
            mainActivity.receivedRestaurantSearch(restaurants);
        }

        public String getJSONString(String urlStr) {
            String resultStr = "";
            try {
                URL url = new URL(urlStr);
                // try to connect using the HTTP protocol to the url
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                // 2. download JSON as string
                // build character by character
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1) { // read() returns -1 when end of stream reached
                    resultStr += (char) data;
                    data = reader.read();
                }
                //Log.d(TAG, "doInBackground: " + resultStr);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultStr;
        }

        /**
         *
         * @param result
         * @return
         */
        public List<Restaurant> parseJSON(String result) {
            List<Restaurant> list = new ArrayList<Restaurant>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray photoArray = jsonObject.getJSONArray("results"); // photo is the key
                for (int i = 0; i < photoArray.length(); i++) {
                    JSONObject singlePhotoObject = photoArray.getJSONObject(i);
                    String id = singlePhotoObject.getString("place_id");
                    Restaurant newRestaurant = getPlaceData(id);
                    if(acceptBulldogBucks){
                        if(newRestaurant.acceptsBulldogBucks()){
                            list.add(newRestaurant);
                        }
                    } else {
                        list.add(newRestaurant);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }

        public Restaurant getPlaceData(String id){
            Restaurant myRestaurant = new Restaurant(id);
            String url = createPlaceRequestURL(id);
            String result = getJSONString(url);
            List<String> nameStr = new ArrayList<String>();
            //Log.d(TAG, "Here!!");
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject resultObject = jsonObject.getJSONObject("result");
                try { myRestaurant.setAddress(resultObject.getString("formatted_address")); } catch (JSONException e) {}
                try { myRestaurant.setName(resultObject.getString("name")); } catch (JSONException e){}
                try {
                    JSONObject hoursObject = resultObject.getJSONObject("opening_hours");
                    JSONArray weekHoursArray = hoursObject.getJSONArray("weekday_text");
                    String monday = weekHoursArray.getString(0);
                    String tuesday = weekHoursArray.getString(1);
                    String wednesday = weekHoursArray.getString(2);
                    String thursday = weekHoursArray.getString(3);
                    String friday = weekHoursArray.getString(4);
                    String saturday = weekHoursArray.getString(5);
                    String sunday = weekHoursArray.getString(6);
                    String[] weekArray = {monday, tuesday, wednesday, thursday, friday, saturday, sunday};
                    myRestaurant.setHours(weekArray);
                } catch (JSONException e) {}
                try { myRestaurant.setPriceLevel(resultObject.getString("price_level")); } catch (JSONException e) {}
                try { myRestaurant.setWebsite(resultObject.getString("website")); } catch (JSONException e) {}
                Log.d(TAG, "Restaurant: " + myRestaurant);

            } catch (JSONException e) {
                Log.d(TAG, "ERROR");
            }
            return myRestaurant;
        }
    }
}
