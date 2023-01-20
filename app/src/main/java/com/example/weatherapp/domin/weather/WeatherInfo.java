package com.example.weatherapp.domin.weather;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

public class WeatherInfo {
    HashMap<Integer, List<WeatherData>> weatherDataPerDay;
    @Nullable
    WeatherData currentWeatherData;
    public WeatherInfo(
            HashMap<Integer, List<WeatherData>> weatherDataPerDay,
            @Nullable WeatherData currentWeatherData
    ){
        this.weatherDataPerDay = weatherDataPerDay;
        this.currentWeatherData = currentWeatherData;
    }
}
