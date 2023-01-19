package com.example.weatherapp.domin.repository;

import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.weather.WeatherInfo;

public interface WeatherRepository {
    abstract Resource<WeatherInfo> getWeatherData(double lat, double lng);
}
