package com.example.weatherapp.data.repository;

import com.example.weatherapp.data.remote.WeatherApi;

import com.example.weatherapp.domin.repository.WeatherRepository;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.weather.WeatherInfo;

import javax.inject.Inject;

public class WeatherRepositoryImpl implements WeatherRepository {
    WeatherApi api;
    @Inject
    public WeatherRepositoryImpl(WeatherApi api){
        this.api = api;
    }

    @Override
    public Resource<WeatherInfo> getWeatherData(double lat, double lng) {
        try {
            Resource.Success(api.getWeatherData(lat, lng).toWeatherInfo()
            )
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}
