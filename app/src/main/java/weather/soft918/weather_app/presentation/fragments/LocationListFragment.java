package weather.soft918.weather_app.presentation.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import weather.soft918.weather_app.R;
import weather.soft918.weather_app.databinding.DialogLayoutBinding;
import weather.soft918.weather_app.databinding.FragmentLocationListBinding;
import weather.soft918.weather_app.domin.adapters.LocationListAdapter;
import weather.soft918.weather_app.domin.model.LocationEntity;
import weather.soft918.weather_app.domin.util.MaterialBottomSheet;
import weather.soft918.weather_app.domin.util.TapTargetView;
import weather.soft918.weather_app.domin.util.Utility;
import weather.soft918.weather_app.presentation.activities.MainActivity;
import weather.soft918.weather_app.presentation.view_models.LocationListViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LocationListFragment extends Fragment {

    private FragmentLocationListBinding binding;
    private LocationListViewModel viewModel;
    @Inject
    SharedPreferences sharedPref;

    private static int lastLocation = -1;
    public static int locationSize = 0;
    static LocationListAdapter adapter;
    static List<LocationEntity> list = new ArrayList<>();

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
                showBottomSheet();
            }
        });
        observeLiveData();
    }
    private void showBottomSheet(){
        MaterialBottomSheet modalBottomSheet = new MaterialBottomSheet();
        modalBottomSheet.show(getActivity().getSupportFragmentManager(), MaterialBottomSheet.TAG);
    }
    public void locationFragmentTapTargetView(){
        if(locationSize == 0){
            List<View> view1 = new ArrayList<>();
            view1.add(binding.fabAddLocation);
            TapTargetView.locationFragmentTapTargetViewEmptyList(requireActivity(),view1);
        }else {
            List<View> view1 = new ArrayList<>();
            view1.add(adapter.btnSetAsCurrentLocation);
            view1.add(binding.fabAddLocation);
            view1.add(adapter.btnDeleteLocation);
            TapTargetView.locationFragmentTapTargetView(requireActivity(),view1);
        }

    }
    private void observeLiveData(){
        Log.d("location!!","!!!!!");
        viewModel.getAllData().observe(getViewLifecycleOwner(), new Observer<List<LocationEntity>>() {
            @Override
            public void onChanged(List<LocationEntity> locationEntities) {
                Log.d("location!!",locationEntities.toString());
                list = locationEntities;
                setupLocationListRv();
                locationSize = locationEntities.size();

            }
        });
        MainActivity.menuAction.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(Objects.equals(s, Utility.LOCATION_LIST_FRAGMENT)){
                    locationFragmentTapTargetView();
                    MainActivity.menuAction.postValue("");
                }
            }
        });
    }
    private void setupLocationListRv(){
        adapter = new LocationListAdapter(
                list,requireContext(),sharedPref);
        binding.rvLocationList.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false));
        binding.rvLocationList.setAdapter(adapter);
        adapter.onClickListenerDelete(
                new LocationListAdapter.OnClickListenerDelete() {
            @Override
            public void onClickDelete(LocationEntity location) {
                if (sharedPref.getInt(Utility.CURRENT_LOCATION,-1) == location.id){
                    sharedPref.edit().putInt(Utility.CURRENT_LOCATION,Utility.LOCALE_LOCATION_ID).apply();
                    showDialog(
                            getResources().getString(R.string.delete_dialog_title),
                            getResources().getString(R.string.delete_dialog_content),
                            location
                    );
                }else {
                    showDialog(
                            getResources().getString(R.string.delete_dialog_title),
                            getResources().getString(R.string.delete_dialog_content),
                            location
                    );
                }
            }
        });
        adapter.onClickListenerSetCurrentLocation(
                new LocationListAdapter.OnClickListenerSetCurrentLocation() {
            @Override
            public void onClickSetCurrentLocation(LocationEntity location, int position) {
                if(lastLocation == -1){
                    for(int i = 0; i <list.size(); i++){
                        if(sharedPref.getInt(Utility.CURRENT_LOCATION,-1) == list.get(i).id ){
                            lastLocation = i;
                            break;
                        }
                    }
                }
                viewModel.locationListEvent(
                        LocationListViewModel.LocationListEvent.SET_AS_CURRENT_LOCATION,location);
                adapter.notifyItemChanged(position);
                adapter.notifyItemChanged(lastLocation);
                lastLocation = position;

            }
        });
    }
    public static void popCurrentRecyclerViewItem(){
        if(list.size() != 0 ){
            adapter.notifyItemChanged(lastLocation);
        }

    }
    private void showDialog(String header,String content, LocationEntity location){
        Dialog dialog = new Dialog(requireContext(), R.style.DialogTheme);
        DialogLayoutBinding dialogBinding = DialogLayoutBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialogBinding.tvContent.setText(content);
        dialogBinding.tvHeader.setText(header);
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.locationListEvent(
                        LocationListViewModel.LocationListEvent.DELETE_LOCATION,location);
                dialog.dismiss();
            }
        });
        dialogBinding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}