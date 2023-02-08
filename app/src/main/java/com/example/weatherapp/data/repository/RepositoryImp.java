package com.example.weatherapp.data.repository;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.weatherapp.data.data_source.local.LocationDao;
import com.example.weatherapp.data.mappers.WeatherMappers;
import com.example.weatherapp.data.data_source.remote.WeatherApi;

import com.example.weatherapp.data.data_source.remote.WeatherDto;
import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.domin.util.CheckInternetConnection;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.util.Utility;
import com.example.weatherapp.domin.weather.WeatherInfo;
import com.example.weatherapp.domin.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryImp implements Repository {
    WeatherApi api;
    LocationDao dao;
    @Inject
    public RepositoryImp(
            WeatherApi api,
            LocationDao dao
    ){
        this.api = api;
        this.dao = dao;
    }
    @Override
    public Resource<WeatherInfo> getWeatherData(double lat, double lng) {

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
    @Override
    public LiveData<List<LocationEntity>> getAllLocation() {
        return dao.fetchAllLocation();
    }
    @Override
    public void insertLocation(LocationEntity location) {
        dao.insertLocation(location);
    }
    @Override
    public void deleteLocation(LocationEntity location) {
        dao.deleteLocation(location);
    }
}
