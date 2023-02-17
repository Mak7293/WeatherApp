package weather.soft918.weather_app.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import weather.soft918.weather_app.R;
import weather.soft918.weather_app.domin.adapters.ViewPagerAdapter;
import weather.soft918.weather_app.databinding.ActivityMainBinding;
import weather.soft918.weather_app.domin.util.Theme;
import weather.soft918.weather_app.domin.util.Utility;
import weather.soft918.weather_app.presentation.fragments.LocationListFragment;
import weather.soft918.weather_app.presentation.fragments.WeatherFragment;
import weather.soft918.weather_app.presentation.view_models.WeatherViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WeatherViewModel viewModel;
    private int _locationId;
    private Menu menu;
    @Inject
    SharedPreferences sharedPref;
    private String currentTheme;
    public static View view;

    public static MutableLiveData<String> menuAction = new MutableLiveData<String>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WeatherApp);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyEnglishLanguage(Locale.ENGLISH);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        _locationId = viewModel.getCurrentLocationId();

        view = binding.rgTheme;
        currentTheme = sharedPref.getString(Utility.THEME_KEY,Utility.THEME_DEFAULT);
        Theme.setTheme(currentTheme,sharedPref);
        themeTypeChecked();
        setupViewPager(binding.tabViewpager);
        binding.tabTableLayout.setupWithViewPager(binding.tabViewpager);
        binding.tabViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // we don't need this methode
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    updateWeatherData();
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {
                // we don't need this methode
            }
        });
        binding.rgTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbDay: {
                        Theme.setTheme(Utility.THEME_DAY,sharedPref);
                        break;
                    }
                    case R.id.rbNight:{
                        Theme.setTheme(Utility.THEME_NIGHT,sharedPref);
                        break;
                    }
                    case R.id.rbDefault:{
                        Theme.setTheme(Utility.THEME_DEFAULT,sharedPref);
                        break;
                    }
                }
            }
        });
        setToolbar();
    }
    private void applyEnglishLanguage(Locale locale){
        Configuration config = this.getResources().getConfiguration();
        Locale sysLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.getLocales().get(0);
        } else {
            //Legacy
            sysLocale = config.locale;
        }
        if (sysLocale.getLanguage() != locale.getLanguage()) {
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale);
            } else {
                //Legacy
                config.locale = locale;
            }
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
    }

    private void setToolbar(){
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
    }
    private void themeTypeChecked(){
        switch (currentTheme) {
            case Utility.THEME_DEFAULT : {
                binding.rbDefault.setChecked(true);
                break;
            }
            case Utility.THEME_DAY     : {
                binding.rbDay.setChecked(true);
                break;
            }
            case Utility.THEME_NIGHT   : {
                binding.rbNight.setChecked(true);
                break;
            }
        }
    }
    private void updateWeatherData(){
        Log.d("@@@",String.valueOf(viewModel.getCurrentLocationId()));
        int locationId = viewModel.getCurrentLocationId();
        if (locationId !=_locationId){
            viewModel.loadWeatherInfo(locationId);
            _locationId = locationId;
        }
    }
    private void setupViewPager(ViewPager viewPager){
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new WeatherFragment());
        fragmentList.add(new LocationListFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentList, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menue_information,menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int currentNightMode = getResources().getConfiguration()
                .uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            menu.getItem(0).getIcon().setTint(
                    ContextCompat.getColor(this,R.color.dark_custom_1));
        }else{
            menu.getItem(0).getIcon().setTint(
                    ContextCompat.getColor(this,R.color.dark_custom_4));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.information) {
            switch (binding.tabTableLayout.getSelectedTabPosition()){
                case 0:{
                    menuAction.postValue(Utility.WEATHER_FRAGMENT);
                    break;
                }
                case 1:{
                    menuAction.postValue(Utility.LOCATION_LIST_FRAGMENT);
                    break;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}