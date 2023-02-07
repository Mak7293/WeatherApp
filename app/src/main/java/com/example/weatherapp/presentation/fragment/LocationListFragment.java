package com.example.weatherapp.presentation.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.FragmentLocationListBinding;
import com.example.weatherapp.domin.util.MaterialBottomSheet;
import com.example.weatherapp.presentation.LocationListViewModel;
import com.example.weatherapp.presentation.WeatherViewModel;


public class LocationListFragment extends Fragment {

    private FragmentLocationListBinding binding;
    private LocationListViewModel viewModel;

    public LocationListFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(LocationListViewModel.class);
        binding.fabAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.locationListEvent(
                        LocationListViewModel.LocationListEvent.SHOW_BOTTOM_SHEET,requireActivity());
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        Log.d("destroy","location");
    }
}