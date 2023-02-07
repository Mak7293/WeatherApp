package com.example.weatherapp.di;


import com.example.weatherapp.data.repository.RepositoryImp;

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
    abstract com.example.weatherapp.domin.repository.Repository bindWeatherRepository(RepositoryImp weatherRepositoryImpl);
}
