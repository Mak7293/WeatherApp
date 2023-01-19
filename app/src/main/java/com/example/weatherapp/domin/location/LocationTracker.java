package com.example.weatherapp.domin.location;

import android.location.Location;

public interface LocationTracker {

    abstract Location getCurrentLocation() throws InterruptedException;
}
