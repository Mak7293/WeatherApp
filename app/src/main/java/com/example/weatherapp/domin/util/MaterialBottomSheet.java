package com.example.weatherapp.domin.util;

import android.app.Application;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.weatherapp.databinding.BottomSheetBinding;
import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.presentation.LocationListViewModel;
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
    Application context;
    private List<Address> address = new ArrayList<>();
    private LocationListViewModel viewModel;
    private final ScheduledExecutorService backgroundExecutor =
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
                if(!binding.etLocation.getText().toString().isEmpty()){
                    searchLocation(binding.etLocation.getText().toString());
                }else {
                    Toast.makeText(context,"Please Enter location",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                backgroundExecutor.shutdown();
            }
        });
        binding.btnSaveToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!address.isEmpty()){
                    LocationEntity location = new LocationEntity(
                            0,
                            address.get(0).getLocality(),
                            address.get(0).getAdminArea(),
                            address.get(0).getCountryName(),
                            address.get(0).getLatitude(),
                            address.get(0).getLongitude()
                    );
                    viewModel.locationListEvent(
                            LocationListViewModel.LocationListEvent.SAVE_LOCATION,null,location);
                    dismiss();
                }else {
                    Toast.makeText(context,
                            "Please first search a location",Toast.LENGTH_SHORT).show();
                }

            }
        });
        getDialog().setCancelable(false);
    }
    private void searchLocation(String location){
        Executor mainExecutor = ContextCompat.getMainExecutor(context);
        backgroundExecutor.execute(new Runnable(){
            @Override
            public void run() {

                try {
                    address = new GeocoderNominatim(context).getFromLocationName(
                            location,0);
                    Log.d("address",address.toString());
                    mainExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            setupBottomSheetCard(address.get(0));
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void setupBottomSheetCard(Address address){
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

}
