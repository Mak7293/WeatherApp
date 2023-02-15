package com.example.weatherapp.presentation.view_models;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.data.data_source.remote.WeatherDto;
import com.example.weatherapp.data.mappers.WeatherMappers;
import com.example.weatherapp.domin.location.LocationTracker;
import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.domin.repository.Repository;
import com.example.weatherapp.domin.util.CheckInternetConnection;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.util.Utility;
import com.example.weatherapp.data.repository.weather.WeatherInfo;
import com.example.weatherapp.domin.util.WeatherUiState;
import com.example.weatherapp.presentation.WeatherState;
import com.example.weatherapp.presentation.activities.StatisticsActivity;
import com.google.gson.Gson;

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

    @Nullable
    public MutableLiveData<WeatherState> state = new MutableLiveData<WeatherState>(
            new WeatherState(null,null,WeatherUiState.NULL));
    private final ScheduledExecutorService backgroundExecutor =
            Executors.newSingleThreadScheduledExecutor();
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
            state.postValue(new WeatherState(null,"Your Internet is not connected.",
                    WeatherUiState.INTERNET_CONNECTION_ERROR));
            return;
        }
        if (state.getValue().state != WeatherUiState.LOADING) {
            state.postValue(new WeatherState(null,null, WeatherUiState.LOADING));
            if (id == Utility.LOCALE_LOCATION_ID && isLocationEnabled()) {
                backgroundExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, Double> location = null;
                        try {
                            location = locationTracker.getCurrentLocation();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            Log.d("location", location.toString());
                        }
                        HashMap<String, Double> finalLocation = location;

                        if (finalLocation.get(Utility.LATITUDE) != null && finalLocation.get(Utility.LONGITUDE) != null) {
                            Resource<WeatherInfo> result = repository.getWeatherData(
                                    finalLocation.get(Utility.LATITUDE), finalLocation.get(Utility.LONGITUDE));
                            if (result instanceof Resource.Success) {
                                state.postValue(new WeatherState(result.data,
                                        null, result.state));
                            } else if (result instanceof Resource.Error) {
                                state.postValue(new WeatherState(null,
                                        result.message, result.state));
                            }
                        } else {
                            state.postValue(new WeatherState(
                                    null,
                                    "Couldn't retrieve location. Make sure to grant permission and enable GPS.",
                                    WeatherUiState.LOCATION_ERROR
                            ));
                        }
                        Log.d("success", state.toString());
                    }
                });
                sharedPref.edit().putInt(Utility.CURRENT_LOCATION,Utility.LOCALE_LOCATION_ID).apply();
            }else if (id == Utility.LOCALE_LOCATION_ID && !isLocationEnabled()){
                Toast.makeText(applicationContext, "Please Enable device location", Toast.LENGTH_SHORT).show();
                state.postValue(new WeatherState(
                        null,
                        "Please Enable your device location.",
                        WeatherUiState.LOCATION_DISABLE
                ));
            }
            else {
                LocationEntity location = getLocationById(id);
                HashMap<String, Double> finalLocation = new HashMap<>();
                finalLocation.put(Utility.LATITUDE, location.latitude);
                finalLocation.put(Utility.LONGITUDE, location.longitude);
                backgroundExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(700);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if (finalLocation.get(Utility.LATITUDE) != null && finalLocation.get(Utility.LONGITUDE) != null) {
                            Resource<WeatherInfo> result = repository.getWeatherData(
                                    finalLocation.get(Utility.LATITUDE), finalLocation.get(Utility.LONGITUDE));
                            if (result instanceof Resource.Success) {
                                state.postValue(new WeatherState(result.data,
                                        null, result.state));
                            } else if (result instanceof Resource.Error) {
                                state.postValue(new WeatherState(null,
                                        result.message, result.state));
                            }
                        } else {
                            state.postValue(new WeatherState(
                                    null,
                                    "Couldn't retrieve location. Make sure to grant permission and enable GPS.",
                                    WeatherUiState.LOCATION_ERROR
                            ));
                        }
                    }
                });
            }
        }else {
            Toast.makeText(applicationContext, "please wait", Toast.LENGTH_SHORT).show();
        }
    }
    //"Cashed data was load. click on get latest data button to recive latest weather forecast."
    public Pair<Boolean, WeatherState> loadWeatherCashedWeatherData(){
        String cashedData = sharedPref.getString(Utility.CASHED_WEATHER_FORECAST,
                Utility.NO_CASHED_WEATHER_FORECAST);
        if(!cashedData.equals(Utility.NO_CASHED_WEATHER_FORECAST)){
            WeatherDto weatherDto = new Gson().fromJson(cashedData,WeatherDto.class);
            WeatherInfo weatherInfo = WeatherMappers.weatherDtoToWeatherInfo(weatherDto);
            return new Pair<>(true, new WeatherState(weatherInfo,null,
                    WeatherUiState.LOAD_CASHED_WEATHER_FORECAST));
        }else {
            return new Pair<>(false, null);
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
    private boolean isLocationEnabled(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            LocationManager lm = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        }else {
            // This was deprecated in API 28
            int mode = Settings.Secure.getInt(applicationContext.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
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
