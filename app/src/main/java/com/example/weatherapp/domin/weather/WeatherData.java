package com.example.weatherapp.domin.weather;

import java.time.LocalDateTime;

public class WeatherData {
    public LocalDateTime time;
    public double temperatureCelsius;
    public double pressure;
    public double windSpeed;
    public double humidity;
    public WeatherType weatherType;
    public WeatherData(
            LocalDateTime time,
            double temperatureCelsius,
            double pressure,
            double windSpeed,
            double humidity,
            WeatherType weatherType
    ){
        this.time = time;
        this.temperatureCelsius = temperatureCelsius;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.weatherType = weatherType;
    }
}
