package com.example.weatherapp.data.remote;

import java.util.List;

public class WeatherDataDto {
    public List<String> time;
    public List<Double> temperature_2m;
    public List<Integer> weathercode;
    public List<Double> pressure_msl;
    public List<Double> windspeed_10m;
    public List<Double> relativehumidity_2m;

    public WeatherDataDto(
            List<String> time,
            List<Double> temperature_2m,
            List<Integer> weathercode,
            List<Double> pressure_msl,
            List<Double> windspeed_10m,
            List<Double> relativehumidity_2m
    ){
        this.time = time;
        this.temperature_2m = temperature_2m;
        this.weathercode = weathercode;
        this.pressure_msl = pressure_msl;
        this.windspeed_10m = windspeed_10m;
        this.relativehumidity_2m = relativehumidity_2m;
    }


}
