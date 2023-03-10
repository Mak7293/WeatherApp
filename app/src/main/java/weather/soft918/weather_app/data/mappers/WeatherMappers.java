package weather.soft918.weather_app.data.mappers;

import android.util.Log;
import weather.soft918.weather_app.data.data_source.remote.WeatherDataDto;
import weather.soft918.weather_app.data.data_source.remote.WeatherDto;
import weather.soft918.weather_app.data.repository.weather.WeatherData;
import weather.soft918.weather_app.data.repository.weather.WeatherInfo;
import weather.soft918.weather_app.data.repository.weather.WeatherType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeatherMappers {

    static public HashMap<Integer, List<WeatherData>> weatherDataDtoToWeatherDataMap(WeatherDataDto weatherDataDto){
        HashMap<Integer, List<WeatherData>> week = new HashMap<>();
        List<WeatherData> day = new ArrayList<>();
        int i = 0;
        int iDay = 0;
        while(i < weatherDataDto.time.size()){
            String time = weatherDataDto.time.get(i);
            double temperature = weatherDataDto.temperature_2m.get(i);
            int weatherCode = weatherDataDto.weathercode.get(i);
            double windSpeed = weatherDataDto.windspeed_10m.get(i);
            double pressure = weatherDataDto.pressure_msl.get(i);
            double humidity = weatherDataDto.relativehumidity_2m.get(i);
            WeatherData weatherData = new WeatherData(
                    LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                    temperature,
                    pressure,
                    windSpeed,
                    humidity,
                    new WeatherType().fromWMO(weatherCode)
            );
            day.add(weatherData);
            if(((i+1)%24d) == 0 ){
                week.put(iDay,day);
                iDay++;
                day = new ArrayList<>();
            }
            i++;
        }
        Log.d("week0", week.toString());
        return week;

    }

    static public WeatherInfo weatherDtoToWeatherInfo(WeatherDto weatherDto) {
        HashMap<Integer, List<WeatherData>> weatherDataMap =
                weatherDataDtoToWeatherDataMap(weatherDto.hourly);
        List<WeatherData> currentWeatherDataList = weatherDataMap.get(0);
        LocalDateTime now = LocalDateTime.now();
        int hour;
        if(now.getMinute() < 30 ) {
            hour = now.getHour();
        } else {
            if(now.getHour() == 23){
                hour = now.getHour();
            }else {
                hour = now.getHour()+1;
            }
        }
        WeatherData currentWeatherData =  new WeatherData(
                currentWeatherDataList.get(hour).time,
                currentWeatherDataList.get(hour).temperatureCelsius,
                currentWeatherDataList.get(hour).pressure,
                currentWeatherDataList.get(hour).windSpeed,
                currentWeatherDataList.get(hour).humidity,
                currentWeatherDataList.get(hour).weatherType
        );
        return new WeatherInfo(weatherDataMap,currentWeatherData);
    }
}
