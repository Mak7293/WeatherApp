package com.example.weatherapp.data.repository;

import android.location.Location;

import com.example.weatherapp.data.mappers.WeatherMappers;
import com.example.weatherapp.data.remote.WeatherApi;

import com.example.weatherapp.data.remote.WeatherDto;
import com.example.weatherapp.domin.repository.WeatherRepository;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.weather.WeatherInfo;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherRepositoryImpl implements WeatherRepository {
    WeatherApi api;
    @Inject
    public WeatherRepositoryImpl(WeatherApi api){
        this.api = api;
    }

    @Override
    public Resource<WeatherInfo> getWeatherData(double lat, double lng) {
        try {
            return new Resource.Success(
                    WeatherMappers.weatherDtoToWeatherInfo(api.getWeatherData(lat, lng)),
                    null
            );
        }catch (Exception e){
            e.printStackTrace();
            String message = e.getMessage();
            if(message == null){
                message = "An unknown error occurred.";
            }
            return new Resource.Error(null, message);
        }
    }
}
