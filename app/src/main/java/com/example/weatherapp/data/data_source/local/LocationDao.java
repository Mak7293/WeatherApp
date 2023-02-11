package com.example.weatherapp.data.data_source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.weatherapp.domin.model.LocationEntity;

import java.util.List;

import javax.annotation.Nullable;

@Dao
public interface LocationDao {

    @Insert
    void insertLocation(LocationEntity location);

    @Delete
    void deleteLocation(LocationEntity location);

    @Query("SELECT * FROM location_table")
    LiveData<List<LocationEntity>> fetchAllLocation();

    @Query("SELECT * FROM location_table WHERE id=:id")
    LocationEntity getLocation(int id);

}
