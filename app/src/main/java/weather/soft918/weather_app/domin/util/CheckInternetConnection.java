package weather.soft918.weather_app.domin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import javax.annotation.Nullable;


public class CheckInternetConnection {
    public static boolean checkInternetConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            @Nullable
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null){
                return false;
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (capabilities == null){
                return false;
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                return true;
            }else if ( capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                return true;
            }else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                return true;
            }else {
                return false;
            }
        }else{
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            switch (activeNetwork.getType()){
                case ConnectivityManager.TYPE_WIFI    :
                    return true;
                case ConnectivityManager.TYPE_MOBILE  :
                    return true;
                case ConnectivityManager.TYPE_ETHERNET:
                    return true;
                default:
                    return false;
            }
        }
    }
}
