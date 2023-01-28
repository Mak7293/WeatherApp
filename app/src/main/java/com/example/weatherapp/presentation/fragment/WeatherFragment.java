package com.example.weatherapp.presentation.fragment;

import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.weatherapp.databinding.FragmentWeatherBinding;
import com.example.weatherapp.domin.adapters.WeatherRecyclerView;
import com.example.weatherapp.presentation.WeatherState;
import com.example.weatherapp.presentation.WeatherViewModel;

import java.util.Map;


public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    public WeatherFragment() {
        // Required empty public constructor
    }
    private WeatherViewModel viewModel;
    private WeatherState _weatherState;

    private ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions()
            ,new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    viewModel.loadWeatherInfo();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        observeLiveData();
        permissionLauncher.launch(new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }
    private void observeLiveData(){
        viewModel.state.observe(getViewLifecycleOwner(), new Observer<WeatherState>() {
            @Override
            public void onChanged(WeatherState weatherState) {
                Log.d("observe", weatherState.weatherInfo.toString());
                if(weatherState.weatherInfo != null){
                    _weatherState = weatherState;
                    setupWeatherUi();
                    setupWeatherRv();
                }else{
                    Log.d("error","!!!");
                }
            }
        });
    }
    private void setupWeatherUi(){
        binding.tvPressure.setText(_weatherState.weatherInfo.currentWeatherData.pressure+" hpa");
        binding.tvHumidity.setText(_weatherState.weatherInfo.currentWeatherData.humidity+ " %");
        binding.tvWind.setText(_weatherState.weatherInfo.currentWeatherData.windSpeed+ " km/hr");
        binding.tvWeatherState.setText(
                _weatherState.weatherInfo.currentWeatherData.weatherType.weatherDesc
        );
        binding.ivWeatherState.setImageDrawable(
                ContextCompat.getDrawable(requireContext(),
                        _weatherState.weatherInfo.currentWeatherData.weatherType.iconRes)
        );
        binding.tvWeatherTemprature.setText(
                _weatherState.weatherInfo.currentWeatherData.temperatureCelsius + " °C");
    }
    private void setupWeatherRv(){
        WeatherRecyclerView adapter = new WeatherRecyclerView(
                _weatherState.weatherInfo.weatherDataPerDay.get(0),requireContext());
        binding.rvTodayForecast.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false));
        binding.rvTodayForecast.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}