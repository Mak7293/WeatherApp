package com.example.weatherapp.domin.util;

import androidx.annotation.Nullable;

public class Resource <T>{
    @Nullable
    public T data;
    @Nullable
    public String message;
    public Resource(@Nullable T data, @Nullable String message){
        this.data = data;
        this.message = message;
    }
    public static class Success<T> extends Resource {
        public Success(@Nullable T data,@Nullable String message) {
            super(data,null);
        }
    }
    public static class Error<T> extends Resource{
        public Error(@Nullable T data,@Nullable String message) {
            super(data, message);
        }
    }
}
