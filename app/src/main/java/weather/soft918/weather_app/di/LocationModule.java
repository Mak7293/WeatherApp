package weather.soft918.weather_app.di;

import weather.soft918.weather_app.data.location.DefaultLocationTracker;
import weather.soft918.weather_app.domin.location.LocationTracker;
import javax.inject.Singleton;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract LocationTracker bindLocationTracker(DefaultLocationTracker defaultLocationTracker );
}
