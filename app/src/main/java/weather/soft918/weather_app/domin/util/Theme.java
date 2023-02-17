package weather.soft918.weather_app.domin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import weather.soft918.weather_app.R;
import weather.soft918.weather_app.databinding.LocationRvItemBinding;
import weather.soft918.weather_app.domin.model.LocationEntity;

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
        int currentNightMode = context.getResources().getConfiguration()
                .uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (location.id == sharedPref.getInt(Utility.CURRENT_LOCATION,-1)){
            if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
                binding.mainLayout.setBackground(ContextCompat.getDrawable(
                        context, R.drawable.location_rv_item_selected_background_day));
            }else{
                binding.mainLayout.setBackground(ContextCompat.getDrawable(
                        context, R.drawable.location_rv_item_selected_background_night));
            }
        }else {
            if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
                binding.mainLayout.setBackground(ContextCompat.getDrawable(
                        context, R.drawable.location_rv_item_background_day));
            }else{
                binding.mainLayout.setBackground(ContextCompat.getDrawable(
                        context, R.drawable.location_rv_item_background_night));
            }
        }
    }
}
