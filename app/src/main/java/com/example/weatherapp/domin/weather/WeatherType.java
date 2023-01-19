package com.example.weatherapp.domin.weather;

import androidx.annotation.DrawableRes;

import com.example.weatherapp.R;

public class WeatherType {
    String weatherDesc;
    int iconRes;
    
    public WeatherType(
            String weatherDesc,
            @DrawableRes
            int iconRes
    ){
        this.weatherDesc = weatherDesc;
        this.iconRes = iconRes;
    }
    WeatherType ClearSky = new WeatherType(
             "Clear sky",
              R.drawable.ic_clear_sky
    );
    WeatherType MainlyClear = new WeatherType(
             "Mainly clear",
              R.drawable.ic_partly_cloudly
    );
    WeatherType PartlyCloudy = new WeatherType(
             "Partly cloudy",
              R.drawable.ic_partly_cloudly
    );
    WeatherType Overcast = new WeatherType(
             "Overcast",
              R.drawable.ic_partly_cloudly
    );
    WeatherType Foggy = new WeatherType(
             "Foggy",
              R.drawable.ic_fog
    );
    WeatherType DepositingRimeFog = new WeatherType(
             "Depositing rime fog",
              R.drawable.ic_very_cloudly
    );
    WeatherType LightDrizzle = new WeatherType(
             "Light drizzle",
              R.drawable.ic_rain_shower
    );
    WeatherType ModerateDrizzle = new WeatherType(
             "Moderate drizzle",
              R.drawable.ic_rain_shower
    );
    WeatherType DenseDrizzle = new WeatherType(
             "Dense drizzle",
              R.drawable.ic_rain_shower
    );
    WeatherType LightFreezingDrizzle = new WeatherType(
             "Slight freezing drizzle",
              R.drawable.ic_rain
    );
    WeatherType DenseFreezingDrizzle = new WeatherType(
             "Dense freezing drizzle",
              R.drawable.ic_rain
    );
    WeatherType SlightRain = new WeatherType(
             "Slight rain",
              R.drawable.ic_rain
    );
    WeatherType ModerateRain = new WeatherType(
             "Rainy",
              R.drawable.ic_rain
    );
    WeatherType HeavyRain = new WeatherType(
             "Heavy rain",
              R.drawable.ic_rain
    );
    WeatherType HeavyFreezingRain = new WeatherType(
             "Heavy freezing rain",
              R.drawable.ic_rain
    );
    WeatherType SlightSnowFall = new WeatherType(
             "Slight snow fall",
              R.drawable.ic_snow
    );
    WeatherType ModerateSnowFall = new WeatherType(
             "Moderate snow fall",
              R.drawable.ic_snow
    );
    WeatherType HeavySnowFall = new WeatherType(
             "Heavy snow fall",
             R.drawable.ic_snow
    );
    WeatherType SnowGrains = new WeatherType(
             "Snow grains",
              R.drawable.ic_snow
    );
    WeatherType SlightRainShowers = new WeatherType(
             "Slight rain showers",
              R.drawable.ic_rain_shower
    );
    WeatherType ModerateRainShowers = new WeatherType(
             "Moderate rain showers",
              R.drawable.ic_rain_shower
    );
    WeatherType ViolentRainShowers = new WeatherType(
             "Violent rain showers",
              R.drawable.ic_rain_shower
    );
    WeatherType SlightSnowShowers = new WeatherType(
             "Light snow showers",
              R.drawable.ic_snow
    );
    WeatherType HeavySnowShowers = new WeatherType(
             "Heavy snow showers",
              R.drawable.ic_snow
    );
    WeatherType ModerateThunderstorm = new WeatherType(
             "Moderate thunderstorm",
              R.drawable.ic_thunder_storm
    );
    WeatherType SlightHailThunderstorm = new WeatherType(
             "Thunderstorm with slight hail",
              R.drawable.ic_thunder_storm
    );
    WeatherType HeavyHailThunderstorm = new WeatherType(
             "Thunderstorm with heavy hail",
              R.drawable.ic_thunder_storm
    );
    WeatherType fromWMO(int code) {
        WeatherType state;
        switch (code) {
            case 0 : state = ClearSky; break;
            case 1 : state = MainlyClear; break;
            case 2 : state = PartlyCloudy; break;
            case 3 : state = Overcast; break;
            case 45 : state = Foggy; break;
            case 48 : state = DepositingRimeFog; break;
            case 51 : state = LightDrizzle; break;
            case 53 : state = ModerateDrizzle; break;
            case 55 : state = DenseDrizzle; break;
            case 56 : state = LightFreezingDrizzle; break;
            case 57 : state = DenseFreezingDrizzle; break;
            case 61 : state = SlightRain; break;
            case 63 : state = ModerateRain; break;
            case 65 : state = HeavyRain; break;
            case 66 : state = LightFreezingDrizzle; break;
            case 67 : state = HeavyFreezingRain; break;
            case 71 : state = SlightSnowFall; break;
            case 73 : state = ModerateSnowFall; break;
            case 75 : state = HeavySnowFall; break;
            case 77 : state = SnowGrains; break;
            case 80 : state = SlightRainShowers; break;
            case 81 : state = ModerateRainShowers; break;
            case 82 : state = ViolentRainShowers; break;
            case 85 : state = SlightSnowShowers; break;
            case 86 : state = HeavySnowShowers; break;
            case 95 : state = ModerateThunderstorm; break;
            case 96 : state = SlightHailThunderstorm; break;
            case 99 : state = HeavyHailThunderstorm; break;
            default : state = ClearSky; break;
        }
        return state;
    }
}
