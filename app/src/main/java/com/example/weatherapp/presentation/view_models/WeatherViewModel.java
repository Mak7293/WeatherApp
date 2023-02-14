package com.example.weatherapp.presentation.view_models;


import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.domin.location.LocationTracker;
import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.domin.repository.Repository;
import com.example.weatherapp.domin.util.CheckInternetConnection;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.util.Utility;
import com.example.weatherapp.data.repository.weather.WeatherInfo;
import com.example.weatherapp.presentation.WeatherState;
import com.example.weatherapp.presentation.activities.StatisticsActivity;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WeatherViewModel extends ViewModel {
    private final Repository repository;
    private final LocationTracker locationTracker;
    private final Application applicationContext;
    private final SharedPreferences sharedPref;
    public boolean isFirstTime = true;

    public MutableLiveData<WeatherState> state = new MutableLiveData<WeatherState>();
    public MutableLiveData<WeatherUiState> weatherUiState = new MutableLiveData<WeatherUiState>();
    private final ScheduledExecutorService backgroundExecutor =
            Executors.newSingleThreadScheduledExecutor();
    public enum WeatherUiState{
        LOADING,
        DATA_ERROR,
        LOCATION_ERROR,
        DATA_AVAILABLE,
        INTERNET_CONNECTION_ERROR
    }
    public enum WeatherEvent {
        GET_LATEST_DATA,
        DETAILS_FORECAST
    }
    @Inject
    public WeatherViewModel(
            Repository repository,
            LocationTracker locationTracker,
            Application applicationContext,
            SharedPreferences sharedPref
    ){
        this.repository = repository;
        this.locationTracker = locationTracker;
        this.applicationContext = applicationContext;
        this.sharedPref = sharedPref;
    }
    public void loadWeatherInfo(int id) {
        if (!CheckInternetConnection.checkInternetConnection(applicationContext)){
            weatherUiState.postValue(WeatherUiState.INTERNET_CONNECTION_ERROR);
            return;
        }
        weatherUiState.postValue(WeatherUiState.LOADING);
        state.postValue(new WeatherState(null, null));
        if (id == Utility.LOCALE_LOCATION_ID){
            backgroundExecutor.execute(new Runnable() {
                @Override
                public void run(){
                    //state = new WeatherState(null, true, null);
                    HashMap<String, Double> location = null;
                    try {
                        location = locationTracker.getCurrentLocation();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Log.d("location", location.toString());
                    }
                    HashMap<String, Double> finalLocation = location;

                    if (finalLocation.get(Utility.LATITUDE) != null && finalLocation.get(Utility.LONGITUDE)!= null){
                        Resource<WeatherInfo> result = repository.getWeatherData(
                                finalLocation.get(Utility.LATITUDE), finalLocation.get(Utility.LONGITUDE));
                        if (result instanceof Resource.Success) {
                            state.postValue(new WeatherState(result.data,null));
                            weatherUiState.postValue(WeatherUiState.DATA_AVAILABLE);
                        } else if (result instanceof Resource.Error) {
                            state.postValue(new WeatherState(null, result.message));
                            weatherUiState.postValue(WeatherUiState.DATA_ERROR);
                        }
                    }else {
                        state.postValue(new WeatherState(
                                null,
                                "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                        ));
                        weatherUiState.postValue(WeatherUiState.LOCATION_ERROR);
                    }
                    Log.d("success",state.toString());
                    Log.d("success",weatherUiState.toString());
                }
            });
        }else {
            LocationEntity location = getLocationById(id);
            HashMap<String ,Double> finalLocation = new HashMap<>();
            finalLocation.put(Utility.LATITUDE,location.latitude);
            finalLocation.put(Utility.LONGITUDE,location.longitude);
            backgroundExecutor.execute(new Runnable() {
                @Override
                public void run(){
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (finalLocation.get(Utility.LATITUDE) != null && finalLocation.get(Utility.LONGITUDE)!= null){
                        Resource<WeatherInfo> result = repository.getWeatherData(
                                finalLocation.get(Utility.LATITUDE), finalLocation.get(Utility.LONGITUDE));
                        if (result instanceof Resource.Success) {
                            state.postValue(new WeatherState(result.data,null));
                            weatherUiState.postValue(WeatherUiState.DATA_AVAILABLE);
                        } else if (result instanceof Resource.Error) {
                            state.postValue(new WeatherState(null, result.message));
                            weatherUiState.postValue(WeatherUiState.DATA_ERROR);
                        }
                    }else {
                        state.postValue(new WeatherState(
                                null,
                                "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                        ));
                        weatherUiState.postValue(WeatherUiState.LOCATION_ERROR);
                    }
                    Log.d("success",state.toString());
                    Log.d("success",weatherUiState.toString());
                }
            });

        }
    }
    public void weatherEvent(WeatherEvent event){
        switch (event){
            case DETAILS_FORECAST: {
                Intent intent = new Intent(applicationContext, StatisticsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
                break;
            }
            case GET_LATEST_DATA: {
                int locationId = getCurrentLocationId();
                loadWeatherInfo(locationId);
                break;
            }
        }
    }
    public int getCurrentLocationId(){
        return sharedPref.getInt(Utility.CURRENT_LOCATION,Utility.LOCALE_LOCATION_ID);
    }
    @Nullable
    public LocationEntity getLocationById(int id){
        try {
            AtomicBoolean processed = new AtomicBoolean(true);
            final LocationEntity[] location = new LocationEntity[1];
            backgroundExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    location[0] = repository.getLocation(id);
                    synchronized (processed) {
                        processed.notify();
                    }
                }
            });
            synchronized (processed) {
                processed.wait();
            }
            return location[0];
        }catch(InterruptedException  e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        backgroundExecutor.shutdown();
    }
}
