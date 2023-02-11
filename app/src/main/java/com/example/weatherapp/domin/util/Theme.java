package com.example.weatherapp.domin.util;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class Theme {
    public Theme(){

    }
    public static void setTheme(String theme, SharedPreferences sharedPref){
        switch (theme){
            case Utility.THEME_DAY: {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPref.edit().putString(Utility.THEME_KEY,Utility.THEME_DAY).apply();
                break;
            }
            case Utility.THEME_NIGHT: {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPref.edit().putString(Utility.THEME_KEY,Utility.THEME_NIGHT).apply();
                break;
            }
            case Utility.THEME_DEFAULT: {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                sharedPref.edit().putString(Utility.THEME_KEY,Utility.THEME_DEFAULT).apply();
                break;
            }
        }
    }

}
