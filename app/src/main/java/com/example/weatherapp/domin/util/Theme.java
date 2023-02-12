package com.example.weatherapp.domin.util;

import static android.content.res.Configuration.UI_MODE_NIGHT_MASK;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.LocationRvItemBinding;
import com.example.weatherapp.domin.adapters.LocationListAdapter;
import com.example.weatherapp.domin.model.LocationEntity;

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
    public static void setLocationListRecyclerViewTheme(
            Context context,
            SharedPreferences sharedPref,
            LocationEntity location,
            LocationRvItemBinding binding
    ){
        if (location.id == sharedPref.getInt(Utility.CURRENT_LOCATION,-1)){
            Log.d("!!!!",sharedPref.getString(Utility.THEME_KEY, Utility.THEME_DEFAULT));
            if(sharedPref.getString(Utility.THEME_KEY, Utility.THEME_DEFAULT).equals(Utility.THEME_DAY)){
                binding.mainLayout.setBackground(ContextCompat.getDrawable(
                        context, R.drawable.location_rv_item_selected_background_day));
            }else if(sharedPref.getString(Utility.THEME_KEY, Utility.THEME_DEFAULT).equals(Utility.THEME_NIGHT)){
                binding.mainLayout.setBackground(ContextCompat.getDrawable(
                        context, R.drawable.location_rv_item_selected_background_night));
            }else if(sharedPref.getString(Utility.THEME_KEY, Utility.THEME_DEFAULT).equals(Utility.THEME_DEFAULT)){
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                    binding.mainLayout.setBackground(ContextCompat.getDrawable(
                            context, R.drawable.location_rv_item_selected_background_day));
                }else {
                    int currentNightMode = context.getResources().getConfiguration().uiMode & UI_MODE_NIGHT_MASK;
                    if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
                        binding.mainLayout.setBackground(ContextCompat.getDrawable(
                                context, R.drawable.location_rv_item_selected_background_day));
                    }else if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
                        binding.mainLayout.setBackground(ContextCompat.getDrawable(
                                context, R.drawable.location_rv_item_selected_background_night));
                    }
                }
            };
        }else {
            if(sharedPref.getString(Utility.THEME_KEY, Utility.THEME_DEFAULT).equals(Utility.THEME_DAY)){
                binding.mainLayout.setBackground(ContextCompat.getDrawable(
                        context, R.drawable.location_rv_item_background_day));
            }else if(sharedPref.getString(Utility.THEME_KEY, Utility.THEME_DEFAULT).equals(Utility.THEME_NIGHT)){
                binding.mainLayout.setBackground(ContextCompat.getDrawable(
                        context, R.drawable.location_rv_item_background_night));
            }else if(sharedPref.getString(Utility.THEME_KEY, Utility.THEME_DEFAULT).equals(Utility.THEME_DEFAULT)){
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                    binding.mainLayout.setBackground(ContextCompat.getDrawable(
                            context, R.drawable.location_rv_item_background_day));
                }else {
                    int currentNightMode = context.getResources().getConfiguration().uiMode & UI_MODE_NIGHT_MASK;
                    if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
                        binding.mainLayout.setBackground(ContextCompat.getDrawable(
                                context, R.drawable.location_rv_item_background_day));
                    }else if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
                        binding.mainLayout.setBackground(ContextCompat.getDrawable(
                                context, R.drawable.location_rv_item_background_night));
                    }
                }
            }
        }
    }
}
