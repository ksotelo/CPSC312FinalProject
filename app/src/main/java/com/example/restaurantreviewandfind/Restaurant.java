package com.example.restaurantreviewandfind;

import java.util.Arrays;

public class Restaurant {

        String placeId;
        String name;
        String address;
        String[] hours = new String[7];
        String website;
        String priceLevel;
        boolean acceptsBulldogBucks = false;
        String[] bulldogBucks = {"ChIJNSuFR_kYnlQROXOwBwpoXRw", "ChIJ5RiO6MIYnlQRswkXPIX1XRQ", "ChIJV5IpUvIYnlQR6e33dlIdBNE",
            "ChIJ5Vm5_uoYnlQRCZhElaJZHyM", "ChIJvZt6Hu4YnlQRdnLFhhWondA", "ChIJJaJeGPIYnlQRKHnyk1yxuUs",
            "ChIJq3O8LusYnlQR-9v8t30ovC4", "ChIJa8wN-1YZnlQRI-ZbVwG0vtc", "ChIJl73M2OkZnlQR-y5-5nJ7hpc",
            "ChIJq3O8LusYnlQRytku1VL2sZA", "ChIJ5Vm5_uoYnlQR0Oz4tk4dF9U", "ChIJp8RNCO8YnlQR4o4ihQX41wk",
            "ChIJcTDKkh8ZnlQRsvD7W9tDOEs", "ChIJQTHPBe4YnlQRfIsZuZf0BCI", "ChIJm39JCMIYnlQRr7ADalrbwyY",
            "ChIJY2gb6mAYnlQRnPsCq3AIpAU", "ChIJlc4mjmAYnlQRGz6_om8Z7p8", "ChIJfQWUTrsZnlQRfKhl1teD1nQ",
            "ChIJK8qcqf4YnlQRyUxkD8P2WBc", "ChIJS13wnIwYnlQR-n2H0ZJizxQ", "ChIJtbpoUeoYnlQRF6JjhHQPV14",
            "ChIJeyQx0u8YnlQR1t3xb1D6HGg", "ChIJLYiJ-PEYnlQRBgXt8gLI0BI", "ChIJYTuXFe4YnlQRIfK9Tmf5iGU",
            "ChIJX0noQe4YnlQRftMwrKKS5qw", "ChIJtT_P8uUYnlQR436dgMrfv3g", "ChIJYy-1eAgZnlQRPnRFaI8MudM",
            "ChIJWUhgYJQYnlQRW-Na2EyAL6w", "ChIJxRi0jGAYnlQRka8-4k0qcHA", "ChIJPSb3V-oYnlQROBy6iO64oi8",
            "ChIJSfiei5MYnlQRhlSDlcd2IiE", "ChIJld6w-PIYnlQR6muzshwSqJ0"};

       public Restaurant(String id){
           placeId = id;
           setBulldogBucks();
       }

       //dvc
       public Restaurant(){
           name = "BLANK NAME";
           address = "BLANK ADDY";
           website = "BLANK WEBSITE";
           priceLevel = "$";
           acceptsBulldogBucks = true;
       }

        public String getPlaceId() {
                return placeId;
        }

        public void setPlaceId(String placeId) {
                this.placeId = placeId;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public String[] getHours() {
                return hours;
        }

        public void setHours(String[] hours) {
                this.hours = hours;
        }

        public String getWebsite() {
                return website;
        }

        public void setWebsite(String website) {
                this.website = website;
        }

        public String getPriceLevel() {
                return priceLevel;
        }

        public void setPriceLevel(String priceLevelIn) {
           /*if(priceLevelIn.equals("1")){
               priceLevel = "$";
           } else if (priceLevelIn.equals("2")){
               priceLevel = "$$";
           } else if (priceLevelIn.equals("3")){
               priceLevel = "$$$";
           } else if (priceLevelIn.equals("4")) {
               priceLevel = "$$$$";
           } else {
               priceLevel = priceLevelIn;
           }*/
            priceLevel = priceLevelIn;

        }

        public void setBulldogBucks(){
           for(int i = 0; i < bulldogBucks.length; i++){
               if (placeId.equals(bulldogBucks[i])){
                   acceptsBulldogBucks = true;
                   break;
               }
           }
        }

        public boolean acceptsBulldogBucks(){
           return acceptsBulldogBucks;
        }

    @Override
    public String toString() {
        return "Restaurant{" +
                "placeId='" + placeId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", hours=" + Arrays.toString(hours) +
                ", website='" + website + '\'' +
                ", priceLevel='" + priceLevel + '\'' +
                ", acceptsBulldogBucks='" + acceptsBulldogBucks + '\'' +
                '}';
    }
}
