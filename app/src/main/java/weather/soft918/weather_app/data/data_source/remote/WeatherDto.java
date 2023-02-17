package weather.soft918.weather_app.data.data_source.remote;

public class WeatherDto {
    public WeatherDataDto hourly;
    public WeatherDto(WeatherDataDto hourly){
        this.hourly = hourly;
    }
}
