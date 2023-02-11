package com.example.weatherapp.domin.repository;

import androidx.lifecycle.LiveData;

import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.weather.WeatherInfo;

import java.util.List;

public interface Repository {
    abstract Resource<WeatherInfo> getWeatherData(double lat, double lng);
    abstract LiveData<List<LocationEntity>> getAllLocation();
    abstract void insertLocation(LocationEntity location);
    abstract void deleteLocation(LocationEntity location);

    abstract LocationEntity getLocation(int id);
}
