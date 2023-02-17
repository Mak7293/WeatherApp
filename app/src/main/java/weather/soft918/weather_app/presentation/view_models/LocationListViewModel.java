package weather.soft918.weather_app.presentation.view_models;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import weather.soft918.weather_app.domin.model.LocationEntity;
import weather.soft918.weather_app.domin.repository.Repository;
import weather.soft918.weather_app.domin.util.MaterialBottomSheet;
import weather.soft918.weather_app.domin.util.Utility;
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

    SharedPreferences sharedPref;
    private final ScheduledExecutorService backgroundExecutor =
            Executors.newSingleThreadScheduledExecutor();
    @Inject
    public LocationListViewModel(
            Application applicationContext,
            Repository repository,
            SharedPreferences sharedPref
    ){
        this.applicationContext = applicationContext;
        this.repository = repository;
        this.sharedPref = sharedPref;
    }
    public enum LocationListEvent{
        SHOW_BOTTOM_SHEET,
        SAVE_LOCATION,
        SET_AS_CURRENT_LOCATION,
        DELETE_LOCATION
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
            case SET_AS_CURRENT_LOCATION: {
                saveLocationToDataStore(location.id);
                break;
            }
            case DELETE_LOCATION: {
                deleteLocationInDatabase(location);
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
            }
        });
    }
    private void saveLocationToDataStore(int id){
        sharedPref.edit().putInt(Utility.CURRENT_LOCATION,id).apply();

    }
    private void deleteLocationInDatabase(LocationEntity location){
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                repository.deleteLocation(location);
            }

        });

    }
    public LiveData<List<LocationEntity>> getAllData(){
        return repository.getAllLocation();
    };

    @Override
    protected void onCleared() {
        super.onCleared();
        backgroundExecutor.shutdown();
        if (!MaterialBottomSheet.backgroundExecutor.isShutdown()){
            MaterialBottomSheet.backgroundExecutor.shutdown();
        }
    }
}
