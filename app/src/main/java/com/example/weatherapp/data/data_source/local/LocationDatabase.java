package com.example.weatherapp.data.data_source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.weatherapp.domin.model.LocationEntity;

@Database(
        entities = LocationEntity.class,
        version = 1
)
public abstract class LocationDatabase extends RoomDatabase {

    abstract public LocationDao locationDao();

    public final static String DATABASE_NAME = "location_db";

}
