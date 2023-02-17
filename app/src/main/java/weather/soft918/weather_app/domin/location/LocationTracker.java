package weather.soft918.weather_app.domin.location;

import androidx.annotation.Nullable;
import java.util.HashMap;

public interface LocationTracker {

    abstract @Nullable HashMap<String,Double> getCurrentLocation() throws InterruptedException;
}
