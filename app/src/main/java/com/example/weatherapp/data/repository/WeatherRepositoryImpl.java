package com.example.weatherapp.data.repository;

import android.location.Location;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;
import android.util.Log;

import com.example.weatherapp.data.mappers.WeatherMappers;
import com.example.weatherapp.data.remote.WeatherApi;

import com.example.weatherapp.data.remote.WeatherDto;
import com.example.weatherapp.domin.repository.WeatherRepository;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.weather.WeatherInfo;
import com.example.weatherapp.presentation.WeatherViewModel;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepositoryImpl implements WeatherRepository {
    WeatherApi api;
    @Inject
    public WeatherRepositoryImpl(WeatherApi api){
        this.api = api;
    }

    @Override
    public Resource<WeatherInfo> getWeatherData(double lat, double lng) {
        Log.d("input", String.valueOf(lat) + lng);
        AtomicBoolean processed = new AtomicBoolean(true) ;
        try {
            HashMap<String,Resource<WeatherInfo>> resultResource = new HashMap<>();
            Call<WeatherDto> call = api.getWeatherData(lat, lng);
            call.enqueue(new Callback<WeatherDto>() {
                @Override
                public void onResponse(Call<WeatherDto> call, Response<WeatherDto> response) {
                    if (response.isSuccessful()){
                        Resource<WeatherInfo> resource= new Resource.Success(
                                WeatherMappers.weatherDtoToWeatherInfo(response.body()),
                                null
                        );
                        Log.d("result0", resource.toString());
                        resultResource.put("resource",resource);
                        synchronized (processed) {
                            processed.notify();
                        }
                    }else {
                        Log.d("callResponse","response isn't successfully");
                        int rc = response.code();
                        switch (rc) {
                            case 400: {
                                Log.e("Error 400", "Bad connection");
                                Resource<WeatherInfo> resource = new Resource.Error(null,
                                        "response isn't successfully");
                                resultResource.put("resource",resource);
                                synchronized (processed) {
                                    processed.notify();
                                }
                            }
                            case 404: {
                                Log.e("Error 404", "Not Found");
                                Resource<WeatherInfo> resource = new Resource.Error(null,
                                        "response isn't successfully");
                                resultResource.put("resource",resource);
                                synchronized (processed) {
                                    processed.notify();
                                }
                            }
                            default: {
                                Log.e("Error", "Generic Error");
                                Resource<WeatherInfo> resource = new Resource.Error(null,
                                        "response isn't successfully");
                                resultResource.put("resource",resource);
                                synchronized (processed) {
                                    processed.notify();
                                }
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<WeatherDto> call, Throwable t) {
                    Resource<WeatherInfo> resource = new Resource.Error(null,
                            "fail to response from api");
                    Log.d("failure",t.toString());
                    resultResource.put("resource",resource);
                }
            });
            synchronized (processed) {
                processed.wait();
            }
            Log.d("result_", resultResource.values().toString());
            return resultResource.get("resource");
        }catch (Exception e){
            Log.d("insideError",e.toString());
            e.printStackTrace();
            String message = e.getMessage();
            if(message == null){
                message = "An unknown error occurred.";
            }
            return new Resource.Error(null, message);
        }
    }
}
