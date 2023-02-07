package com.example.weatherapp.data.data_source.remote;

import com.example.weatherapp.domin.util.Utility;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET(Utility.URL)
    Call<WeatherDto> getWeatherData(
            @Query("latitude")
            double lat,
            @Query("longitude")
            double lng
    );

}
