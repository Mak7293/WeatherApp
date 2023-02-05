package com.example.weatherapp.domin.weather;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class WeatherInfo {
    public HashMap<Integer, List<WeatherData>> weatherDataPerDay;
    @Nullable
    public WeatherData currentWeatherData;
    public WeatherInfo(
            HashMap<Integer, List<WeatherData>> weatherDataPerDay,
            @Nullable WeatherData currentWeatherData
    ){
        this.weatherDataPerDay = weatherDataPerDay;
        this.currentWeatherData = currentWeatherData;
    }
}
