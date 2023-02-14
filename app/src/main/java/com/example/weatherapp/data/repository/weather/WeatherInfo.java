package com.example.weatherapp.data.repository.weather;

import androidx.annotation.Nullable;

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
