package com.example.weatherapp.di;


import android.app.Application;

import com.example.weatherapp.data.remote.WeatherApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
@InstallIn(SingletonComponent.class )
public class AppModule {
    @Provides
    @Singleton
    WeatherApi provideWeatherApi(){
        return new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(WeatherApi.class);
    }

    @Provides
    @Singleton
    FusedLocationProviderClient provideFusedLocationProviderClient(Application app){
        return LocationServices.getFusedLocationProviderClient(app);
    }
}
