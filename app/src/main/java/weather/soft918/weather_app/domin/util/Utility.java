package weather.soft918.weather_app.domin.util;



public class Utility {
    public Utility(){

    }
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String BASE_URL = "https://api.open-meteo.com/";
    public static final String URL = "v1/forecast?latitude=52.52&longitude=13.41&timezone=auto&hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl";
    public static final String TEMPERATURE = "temperature";
    public static final String PRESSURE = "pressure";
    public static final String WIND_SPEED = "wind_speed";
    public static final String HUMIDITY = "humidity";
    public static final String CASHED_WEATHER_FORECAST = "cashed_weather_forecast";
    public static final String NO_CASHED_WEATHER_FORECAST = "no_cashed_weather_forecast";
    public static final int LOCALE_LOCATION_ID = -2;
    public static final String SHARED_PREF = "shared_pref";
    public static final String CURRENT_LOCATION = "current_location";
    public static final String THEME_KEY = "theme_key";
    public static final String THEME_DAY = "theme_day";
    public static final String THEME_NIGHT = "theme_night";
    public static final String THEME_DEFAULT = "theme_default";
    public static final String WEATHER_FRAGMENT = "weather_fragment";
    public static final String LOCATION_LIST_FRAGMENT = "location_list_fragment";
}
