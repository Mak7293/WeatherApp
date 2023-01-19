package com.example.weatherapp.domin.util;

import androidx.annotation.Nullable;

public class Resource <T>{
    T data;
    String message;
    public Resource(T data, String message){
        this.data = data;
        this.message = message;
    }
    public static class Success<T> extends Resource {
        public Success(T data, String message) {
            super(data,null);
        }
    }
    public static class Error<T> extends Resource{
        public Error(T data, String message) {
            super(data, message);
        }
    }
}
