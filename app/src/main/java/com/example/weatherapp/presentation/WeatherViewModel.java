package com.example.weatherapp.presentation;


import android.location.Location;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.weatherapp.domin.location.LocationTracker;
import com.example.weatherapp.domin.repository.WeatherRepository;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.weather.WeatherInfo;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class WeatherViewModel extends ViewModel {
    WeatherRepository repository;
    LocationTracker locationTracker;
    ScheduledExecutorService backgroundExecutor = Executors.newSingleThreadScheduledExecutor();
    @Inject
    public WeatherViewModel(
            WeatherRepository repository,
            LocationTracker locationTracker
    ){
        this.repository = repository;
        this.locationTracker = locationTracker;
    }
    private WeatherState state = new WeatherState(null,null,null);
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public Observable<WeatherState> stateEmitter = Observable
            .create(new ObservableOnSubscribe<WeatherState>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<WeatherState> emitter) throws Throwable {
                    if(!emitter.isDisposed()){
                        emitter.onNext(state);
                    }
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());


    public void loadWeatherInfo(){
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                state = new WeatherState(null, true,null);
                Location location =locationTracker.getCurrentLocation();
                if (location != null){
                    Log.d("location","latitude: "+location.getLatitude()
                            + "longitude: "+location.getLongitude());
                    Resource<WeatherInfo> result = repository.getWeatherData(
                            location.getLatitude(),location.getLongitude());
                    if (result instanceof Resource.Success) {
                        state = new WeatherState(result.data,false,null);
                    } else if (result instanceof Resource.Error) {
                        state = new WeatherState(null,false, result.message);
                    }
                }else {
                    state = new WeatherState(
                            null,
                             false,
                             "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                    );
                }
            }
        });
    }
}
