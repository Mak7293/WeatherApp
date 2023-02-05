package com.example.weatherapp.presentation;


import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.domin.location.LocationTracker;
import com.example.weatherapp.domin.repository.WeatherRepository;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.util.Utility;
import com.example.weatherapp.domin.weather.WeatherInfo;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Nullable;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class WeatherViewModel extends ViewModel {
    private final WeatherRepository repository;
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
    @Inject
    public WeatherViewModel(
            WeatherRepository repository,
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
            Executor mainExecutor = ContextCompat.getMainExecutor(applicationContext);
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
}
