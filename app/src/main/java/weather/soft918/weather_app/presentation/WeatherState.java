package weather.soft918.weather_app.presentation;

import androidx.annotation.Nullable;
import weather.soft918.weather_app.data.repository.weather.WeatherInfo;
import weather.soft918.weather_app.domin.util.WeatherUiState;


public class WeatherState {
    @Nullable
    public WeatherInfo weatherInfo;
    @Nullable
    public String error;
    @Nullable
    public WeatherUiState state;
    public WeatherState(
            @Nullable WeatherInfo weatherInfo,
            @Nullable String error,
            @Nullable WeatherUiState state
    ){
        this.weatherInfo = weatherInfo;
        this.error = error;
        this.state = state;
    }

}
