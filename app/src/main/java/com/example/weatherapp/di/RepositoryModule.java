package com.example.weatherapp.di;


import com.example.weatherapp.data.repository.WeatherRepositoryImpl;
import com.example.weatherapp.domin.repository.WeatherRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract WeatherRepository bindWeatherRepository(WeatherRepositoryImpl weatherRepositoryImpl);
}
