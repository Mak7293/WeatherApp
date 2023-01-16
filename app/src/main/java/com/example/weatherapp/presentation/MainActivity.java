package com.example.weatherapp.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.ViewPagerAdapter;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.presentation.fragment.LocationListFragment;
import com.example.weatherapp.presentation.fragment.WeatherFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WeatherApp);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPager(binding.tabViewpager);
        binding.tabTableLayout.setupWithViewPager(binding.tabViewpager);

    }
    private void setupViewPager(ViewPager viewPager){
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new WeatherFragment());
        fragmentList.add(new LocationListFragment());

        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentList, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}