package weather.soft918.weather_app.presentation.Events;

import androidx.annotation.Nullable;

public class Event<T>{

    @Nullable
    public T data;
    public Event(@Nullable T data){
        this.data = data;
    }
    public static class DetailsForecast<T> extends Event{
        public DetailsForecast(@Nullable T data){
            super(data);
        }
    }
    public static class GetData<T> extends Event{
        public GetData(@Nullable T data){
            super(data);
        }
    }

}
