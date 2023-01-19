package com.example.weatherapp.domin.weather;

import java.util.HashMap;
import java.util.List;

public class WeatherInfo {
    HashMap<Integer, List<WeatherData>> weatherDataPerDay;
    WeatherData currentWeatherData;
    public WeatherInfo(
            HashMap<Integer, List<WeatherData>> weatherDataPerDay,
            WeatherData currentWeatherData
    ){
        this.weatherDataPerDay = weatherDataPerDay;
        this.currentWeatherData = currentWeatherData;
    }
}
