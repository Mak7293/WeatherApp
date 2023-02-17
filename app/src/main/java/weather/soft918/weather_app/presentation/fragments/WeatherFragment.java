package weather.soft918.weather_app.presentation.fragments;


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
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.transition.ChangeBounds;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Toast;
import weather.soft918.weather_app.R;
import weather.soft918.weather_app.databinding.FragmentWeatherBinding;
import weather.soft918.weather_app.domin.adapters.WeatherAdapter;
import weather.soft918.weather_app.domin.model.LocationEntity;
import weather.soft918.weather_app.domin.util.TapTargetView;
import weather.soft918.weather_app.domin.util.Utility;
import weather.soft918.weather_app.domin.util.WeatherUiState;
import weather.soft918.weather_app.presentation.WeatherState;
import weather.soft918.weather_app.presentation.activities.MainActivity;
import weather.soft918.weather_app.presentation.view_models.WeatherViewModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;

    public WeatherFragment() {

    }
    @Inject
    SharedPreferences sharedPref;
    private WeatherViewModel viewModel;
    public static WeatherState state;
    private ConstraintSet constraintSet = new ConstraintSet();
    public ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions()
            ,new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    result.entrySet().forEach(it -> {
                        String permissionName = it.getKey();
                        boolean isGranted = it.getValue();
                        if(isGranted){
                            if(Objects.equals(permissionName, Manifest.permission.ACCESS_FINE_LOCATION)){
                                checkUpdateInStartOfApp();
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
                if(state.weatherInfo!=null){
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
        binding.llGetUserLocaleLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserLocaleLocation();
            }
        });

    }
    private void checkUpdateInStartOfApp(){
        Pair<Boolean,WeatherState> cash = viewModel.loadWeatherCashedWeatherData();
        boolean isCashedAvailable = cash.first;
        WeatherState weatherState = cash.second;
        LocalDateTime now = LocalDateTime.now();
        if (isCashedAvailable){
            if(now.getDayOfYear() ==
                    weatherState.weatherInfo.currentWeatherData.time.getDayOfYear()){
                viewModel.state.postValue(weatherState);
            }else {
                if(viewModel.isFirstTime){
                    int locationId = viewModel.getCurrentLocationId();
                    viewModel.loadWeatherInfo(locationId);
                }
                viewModel.isFirstTime = false;
            }
        }else {
            if(viewModel.isFirstTime){
                int locationId = viewModel.getCurrentLocationId();
                viewModel.loadWeatherInfo(locationId);
            }
            viewModel.isFirstTime = false;
        }
    }
    public void weatherFragmentTapTargetView(){
        List<View> view1 = new ArrayList<>();
        view1.add(binding.llGetLatestData);
        view1.add(binding.tvGoToWeeklyForecast);
        view1.add(binding.llGetUserLocaleLocation);
        TapTargetView.weatherFragmentTapTargetView(requireActivity(),view1);
    }
    private void observeLiveData(){
        viewModel.state.observe(getViewLifecycleOwner(), new Observer<WeatherState>() {
            @Override
            public void onChanged(WeatherState weatherState) {
                Log.d("observe", weatherState.toString());
                state = weatherState;
                switch (weatherState.state){
                    case LOADING: {
                        hideWeatherData();
                        break;
                    }
                    case DATA_AVAILABLE: {
                        setupWeatherUi(weatherState);
                        setupWeatherRv(weatherState);
                        showWeatherData();
                        break;
                    }
                    case DATA_ERROR: {
                        hideWeatherData();
                        showErrorMessage(weatherState.error);
                        break;
                    }
                    case LOCATION_ERROR: {
                        hideWeatherData();
                        showErrorMessage(weatherState.error);
                        break;
                    }
                    case INTERNET_CONNECTION_ERROR: {
                        hideWeatherData();
                        showErrorMessage(weatherState.error);
                        break;
                    }
                    case LOCATION_DISABLE:{
                        hideWeatherData();
                        showErrorMessage(weatherState.error);
                        break;
                    }
                    case ERROR_LONG_TIME_HTTP_REQUEST:{
                        hideWeatherData();
                        showErrorMessage(weatherState.error);
                        break;
                    }
                    case LOAD_CASHED_WEATHER_FORECAST:{
                        setupWeatherUi(weatherState);
                        setupWeatherRv(weatherState);
                        showWeatherData();
                        break;
                    }
                }
            }
        });
        MainActivity.menuAction.observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(String s) {
                if(Objects.equals(s, Utility.WEATHER_FRAGMENT)){
                    weatherFragmentTapTargetView();
                    MainActivity.menuAction.postValue("");
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
    private void showErrorMessage(String errorString){
        binding.tvError.setVisibility(View.VISIBLE);
        binding.progressBarLoading.setVisibility(View.GONE);
        binding.tvError.setText(errorString);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(viewModel.state.getValue().state == WeatherUiState.DATA_AVAILABLE){
            showWeatherData();
        }

    }
    private void getUserLocaleLocation(){
        viewModel.loadWeatherInfo(Utility.LOCALE_LOCATION_ID);
    }
    private void setupWeatherUi(WeatherState weatherState){
        int locationId = viewModel.getCurrentLocationId();
        if(locationId == Utility.LOCALE_LOCATION_ID){
            binding.tvLocation.setText("Local Location");
        }else {
            LocationEntity location = viewModel.getLocationById(locationId);
            binding.tvLocation.setText(location.locality);
        }
        binding.tvPressure.setText(weatherState.weatherInfo.currentWeatherData.pressure +" hpa");
        binding.tvHumidity.setText(weatherState.weatherInfo.currentWeatherData.humidity + " %");
        binding.tvWind.setText(weatherState.weatherInfo.currentWeatherData.windSpeed + " km/hr");
        binding.tvWeatherState.setText(
                weatherState.weatherInfo.currentWeatherData.weatherType.weatherDesc
        );
        binding.ivWeatherState.setImageDrawable(
                ContextCompat.getDrawable(requireContext(),
                        weatherState.weatherInfo.currentWeatherData.weatherType.iconRes)
        );
        binding.tvWeatherTemperature.setText(
                weatherState.weatherInfo.currentWeatherData.temperatureCelsius + " °C");
        binding.tvLastUpdate.setText(generateCurrentTime(viewModel.state.getValue()));
        binding.tvTime.setText(weatherState.weatherInfo.currentWeatherData.time.toLocalTime().toString());
    }
    private String generateCurrentTime(WeatherState weatherState){
        String year = String.valueOf(weatherState.weatherInfo.currentWeatherData.time.getYear());
        String month = String.valueOf(weatherState.weatherInfo.currentWeatherData.time.getMonth());
        String day = String.valueOf(weatherState.weatherInfo.currentWeatherData.time.getDayOfMonth());
        return day + "/" + month + "/" + year;
    }
    private void setupWeatherRv(WeatherState weatherState){
        WeatherAdapter adapter = new WeatherAdapter(
                weatherState.weatherInfo.weatherDataPerDay.get(0),requireContext());
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