package weather.soft918.weather_app.data.data_source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import weather.soft918.weather_app.domin.model.LocationEntity;

@Database(
        entities = LocationEntity.class,
        version = 1,
        exportSchema = false
)
public abstract class LocationDatabase extends RoomDatabase {

    abstract public LocationDao locationDao();

    public final static String DATABASE_NAME = "location_db";

}
