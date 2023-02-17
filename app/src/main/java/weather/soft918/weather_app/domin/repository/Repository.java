package weather.soft918.weather_app.domin.repository;

import androidx.lifecycle.LiveData;
import weather.soft918.weather_app.domin.model.LocationEntity;
import weather.soft918.weather_app.domin.util.Resource;
import weather.soft918.weather_app.data.repository.weather.WeatherInfo;

import java.util.List;

public interface Repository {
    abstract Resource<WeatherInfo> getWeatherData(double lat, double lng);
    abstract LiveData<List<LocationEntity>> getAllLocation();
    abstract void insertLocation(LocationEntity location);
    abstract void deleteLocation(LocationEntity location);
    abstract LocationEntity getLocation(int id);
    abstract void onDestroyRepositoryBackgroundThread();
}
