package com.example.weatherapp.domin.util;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

import java.util.Map;
import java.util.Objects;

public class Utility {
    public Utility(){

    }
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String BASE_URL = "https://api.open-meteo.com/";
    public static final String URL = "v1/forecast?latitude=52.52&longitude=13.41&timezone=auto&hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl";
    public static final String ERROR_DATA = "error_data";
    public static final String ERROR_LOCATION = "error_location";
    public static final String ERROR_INTERNET_CONNECTION = "error_internet_connection";
    public static final String TEMPERATURE = "temperature";
    public static final String PRESSURE = "pressure";
    public static final String WIND_SPEED = "wind_speed";
    public static final String HUMIDITY = "humidity";
    public static final int LOCALE_LOCATION_ID = -2;
    public static final String SHARED_PREF = "shared_pref";
    public static final String CURRENT_LOCATION = "current_location";
    public static final String THEME_KEY = "theme_key";
    public static final String THEME_DAY = "theme_day";
    public static final String THEME_NIGHT = "theme_night";
    public static final String THEME_DEFAULT = "theme_default";


}
