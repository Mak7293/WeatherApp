package weather.soft918.weather_app.di;


import static android.content.Context.MODE_PRIVATE;
import android.app.Application;
import android.content.SharedPreferences;
import androidx.room.Room;
import weather.soft918.weather_app.data.data_source.local.LocationDatabase;
import weather.soft918.weather_app.data.data_source.remote.WeatherApi;
import weather.soft918.weather_app.data.repository.RepositoryImp;
import weather.soft918.weather_app.domin.repository.Repository;
import weather.soft918.weather_app.domin.util.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
@InstallIn(SingletonComponent.class )
public class AppModule {
    @Provides
    @Singleton
    OkHttpClient provideHttpClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }
    @Provides
    @Singleton
    WeatherApi provideWeatherApi(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(Utility.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
                .create(WeatherApi.class);
    }
    @Provides
    @Singleton
    FusedLocationProviderClient provideFusedLocationProviderClient(Application app){
        return LocationServices.getFusedLocationProviderClient(app);
    }
    @Provides
    @Singleton
    LocationDatabase provideLocationDatabase(Application app){
        return Room.databaseBuilder(
                app,
                LocationDatabase.class,
                LocationDatabase.DATABASE_NAME
        ).build();
    }
    @Provides
    @Singleton
    Repository provideLocationRepository(LocationDatabase db, SharedPreferences sharedPref) {
        return new RepositoryImp(
                provideWeatherApi(provideHttpClient()),
                db.locationDao(),
                sharedPref
        );
    }
    @Provides
    @Singleton
    SharedPreferences privateSharedPreferences(Application app) {
        return app.getSharedPreferences(Utility.SHARED_PREF, MODE_PRIVATE);
    }
}
