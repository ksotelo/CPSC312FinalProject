package com.example.restaurantreviewandfind;

import java.io.Serializable;

public class Review implements Serializable {

        private String restaurant;
        private float rating;
        private String reviewText;

        public Review() {
            restaurant = "BLANK AUTHOR";
            rating = 0;
            reviewText = "BLANK CONTENT";
        }

        public Review(String restaurant, float rating, String reviewText) {
            this.restaurant = restaurant;
            this.rating = rating;
            this.reviewText = reviewText;
        }

        public String getRestaurant() {
            return restaurant;
        }

        public void setRestaurant(String restaurant) {
            this.restaurant = restaurant;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public String getReviewText() {
            return reviewText;
        }

        public void setReviewText(String reviewText) {
            this.reviewText = reviewText;
        }

        @Override
        public String toString() {
            return restaurant + ", " + rating + ", " + reviewText;
        }
}
