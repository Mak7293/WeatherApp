package com.example.weatherapp.di;


import android.app.Application;

import com.example.weatherapp.data.remote.WeatherApi;
import com.example.weatherapp.domin.util.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
@InstallIn(SingletonComponent.class )
public class AppModule {

    @Provides
    @Singleton
    OkHttpClient httpClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }
    @Provides
    @Singleton
    WeatherApi provideWeatherApi(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(Utility.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create(WeatherApi.class);
    }
    @Provides
    @Singleton
    FusedLocationProviderClient provideFusedLocationProviderClient(Application app){
        return LocationServices.getFusedLocationProviderClient(app);
    }
}
