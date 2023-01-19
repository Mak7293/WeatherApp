package com.example.weatherapp.di;

import android.location.Location;

import com.example.weatherapp.data.location.DefaultLocationTracker;
import com.example.weatherapp.domin.location.LocationTracker;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract LocationTracker bindLocationTracker(DefaultLocationTracker defaultLocationTracker );
}
