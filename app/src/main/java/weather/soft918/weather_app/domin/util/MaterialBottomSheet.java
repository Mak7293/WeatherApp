package weather.soft918.weather_app.domin.util;

import android.app.Application;
import android.content.DialogInterface;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import weather.soft918.weather_app.R;
import weather.soft918.weather_app.databinding.BottomSheetBinding;
import weather.soft918.weather_app.domin.model.LocationEntity;
import weather.soft918.weather_app.presentation.fragments.LocationListFragment;
import weather.soft918.weather_app.presentation.view_models.LocationListViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MaterialBottomSheet extends BottomSheetDialogFragment {
    @Inject
    Application application;
    private List<Address> address = new ArrayList<>();
    private LocationListViewModel viewModel;
    public static final ScheduledExecutorService backgroundExecutor =
            Executors.newSingleThreadScheduledExecutor();
    @Inject
    public MaterialBottomSheet(){
    }
    private BottomSheetBinding binding;
    public static String TAG = "modalBottomSheet";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LocationListViewModel.class);
        binding.ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckInternetConnection.checkInternetConnection(application)){
                    Toast.makeText(application, "Please connect your device to internet.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!binding.etLocation.getText().toString().isEmpty()){
                    binding.llResult.setVisibility(View.GONE);
                    binding.progressBarLoading.setVisibility(View.VISIBLE);
                    searchLocation(binding.etLocation.getText().toString());
                }else {
                    Toast.makeText(application,"Please Enter location",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.btnSaveToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!address.isEmpty()){
                    if(LocationListFragment.locationSize<150) {
                        String locality = "";
                        if(address.get(0).getLocality() == null) {
                            locality = Objects.requireNonNull(binding.etLocation.getText()).toString();
                        }else {
                            locality = address.get(0).getLocality();
                        }
                        LocationEntity location = new LocationEntity(
                                0,
                                locality,
                                address.get(0).getAdminArea(),
                                address.get(0).getCountryName(),
                                address.get(0).getLatitude(),
                                address.get(0).getLongitude()
                        );
                        viewModel.locationListEvent(
                                LocationListViewModel.LocationListEvent.SAVE_LOCATION,null,location);
                        dismiss();
                    }else {
                        alertDialogForMoreThan150EntriesInDatabase();
                    }
                }else {
                    Toast.makeText(application,
                            "Please first search a location",Toast.LENGTH_SHORT).show();
                }

            }
        });
        getDialog().setCancelable(false);
    }
    private void searchLocation(String location){
        Log.d("address00",address.toString());
        Executor mainExecutor = ContextCompat.getMainExecutor(application);
        backgroundExecutor.execute(new Runnable(){
            @Override
            public void run() {
                try {
                    address = new GeocoderNominatim(application).getFromLocationName(
                            location,0);
                    Log.d("address",address.toString());
                    mainExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            if(address.size() != 0){
                                setupBottomSheetCard(address.get(0));
                            }else {
                                binding.progressBarLoading.setVisibility(View.GONE);
                                Toast.makeText(application, "Can not find address, Maybe you typed the address name wrong.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void setupBottomSheetCard(Address address){
        binding.progressBarLoading.setVisibility(View.GONE);
        binding.llResult.setVisibility(View.VISIBLE);
        String locality = "";
        if(address.getLocality() == null) {
            locality = Objects.requireNonNull(binding.etLocation.getText()).toString();
        }else {
            locality = address.getLocality();
        }
        binding.tvLocationName.setText(locality);
        binding.tvProvinceName.setText(address.getAdminArea());
        binding.tvCountryName.setText(address.getCountryName());
        binding.tvLatitude.setText(String.valueOf((double) address.getLatitude()));
        binding.tvLongitude.setText(String.valueOf((double) address.getLongitude()));
    }
    private void alertDialogForMoreThan150EntriesInDatabase(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("You have added 150 locations so far!!!");
        builder.setMessage(getResources().getString(R.string.alert_more_than_150_entries_in_database));
        builder.setIcon(R.drawable.ic_alert);
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

}
