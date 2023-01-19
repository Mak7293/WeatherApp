package com.example.weatherapp.data.location;



import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.content.ContextCompat;


import com.example.weatherapp.domin.location.LocationTracker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DefaultLocationTracker implements LocationTracker {

    FusedLocationProviderClient locationClient;
    Context applicationContext;
    @Inject
    public DefaultLocationTracker(
            FusedLocationProviderClient fusedLocationProviderClient,
            Context applicationContext
    ){
        this.locationClient = fusedLocationProviderClient;
        this.applicationContext = applicationContext;
    }

    @Override
    public Location getCurrentLocation() throws InterruptedException {
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

        final Location[] location = new Location[1];
        @SuppressLint("MissingPermission")
        Observable<Task<Location>> locationObservable =
                Observable.just(locationClient.getLastLocation());
        Observer<Task<Location>> locationObserver = new DisposableObserver<Task<Location>>(){

            @Override
            public void onNext(@NonNull Task<Location> locationTask) {
                if (locationTask.isComplete()){
                    if (locationTask.isSuccessful()){
                        location[0] = locationTask.getResult();
                        notify();
                        dispose();
                    }else{
                        location[0] = null;
                        notify();
                        dispose();
                    }
                }
            }
            @Override
            public void onError(@NonNull Throwable e) {
                location[0] = null;
                notify();
                dispose();
            }
            @Override
            public void onComplete() {
                notify();
                dispose();
            }

        };
        locationObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(locationObserver);

        wait();
        return location[0];
    }
}
