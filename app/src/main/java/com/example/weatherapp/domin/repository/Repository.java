package com.example.weatherapp.domin.repository;

import android.location.Location;

import androidx.lifecycle.LiveData;

import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.weather.WeatherInfo;

public interface Repository {
    abstract Resource<WeatherInfo> getWeatherData(double lat, double lng);
    abstract LiveData<LocationEntity> getNotes();

    abstract void insertNote(LocationEntity location);

    abstract void deleteNote(LocationEntity location);
}
