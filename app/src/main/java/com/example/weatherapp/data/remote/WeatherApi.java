package com.example.weatherapp.data.remote;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl")
    public Observable<WeatherDto> getWeatherData(
            @Query("latitude")
            double lat,
            @Query("longitude")
            double lng
    );
}
