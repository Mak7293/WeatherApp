package com.example.weatherapp.data.remote;

public class WeatherDto {
    public WeatherDataDto hourly;
    public WeatherDto(WeatherDataDto hourly){
        this.hourly = hourly;
    }
}
