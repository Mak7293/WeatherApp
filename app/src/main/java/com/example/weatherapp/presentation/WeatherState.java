package com.example.weatherapp.presentation;

import androidx.annotation.Nullable;

import com.example.weatherapp.domin.weather.WeatherInfo;

public class WeatherState {
    @Nullable
    public WeatherInfo weatherInfo;
    @Nullable
    public String error;
    public WeatherState(
            @Nullable WeatherInfo weatherInfo,
            @Nullable String error
    ){
        this.weatherInfo = weatherInfo;
        this.error = error;
    }
}
