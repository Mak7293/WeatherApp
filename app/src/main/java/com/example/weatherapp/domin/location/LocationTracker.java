package com.example.weatherapp.domin.location;

import android.location.Location;

import androidx.annotation.Nullable;

import java.util.HashMap;

public interface LocationTracker {

    abstract @Nullable HashMap<String,Double> getCurrentLocation() throws InterruptedException;
}
