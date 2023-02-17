package weather.soft918.weather_app.data.repository;

import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.LiveData;
import weather.soft918.weather_app.data.data_source.local.LocationDao;
import weather.soft918.weather_app.data.mappers.WeatherMappers;
import weather.soft918.weather_app.data.data_source.remote.WeatherApi;
import weather.soft918.weather_app.data.data_source.remote.WeatherDto;
import weather.soft918.weather_app.domin.model.LocationEntity;
import weather.soft918.weather_app.domin.util.Resource;
import weather.soft918.weather_app.data.repository.weather.WeatherInfo;
import weather.soft918.weather_app.domin.repository.Repository;
import weather.soft918.weather_app.domin.util.Utility;
import weather.soft918.weather_app.domin.util.WeatherUiState;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryImp implements Repository {
    private WeatherApi api;
    private LocationDao dao;
    private SharedPreferences sharedPref;
    private final ScheduledExecutorService backgroundExecutor =
            Executors.newSingleThreadScheduledExecutor();
    @Inject
    public RepositoryImp(
            WeatherApi api,
            LocationDao dao,
            SharedPreferences sharedPref
    ){
        this.api = api;
        this.dao = dao;
        this.sharedPref = sharedPref;
    }
    @Override
    public Resource<WeatherInfo> getWeatherData(double lat, double lng) {

        AtomicBoolean processed = new AtomicBoolean(true) ;

        try {
            HashMap<String,Resource<WeatherInfo>> resultResource = new HashMap<>();
            Call<WeatherDto> call = api.getWeatherData(lat, lng);
            backgroundExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(call.isExecuted()){
                        call.cancel();
                        Resource<WeatherInfo> resource = new Resource.Error(
                                null,"Server is too busy, please try again.",
                                WeatherUiState.ERROR_LONG_TIME_HTTP_REQUEST);
                        resultResource.put("resource",resource);
                        synchronized (processed) {
                            processed.notify();
                        }
                    }
                }
            });

            call.enqueue(new Callback<WeatherDto>() {
                @Override
                public void onResponse(Call<WeatherDto> call, Response<WeatherDto> response) {
                    if (response.isSuccessful()){
                        Resource<WeatherInfo> resource= new Resource.Success(
                                WeatherMappers.weatherDtoToWeatherInfo(response.body()),
                                null,
                                WeatherUiState.DATA_AVAILABLE
                        );
                        Log.d("result0", resource.toString());
                        String cashedWeatherForecast = new Gson().toJson(response.body());
                        sharedPref.edit().putString(Utility.CASHED_WEATHER_FORECAST,
                                cashedWeatherForecast).apply();
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
                                        "Http response isn't successfully form application server, please try again."
                                        ,WeatherUiState.DATA_ERROR);
                                resultResource.put("resource",resource);
                                synchronized (processed) {
                                    processed.notify();
                                }
                            }
                            case 404: {
                                Log.e("Error 404", "Not Found");
                                Resource<WeatherInfo> resource = new Resource.Error(null,
                                        "Http response isn't successfully form application server, please try again."
                                        ,WeatherUiState.DATA_ERROR);
                                resultResource.put("resource",resource);
                                synchronized (processed) {
                                    processed.notify();
                                }
                            }
                            default: {
                                Log.e("Error", "Generic Error");
                                Resource<WeatherInfo> resource = new Resource.Error(null,
                                        "Http response was not successfully received from the application server, please try again."
                                        ,WeatherUiState.DATA_ERROR);
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
                            "Fail to response from api",WeatherUiState.DATA_ERROR);
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
            e.printStackTrace();
            String message = e.getMessage();
            if(message == null){
                message = "An unknown error occurred. Please try again later.";
            }
            return new Resource.Error(null, message,WeatherUiState.DATA_ERROR);
        }
    }
    public void onDestroyRepositoryBackgroundThread(){
        if (!backgroundExecutor.isShutdown()){
            backgroundExecutor.shutdown();
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
    @Override
    public LocationEntity getLocation(int id) {
        return dao.getLocation(id);
    }
}
