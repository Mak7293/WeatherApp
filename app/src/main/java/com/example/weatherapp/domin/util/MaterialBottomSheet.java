package com.example.weatherapp.domin.util;

import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.BottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.divider.MaterialDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MaterialBottomSheet extends BottomSheetDialogFragment {

    public MaterialBottomSheet(){

    }
    private BottomSheetBinding binding;
    private List<Address> address = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final ScheduledExecutorService backgroundExecutor =
            Executors.newSingleThreadScheduledExecutor();
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


        binding.ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.etLocation.getText().toString().isEmpty()){
                    backgroundExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                address = new GeocoderNominatim(requireContext()).getFromLocationName(
                                        binding.etLocation.getText().toString(),0);
                                Log.d("address", address.get(0).toString());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            backgroundExecutor.shutdown();
                        }
                    });
                }else {
                    Toast.makeText(requireContext(),"Please Enter location",Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getDialog().setCancelable(false);
    }

}
