package com.example.weatherapp.domin.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.LocationRvItemBinding;
import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.domin.util.Theme;
import com.example.weatherapp.domin.util.Utility;

import java.util.List;


public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private List<LocationEntity> list;
    private Context context;

    private SharedPreferences sharedPref;

    public LocationListAdapter(List<LocationEntity> list, Context context,SharedPreferences sharedPref){
        this.list = list;
        this.context = context;
        this.sharedPref =sharedPref;
    }
    public interface OnClickListenerDelete{
        void onClickDelete(LocationEntity location);
    }
    public interface OnClickListenerSetCurrentLocation{
        void onClickSetCurrentLocation(LocationEntity location,int position);
    }
    OnClickListenerDelete onClickListenerDelete;
    OnClickListenerSetCurrentLocation onClickListenerSetCurrentLocation;
    public void onClickListenerDelete(OnClickListenerDelete onClickListener){
        this.onClickListenerDelete = onClickListener;
    }
    public void onClickListenerSetCurrentLocation(OnClickListenerSetCurrentLocation onClickListener){
        this.onClickListenerSetCurrentLocation = onClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        LocationRvItemBinding binding;
        public ViewHolder(@NonNull LocationRvItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LocationRvItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationEntity location = list.get(position);
        holder.binding.tvProvinceName.setText("Province Name:" + location.province);
        holder.binding.tvCountryName.setText("Country Name:" + location.country);
        holder.binding.tvLocality.setText("Locality Name:" + location.locality);
        Theme.setLocationListRecyclerViewTheme(context,sharedPref,location,holder.binding);
        holder.binding.btnDeleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerDelete.onClickDelete(location);
            }
        });
        holder.binding.btnSetAsCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerSetCurrentLocation
                        .onClickSetCurrentLocation(location, holder.getAdapterPosition());
            };
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}
