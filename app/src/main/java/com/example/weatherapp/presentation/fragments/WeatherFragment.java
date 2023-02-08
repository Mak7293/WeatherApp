package com.example.weatherapp.presentation.fragments;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.transition.ChangeBounds;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.FragmentWeatherBinding;
import com.example.weatherapp.domin.adapters.WeatherAdapter;
import com.example.weatherapp.domin.util.Utility;
import com.example.weatherapp.presentation.WeatherState;
import com.example.weatherapp.presentation.view_models.WeatherViewModel;
import java.util.Map;
import java.util.Objects;


public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    public WeatherFragment() {
        // Required empty public constructor
    }
    private WeatherViewModel viewModel;
    public static WeatherState _weatherState;
    private ConstraintSet constraintSet = new ConstraintSet();
    private ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions()
            ,new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    result.entrySet().forEach(it ->{
                        String permissionName = it.getKey();
                        boolean isGranted = it.getValue();
                        Log.d("!!!!",result.toString());
                        Log.d("!!!!", String.valueOf(isGranted));
                        Log.d("!!!!",permissionName);
                        if(isGranted){
                            if(Objects.equals(permissionName, Manifest.permission.ACCESS_FINE_LOCATION)){
                                viewModel.loadWeatherInfo();
                            }
                        }else {
                            if(Objects.equals(permissionName, Manifest.permission.ACCESS_FINE_LOCATION)){
                                Log.d("!!!!","");
                                alertDialogForPermissionDenied();
                            }
                        }
                    });
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
        binding.tvGoToWeeklyForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_weatherState.weatherInfo!=null){
                    viewModel.weatherEvent(WeatherViewModel.WeatherEvent.DETAILS_FORECAST);
                }else {
                    Toast.makeText(requireContext(), "no weather forecast available to show.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        binding.llGetLatestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.weatherEvent(WeatherViewModel.WeatherEvent.GET_LATEST_DATA);
            }
        });

    }
    private void observeLiveData(){
        viewModel.state.observe(getViewLifecycleOwner(), new Observer<WeatherState>() {
            @Override
            public void onChanged(WeatherState weatherState) {
                if(weatherState.weatherInfo != null){
                    Log.d("observe", weatherState.weatherInfo.toString());
                    _weatherState = weatherState;
                    setupWeatherUi();
                    setupWeatherRv();
                }else{
                    _weatherState = weatherState;
                    Log.d("error","!!!");
                }
            }
        });
        viewModel.weatherUiState.observe(getViewLifecycleOwner(), new Observer<WeatherViewModel.WeatherUiState>(){
            @Override
            public void onChanged(WeatherViewModel.WeatherUiState weatherUiState) {
                Log.d("weatherUiState",weatherUiState.toString());
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
                    case INTERNET_CONNECTION_ERROR: {
                        hideWeatherData();
                        showErrorMessage(Utility.ERROR_INTERNET_CONNECTION);
                        break;
                    }
                }
            }
        });
    }
    private void hideWeatherData(){
        Log.d("invoke","111");
        binding.progressBarLoading.setVisibility(View.VISIBLE);
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

        ViewGroup.LayoutParams ivLayoutParams = binding.ivFrameLayout.getLayoutParams();
        ivLayoutParams.height = 0;
        ivLayoutParams.width = 0;
        binding.ivFrameLayout.setLayoutParams(ivLayoutParams);
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(750L);
        TransitionManager.beginDelayedTransition(binding.ivFrameLayout, transition);
        binding.ivFrameLayout.requestLayout();

        ViewGroup.LayoutParams rvLayoutParams = binding.rvTodayForecast.getLayoutParams();
        rvLayoutParams.height = 0;
        binding.rvTodayForecast.setLayoutParams(rvLayoutParams);
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(750L);
        TransitionManager.beginDelayedTransition(binding.rvTodayForecast, transition);
        binding.rvTodayForecast.requestLayout();


    }
    private void showWeatherData(){
        Log.d("invoke","222");
        binding.progressBarLoading.setVisibility(View.GONE);
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

        ViewGroup.LayoutParams rvLayoutParams = binding.rvTodayForecast.getLayoutParams();
        rvLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        binding.rvTodayForecast.setLayoutParams(rvLayoutParams);
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(750L);
        TransitionManager.beginDelayedTransition(binding.rvTodayForecast, transition);
        binding.rvTodayForecast.requestLayout();
    }
    private void showErrorMessage(String errorType){
        switch (errorType){
            case Utility.ERROR_DATA:     {
                binding.tvError.setVisibility(View.VISIBLE);
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.tvError.setText("Can not download weather data form server. please try again later.");
                break;
            }
            case Utility.ERROR_LOCATION: {
                binding.tvError.setVisibility(View.VISIBLE);
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.tvError.setText("Can not access to user location.");
                break;
            }
            case Utility.ERROR_INTERNET_CONNECTION: {
                binding.tvError.setVisibility(View.VISIBLE);
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.tvError.setText("Your Internet is not connected.");
                break;
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(viewModel.weatherUiState.getValue() == WeatherViewModel.WeatherUiState.DATA_AVAILABLE){
            showWeatherData();
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
        binding.tvLastUpdate.setText(generateCurrentTime());
    }
    private String generateCurrentTime(){
        String year = String.valueOf(_weatherState.weatherInfo.currentWeatherData.time.getYear());
        String month = String.valueOf(_weatherState.weatherInfo.currentWeatherData.time.getMonth());
        String day = String.valueOf(_weatherState.weatherInfo.currentWeatherData.time.getDayOfMonth());
        return day + "/" + month + "/" + year;
    }
    private void setupWeatherRv(){
        WeatherAdapter adapter = new WeatherAdapter(
                _weatherState.weatherInfo.weatherDataPerDay.get(0),requireContext());
        binding.rvTodayForecast.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false));
        binding.rvTodayForecast.setAdapter(adapter);
    }
    private void alertDialogForPermissionDenied(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Permission Denied");
        builder.setMessage(getResources().getString(R.string.all_time_location_permission));
        builder.setIcon(R.drawable.ic_alert);
        builder.setPositiveButton(getResources().getString(R.string.activate_permission),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                requireContext().getPackageName(),null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton( getResources().getString(R.string.negative),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}