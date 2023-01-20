package com.example.weatherapp.data.mappers;

import com.example.weatherapp.data.remote.WeatherDataDto;
import com.example.weatherapp.data.remote.WeatherDto;
import com.example.weatherapp.domin.weather.WeatherData;
import com.example.weatherapp.domin.weather.WeatherInfo;

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
        while(i <= weatherDataDto.time.size()){
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
                    weatherCode
            );
            day.add(weatherData);
            if(((i+1)%24d) == 0 ){
                week.put(iDay,day);
                iDay++;
                day.clear();
            }
            i++;
        }
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
            hour = now.getHour();
        }
        WeatherData currentWeatherData =  new WeatherData(
                currentWeatherDataList.get(0).time.withHour(hour),
                currentWeatherDataList.get(0).temperatureCelsius,
                currentWeatherDataList.get(0).pressure,
                currentWeatherDataList.get(0).windSpeed,
                currentWeatherDataList.get(0).humidity,
                currentWeatherDataList.get(0).weatherType
        );
        return new WeatherInfo(weatherDataMap,currentWeatherData);
    }
}
