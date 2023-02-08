package com.example.weatherapp.presentation.view_models;


import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.domin.location.LocationTracker;
import com.example.weatherapp.domin.repository.Repository;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.util.Utility;
import com.example.weatherapp.domin.weather.WeatherInfo;
import com.example.weatherapp.presentation.WeatherState;
import com.example.weatherapp.presentation.activities.StatisticsActivity;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class WeatherViewModel extends ViewModel {
    private final Repository repository;
    private final LocationTracker locationTracker;
    private final Application applicationContext;
    public MutableLiveData<WeatherState> state = new MutableLiveData<WeatherState>();
    public MutableLiveData<WeatherUiState> weatherUiState = new MutableLiveData<WeatherUiState>();
    private final ScheduledExecutorService backgroundExecutor =
            Executors.newSingleThreadScheduledExecutor();
    public enum WeatherUiState{
        LOADING,
        DATA_ERROR,
        LOCATION_ERROR,
        DATA_AVAILABLE
    }
    public enum WeatherEvent {
        GET_LATEST_DATA,
        DETAILS_FORECAST
    }
    @Inject
    public WeatherViewModel(
            Repository repository,
            LocationTracker locationTracker,
            Application applicationContext
    ){
        this.repository = repository;
        this.locationTracker = locationTracker;
        this.applicationContext = applicationContext;
    }

    public void loadWeatherInfo(){
        weatherUiState.postValue(WeatherUiState.LOADING);
        state.postValue(new WeatherState(null, null));
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
                    Log.d("success",state.toString());
                }
                backgroundExecutor.shutdown();
            }
        });
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
                Toast.makeText(applicationContext,"refresh data",Toast.LENGTH_SHORT).show();
                break;
            }
        }

    }
}