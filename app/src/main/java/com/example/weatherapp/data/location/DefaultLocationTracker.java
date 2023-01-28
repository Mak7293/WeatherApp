package com.example.weatherapp.data.location;



import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import com.example.weatherapp.domin.location.LocationTracker;
import com.example.weatherapp.domin.util.Resource;
import com.example.weatherapp.domin.util.Utility;
import com.example.weatherapp.domin.weather.WeatherInfo;
import com.example.weatherapp.presentation.WeatherState;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DefaultLocationTracker implements LocationTracker {

    FusedLocationProviderClient locationClient;
    Application applicationContext;
    @Inject
    public DefaultLocationTracker(
            FusedLocationProviderClient fusedLocationProviderClient,
            Application applicationContext
    ){
        this.locationClient = fusedLocationProviderClient;
        this.applicationContext = applicationContext;
    }

    @Override
    public HashMap<String,Double> getCurrentLocation() throws InterruptedException {
        boolean hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
                applicationContext,
                ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
        boolean hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
                applicationContext,
                ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        LocationManager locationManager = (LocationManager) applicationContext.getSystemService(
                Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission
                || !isGpsEnabled){
            return null;
        }
        @SuppressLint("MissingPermission")
        Task<Location> locationTask = locationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                    @NonNull
                    @Override
                    public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                        return null;
                    }
                    @Override
                    public boolean isCancellationRequested() {
                        return false;
                    }
                });
        HashMap<String,Double> location = new HashMap<>();
        AtomicBoolean processed = new AtomicBoolean(true) ;

        locationTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                String result;
                if(o == null){
                    location.put(Utility.LATITUDE,null);
                    location.put(Utility.LONGITUDE,null);
                    result = "null";
                }else {
                    Location _location = (Location) o;
                    location.put(Utility.LATITUDE,_location.getLatitude());
                    location.put(Utility.LONGITUDE,_location.getLongitude());
                    result = o.toString();
                }
                Log.d("taskSuccess",result);
                synchronized (processed) {
                    processed.notify();
                }
                Log.d("thread1",Thread.currentThread().getName());
            }
        });
        locationTask.addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.d("taskCanceled","!!!!");
                location.put(Utility.LATITUDE,null);
                location.put(Utility.LONGITUDE,null);
                synchronized (processed) {
                    processed.notify();
                }
            }
        });
        locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Log.d("taskComplete",task.toString());
                synchronized (processed) {
                    processed.notify();
                }
            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("taskFailure",e.toString());
                location.put(Utility.LATITUDE,null);
                location.put(Utility.LONGITUDE,null);
                synchronized (processed) {
                    processed.notify();
                }
            }
        });
        Log.d("thread2",Thread.currentThread().getName());
        synchronized (processed) {
            processed.wait();
        }
        return location;
    }
}
