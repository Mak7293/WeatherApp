package com.example.weatherapp.domin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.databinding.WeatherRvItemBinding;
import com.example.weatherapp.domin.weather.WeatherData;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{

    private List<WeatherData> list;
    private Context context;

    public WeatherAdapter(List<WeatherData> list, Context context){
        this.list = list;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        WeatherRvItemBinding binding;
        public ViewHolder(@NonNull WeatherRvItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(WeatherRvItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherData item = list.get(position);
        holder.binding.tvTime.setText(item.time.format( DateTimeFormatter.ofPattern("HH:mm")));
        holder.binding.ivWeatherState.setImageDrawable(
                ContextCompat.getDrawable(context,item.weatherType.iconRes));
        holder.binding.tvDegree.setText(item.temperatureCelsius + "°C");

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}
