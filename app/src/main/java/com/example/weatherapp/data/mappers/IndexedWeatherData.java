package com.example.weatherapp.data.mappers;

import com.example.weatherapp.domin.weather.WeatherData;

public class IndexedWeatherData {
    int index;
    WeatherData data;
    IndexedWeatherData(int index, WeatherData data){
        this.index = index;
        this.data = data;
    }
}
