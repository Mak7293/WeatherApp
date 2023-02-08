package com.example.weatherapp.domin.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_table")
public class LocationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id ;
    public String locality = "";
    public String province = "";
    public String country = "";
    public double latitude = 0.0d;
    public double longitude = 0.0d;
    public LocationEntity(
            int id,
            String locality,
            String province,
            String country,
            double latitude,
            double longitude
    ){
        this.id = id;
        this.locality = locality;
        this.province = province;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
