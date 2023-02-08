package com.example.weatherapp.domin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.FragmentLocationListBinding;
import com.example.weatherapp.databinding.LocationRvItemBinding;
import com.example.weatherapp.databinding.WeatherRvItemBinding;
import com.example.weatherapp.domin.model.LocationEntity;
import com.example.weatherapp.domin.weather.WeatherData;

import java.util.List;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private List<LocationEntity> list;
    private Context context;


    public LocationListAdapter(List<LocationEntity> list, Context context){
        this.list = list;
        this.context = context;
    }
    public interface OnClickListenerDelete{
        void onClickDelete(int position);
    }
    public interface OnClickListenerSetCurrentLocation{
        void onClickSetCurrentLocation(int position);
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

        holder.binding.btnDeleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerDelete.onClickDelete(holder.getAdapterPosition());
            }
        });
        holder.binding.btnSetAsCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerSetCurrentLocation
                        .onClickSetCurrentLocation(getItemViewType(holder.getAdapterPosition()));
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}
