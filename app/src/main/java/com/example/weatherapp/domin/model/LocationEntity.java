package com.example.weatherapp.domin.model;


import androidx.room.Entity;

@Entity(tableName = "location_table")
public class LocationEntity {
    String locality;
    String province;
    String country;
    String latitude;
    String longitude;
    public LocationEntity(
            String locality,
            String province,
            String country,
            String latitude,
            String longitude
    ){
        this.locality = locality;
        this.province = province;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
