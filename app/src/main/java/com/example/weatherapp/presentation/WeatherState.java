package com.example.weatherapp.presentation;

import androidx.annotation.Nullable;

import com.example.weatherapp.data.repository.weather.WeatherInfo;
import com.example.weatherapp.domin.util.WeatherUiState;


public class WeatherState {
    @Nullable
    public WeatherInfo weatherInfo;
    @Nullable
    public String error;
    @Nullable
    public WeatherUiState state;
    public WeatherState(
            @Nullable WeatherInfo weatherInfo,
            @Nullable String error,
            @Nullable WeatherUiState state
    ){
        this.weatherInfo = weatherInfo;
        this.error = error;
        this.state = state;
    }

}
