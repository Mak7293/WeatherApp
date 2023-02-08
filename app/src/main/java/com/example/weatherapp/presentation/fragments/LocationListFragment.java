package com.example.weatherapp.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weatherapp.databinding.FragmentLocationListBinding;
import com.example.weatherapp.domin.adapters.LocationListAdapter;
import com.example.weatherapp.domin.adapters.WeatherAdapter;
import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.presentation.view_models.LocationListViewModel;

import java.util.List;


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
                        LocationListViewModel.LocationListEvent.SHOW_BOTTOM_SHEET,requireActivity(),null);
            }
        });
        observeViewModelLiveData();
    }
    private void observeViewModelLiveData(){
        Log.d("location!!","!!!!!");
        viewModel.getAllData().observe(getViewLifecycleOwner(), new Observer<List<LocationEntity>>() {
            @Override
            public void onChanged(List<LocationEntity> locationEntities) {
                Log.d("location!!",locationEntities.toString());
                setupLocationListRv(locationEntities);
            }
        });
    }
    private void setupLocationListRv(List<LocationEntity> list){
        LocationListAdapter adapter = new LocationListAdapter(
                list,requireContext());
        binding.rvLocationList.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false));
        binding.rvLocationList.setAdapter(adapter);
        adapter.onClickListenerDelete(
                new LocationListAdapter.OnClickListenerDelete() {

            @Override
            public void onClickDelete(int position) {
                Toast.makeText(requireContext(), "Delete", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.onClickListenerSetCurrentLocation(
                new LocationListAdapter.OnClickListenerSetCurrentLocation() {
            @Override
            public void onClickSetCurrentLocation(int position) {
                Toast.makeText(requireContext(), "Location set as current location", Toast.LENGTH_SHORT).show();
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