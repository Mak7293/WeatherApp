package com.example.weatherapp.presentation.fragment;

import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.FragmentWeatherBinding;
import com.example.weatherapp.domin.adapters.WeatherRecyclerView;
import com.example.weatherapp.domin.util.Utility;
import com.example.weatherapp.presentation.WeatherState;
import com.example.weatherapp.presentation.WeatherViewModel;
import java.util.Map;
import java.util.concurrent.Executor;


public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    public WeatherFragment() {
        // Required empty public constructor
    }
    private WeatherViewModel viewModel;
    private WeatherState _weatherState;
    private ConstraintSet constraintSet = new ConstraintSet();
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
        viewModel.weatherUiState.observe(getViewLifecycleOwner(), new Observer<WeatherViewModel.WeatherUiState>(){
            @Override
            public void onChanged(WeatherViewModel.WeatherUiState weatherUiState) {
                switch (weatherUiState){
                    case LOADING: {
                        hideWeatherData();
                        break;
                    }
                    case DATA_AVAILABLE: {
                        showWeatherData();
                        break;
                    }
                    case DATA_ERROR: {
                        showErrorMessage(Utility.ERROR_DATA);
                        break;
                    }
                    case LOCATION_ERROR: {
                        showErrorMessage(Utility.ERROR_LOCATION);
                        break;
                    }
                }
            }
        });
    }
    private void hideWeatherData(){
        Log.d("invoke","111");
        binding.tvError.setVisibility(View.INVISIBLE);
        constraintSet.clone(binding.mainContentConstraintLayout);
        constraintSet.connect(R.id.main_content_linear_layout,ConstraintSet.TOP,
                R.id.main_content_constraint_layout,ConstraintSet.BOTTOM);
        constraintSet.connect(R.id.ll_location,ConstraintSet.END,
                R.id.main_content_constraint_layout,ConstraintSet.START);
        constraintSet.connect(R.id.ll_weather_temperature_state,ConstraintSet.START,
                R.id.main_content_constraint_layout,ConstraintSet.END);
        constraintSet.clear(R.id.ll_location,ConstraintSet.START);
        constraintSet.clear(R.id.main_content_linear_layout,ConstraintSet.BOTTOM);
        constraintSet.clear(R.id.ll_weather_temperature_state,ConstraintSet.END);


        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1000L);
        TransitionManager.beginDelayedTransition(binding.mainContentLinearLayout, transition);
        TransitionManager.beginDelayedTransition(binding.llLocation, transition);
        TransitionManager.beginDelayedTransition(binding.llWeatherTemperatureState, transition);
        constraintSet.applyTo(binding.mainContentConstraintLayout);


        ViewGroup.LayoutParams layoutParams = binding.ivFrameLayout.getLayoutParams();
        layoutParams.height = 0;
        layoutParams.width = 0;
        binding.ivFrameLayout.setLayoutParams(layoutParams);
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(750L);
        TransitionManager.beginDelayedTransition(binding.ivFrameLayout, transition);
        binding.ivFrameLayout.requestLayout();

    }
    private void showWeatherData(){
        Log.d("invoke","222");
        binding.tvError.setVisibility(View.INVISIBLE);
        constraintSet.clone(binding.mainContentConstraintLayout);
        constraintSet.connect(R.id.main_content_linear_layout,ConstraintSet.BOTTOM,
                R.id.main_content_constraint_layout,ConstraintSet.BOTTOM);
        constraintSet.connect(R.id.ll_location,ConstraintSet.START,
                R.id.main_content_constraint_layout,ConstraintSet.START);
        constraintSet.connect(R.id.ll_weather_temperature_state,ConstraintSet.START,
                R.id.main_content_constraint_layout,ConstraintSet.START);
        constraintSet.connect(R.id.ll_weather_temperature_state,ConstraintSet.END,
                R.id.main_content_constraint_layout,ConstraintSet.END);
        constraintSet.clear(R.id.ll_location,ConstraintSet.END);
        constraintSet.clear(R.id.main_content_linear_layout,ConstraintSet.TOP);

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1000L);
        TransitionManager.beginDelayedTransition(binding.mainContentLinearLayout, transition);
        TransitionManager.beginDelayedTransition(binding.llLocation, transition);
        TransitionManager.beginDelayedTransition(binding.llWeatherTemperatureState, transition);
        constraintSet.applyTo(binding.mainContentConstraintLayout);

        ViewGroup.LayoutParams layoutParams = binding.ivFrameLayout.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        binding.ivFrameLayout.setLayoutParams(layoutParams);
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(750L);
        TransitionManager.beginDelayedTransition(binding.ivFrameLayout, transition);
        binding.ivFrameLayout.requestLayout();
    }
    private void showErrorMessage(String errorType){
        switch (errorType){
            case Utility.ERROR_DATA:     {
                binding.tvError.setVisibility(View.VISIBLE);
                binding.tvError.setText("Can not download weather data form server. please try again later.");
                break;
            }
            case Utility.ERROR_LOCATION: {
                binding.tvError.setVisibility(View.VISIBLE);
                binding.tvError.setText("Can not access to user location.");
                break;
            }
        }
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
        binding.tvWeatherTemperature.setText(
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