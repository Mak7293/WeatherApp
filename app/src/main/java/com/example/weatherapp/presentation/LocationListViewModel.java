package com.example.weatherapp.presentation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp.domin.util.GeocoderNominatim;
import com.example.weatherapp.domin.util.MaterialBottomSheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.Nullable;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ActivityContext;

@HiltViewModel
public class LocationListViewModel extends ViewModel {
    Application applicationContext;
    @Inject
    public LocationListViewModel(
            Application applicationContext
    ){
        this.applicationContext = applicationContext;
    }
    public enum LocationListEvent{
        SHOW_BOTTOM_SHEET
    }
    public void locationListEvent(
            LocationListEvent event,
            @Nullable FragmentActivity activity
    ){
        switch (event){
            case SHOW_BOTTOM_SHEET: {
                showBottomSheet(activity);
                break;
            }
        }
    }
    private void showBottomSheet(FragmentActivity activity){
        MaterialBottomSheet modalBottomSheet = new MaterialBottomSheet();
        modalBottomSheet.show(activity.getSupportFragmentManager(), MaterialBottomSheet.TAG);
    }



}
