package com.example.weatherapp.domin.location;

import android.location.Location;

import androidx.annotation.Nullable;

public interface LocationTracker {

    abstract @Nullable Location getCurrentLocation();
}
