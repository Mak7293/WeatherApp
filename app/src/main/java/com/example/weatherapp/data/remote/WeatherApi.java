package com.example.weatherapp.data.remote;

import com.example.weatherapp.domin.util.Utility;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
