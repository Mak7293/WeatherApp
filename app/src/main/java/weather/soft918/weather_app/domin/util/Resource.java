package weather.soft918.weather_app.domin.util;

import androidx.annotation.Nullable;


public class Resource <T>{
    @Nullable
    public T data;
    @Nullable
    public String message;
    @Nullable
    public WeatherUiState state;
    public Resource(@Nullable T data, @Nullable String message, @Nullable WeatherUiState state){
        this.data = data;
        this.message = message;
        this.state = state;
    }
    public static class Success<T> extends Resource {
        public Success(@Nullable T data,@Nullable String message, @Nullable WeatherUiState state) {
            super(data,null, state);
        }
    }
    public static class Error<T> extends Resource{
        public Error(@Nullable T data,@Nullable String message, @Nullable WeatherUiState state) {
            super(data, message, state);
        }
    }
}
