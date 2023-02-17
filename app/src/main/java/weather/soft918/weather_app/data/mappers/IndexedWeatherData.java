package weather.soft918.weather_app.data.mappers;

import weather.soft918.weather_app.data.repository.weather.WeatherData;

public class IndexedWeatherData {
    int index;
    WeatherData data;
    IndexedWeatherData(int index, WeatherData data){
        this.index = index;
        this.data = data;
    }
}
