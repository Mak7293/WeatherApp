package com.example.weatherapp.presentation.view_models;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.domin.adapters.LocationListAdapter;
import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.domin.repository.Repository;
import com.example.weatherapp.domin.util.MaterialBottomSheet;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.Nullable;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class LocationListViewModel extends ViewModel {
    Application applicationContext;
    Repository repository;
    private final ScheduledExecutorService backgroundExecutor =
            Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService backgroundExecutor_one =
            Executors.newSingleThreadScheduledExecutor();
    @Inject
    public LocationListViewModel(
            Application applicationContext,
            Repository repository
    ){
        this.applicationContext = applicationContext;
        this.repository = repository;
    }
    public enum LocationListEvent{
        SHOW_BOTTOM_SHEET,
        SAVE_LOCATION
    }
    public void locationListEvent(
            LocationListEvent event,
            @Nullable FragmentActivity activity,
            @Nullable LocationEntity location
    ){
        switch (event){
            case SHOW_BOTTOM_SHEET: {
                showBottomSheet(activity);
                //Log.d("size" , repository.getAllLocation().toString());
                break;
            }
            case SAVE_LOCATION: {
                saveLocationToDatabase(location);
                break;
            }
        }
    }
    private void showBottomSheet(FragmentActivity activity){
        MaterialBottomSheet modalBottomSheet = new MaterialBottomSheet();
        modalBottomSheet.show(activity.getSupportFragmentManager(), MaterialBottomSheet.TAG);
    }
    private void saveLocationToDatabase(LocationEntity location){
        Executor mainExecutor = ContextCompat.getMainExecutor(applicationContext);
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                repository.insertLocation(location);
                mainExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(applicationContext.getApplicationContext(),
                                "Location Saved", Toast.LENGTH_SHORT).show();
                    }
                });
                backgroundExecutor.shutdown();
            }
        });
    }
    public LiveData<List<LocationEntity>> getAllData(){
        return repository.getAllLocation();
    };

    @Override
    protected void onCleared() {
        super.onCleared();
        backgroundExecutor_one.shutdown();
    }
}
