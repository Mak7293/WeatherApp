package com.example.weatherapp.presentation.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivityStatisticsBinding;
import com.example.weatherapp.domin.util.Utility;
import com.example.weatherapp.domin.weather.WeatherData;
import com.example.weatherapp.presentation.fragments.WeatherFragment;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import com.github.mikephil.charting.charts.BarChart;

public class StatisticsActivity extends AppCompatActivity {

    private ActivityStatisticsBinding binding;
    private HashMap<Integer, List<WeatherData>> weatherDataPerDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.dark_green_6));
        setUpToolbar();
        weatherDataPerDay = Objects.requireNonNull(WeatherFragment
                ._weatherState.weatherInfo).weatherDataPerDay;
        setupLastDayChart(binding.chartTemperature, "Temperature°C",
                lastDayChartData().get(Utility.TEMPERATURE));
        setupLastDayChart(binding.chartPressure,"Pressure hPa",
                lastDayChartData().get(Utility.PRESSURE));
        setupLastDayChart(binding.chartWindSpeed,"Wind Speed km/hr",
                lastDayChartData().get(Utility.WIND_SPEED));
        setupLastDayChart(binding.chartHumidity,"Humidity %",
                lastDayChartData().get(Utility.HUMIDITY));

        setup7daysChart(binding.chartTemperature7days, "Average Temperature°C",
                weeklyChartData().get(Utility.TEMPERATURE));
        setup7daysChart(binding.chartPressure7days,"Average Pressure hPa",
                weeklyChartData().get(Utility.PRESSURE));
        setup7daysChart(binding.chartWindSpeed7days,"Average Wind Speed km/hr",
                weeklyChartData().get(Utility.WIND_SPEED));
        setup7daysChart(binding.chartHumidity7days,"Average Humidity %",
                weeklyChartData().get(Utility.HUMIDITY));
    }
    private void setUpToolbar(){
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    finish();
                }
            });
        }
    }
    private HashMap<String,List<Pair<LocalDateTime,Float>>> lastDayChartData(){
        HashMap<String,List<Pair<LocalDateTime,Float>>> map = new HashMap<>();
        List<Pair<LocalDateTime,Float>> temperature = new ArrayList<>();
        List<Pair<LocalDateTime,Float>> pressure = new ArrayList<>();
        List<Pair<LocalDateTime,Float>> windSpeed = new ArrayList<>();
        List<Pair<LocalDateTime,Float>> humidity = new ArrayList<>();

        weatherDataPerDay.get(0).forEach((n) -> {
            temperature.add(new Pair<>(n.time,(float)n.temperatureCelsius));
            pressure.add(new Pair<>(n.time,(float)n.pressure));
            windSpeed.add(new Pair<>(n.time,(float)n.windSpeed));
            humidity.add(new Pair<>(n.time,(float)n.humidity));

        });
        map.put(Utility.TEMPERATURE,temperature);
        map.put(Utility.PRESSURE,pressure);
        map.put(Utility.HUMIDITY,humidity);
        map.put(Utility.WIND_SPEED,windSpeed);
        return map;
    }
    private HashMap<String,List<Pair<Integer,Float>>> weeklyChartData(){
        HashMap<String,List<Pair<Integer,Float>>> map = new HashMap<>();
        List<Pair<Integer,Float>> humidityList = new ArrayList<>();
        List<Pair<Integer,Float>> temperatureList = new ArrayList<>();
        List<Pair<Integer,Float>> pressureList = new ArrayList<>();
        List<Pair<Integer,Float>> windSpeedList = new ArrayList<>();

        for(int i = 0; i < weatherDataPerDay.size(); i++){
            final Float[] dailyAvgTemperature = {0.0f};
            final Float[] dailyAvgPressure = {0.0f};
            final Float[] dailyAvgWindSpeed = {0.0f};
            final Float[] dailyAvgHumidity = {0.0f};
            weatherDataPerDay.get(i).forEach((n) -> {
                dailyAvgHumidity[0] += (float) n.humidity;
                dailyAvgPressure[0] += (float) n.pressure;
                dailyAvgWindSpeed[0] += (float) n.windSpeed;
                dailyAvgTemperature[0] += (float) n.temperatureCelsius;
            });
            Log.d("humidity", String.valueOf(dailyAvgHumidity[0]));
            dailyAvgHumidity[0] /= 24;
            Log.d("humidity1", String.valueOf(dailyAvgHumidity[0]));
            dailyAvgPressure[0] /= 24;
            dailyAvgWindSpeed[0] /= 24;
            dailyAvgTemperature[0] /= 24;

            temperatureList.add(new Pair<>(i,dailyAvgTemperature[0]));
            humidityList.add(new Pair<>(i,dailyAvgHumidity[0]));
            pressureList.add(new Pair<>(i,dailyAvgPressure[0]));
            windSpeedList.add(new Pair<>(i,dailyAvgWindSpeed[0]));
        }
        map.put(Utility.HUMIDITY,humidityList);
        map.put(Utility.TEMPERATURE,temperatureList);
        map.put(Utility.WIND_SPEED,windSpeedList);
        map.put(Utility.PRESSURE,pressureList);
        return map;
    }
    private void setupLastDayChart(BarChart chart, String title,List<Pair<LocalDateTime,Float>> list){
        List<BarEntry> barEntryList = new ArrayList<>();
        list.forEach((n) -> {
            barEntryList.add(new BarEntry(n.first.getHour(), n.second.floatValue()));
        });
        StatisticsActivity.DayAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(list);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1.0f);
        xAxis.setLabelRotationAngle(30.0f);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextColor(ContextCompat.getColor(this, R.color.dark_custom_0));
        xAxis.setAxisLineColor(ContextCompat.getColor(this,R.color.dark_custom_1));
        /*if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            this?.textColor = ContextCompat.getColor(requireContext(),R.color.dark_0)
            this?.axisLineColor = ContextCompat.getColor(requireContext(),R.color.dark_1)
        }else{
            this?.textColor = ContextCompat.getColor(requireContext(),R.color.dark_7)
            this?.axisLineColor = ContextCompat.getColor(requireContext(),R.color.dark_5)
        }*/

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setTextColor(ContextCompat.getColor(this,R.color.dark_custom_0));
        yAxisLeft.setAxisLineColor(ContextCompat.getColor(this,R.color.dark_custom_1));
        /*if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            this?.textColor = ContextCompat.getColor(requireContext(),R.color.dark_0)
            this?.axisLineColor = ContextCompat.getColor(requireContext(),R.color.dark_1)
        }else{
            this?.textColor = ContextCompat.getColor(requireContext(),R.color.dark_7)
            this?.axisLineColor = ContextCompat.getColor(requireContext(),R.color.dark_5)
        }*/

        YAxis axisRight = chart.getAxisRight();
        axisRight.setAxisLineColor(Color.TRANSPARENT);
        axisRight.setTextColor(Color.TRANSPARENT);
        axisRight.setDrawGridLines(false);

        BarDataSet barDataSet = new BarDataSet(barEntryList,title);
        barDataSet.setValueTextColor(Color.TRANSPARENT);
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setColor(ContextCompat.getColor(this,R.color.dark_custom_1));

        /*val barDataSet: BarDataSet = if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            BarDataSet(barEntryList, "Calories Burned").apply {

                valueTextColor = ContextCompat.getColor(requireContext(),R.color.dark_0)
                valueTextSize = 10.0f
                color = ContextCompat.getColor(requireContext(),R.color.green_primary_color)
            }
        }else{
            BarDataSet(barEntryList, "Calories Burned").apply {
                valueTextColor = ContextCompat.getColor(requireContext(),R.color.dark_7)
                valueTextSize = 10.0f
                color = ContextCompat.getColor(requireContext(),R.color.yellow_primary_color)
            }
        }*/
        chart.setData(new BarData(barDataSet));
    }
    private void setup7daysChart(BarChart chart, String title,List<Pair<Integer,Float>> list){
        List<BarEntry> barEntryList = new ArrayList<>();
        list.forEach((n) -> {
            barEntryList.add(new BarEntry(n.first, n.second.floatValue()));
        });
        StatisticsActivity.WeekAxisValueFormatter xAxisFormatter = new WeekAxisValueFormatter(list);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1.0f);
        xAxis.setLabelRotationAngle(30.0f);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextColor(ContextCompat.getColor(this, R.color.dark_custom_0));
        xAxis.setAxisLineColor(ContextCompat.getColor(this,R.color.dark_custom_1));
        /*if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            this?.textColor = ContextCompat.getColor(requireContext(),R.color.dark_0)
            this?.axisLineColor = ContextCompat.getColor(requireContext(),R.color.dark_1)
        }else{
            this?.textColor = ContextCompat.getColor(requireContext(),R.color.dark_7)
            this?.axisLineColor = ContextCompat.getColor(requireContext(),R.color.dark_5)
        }*/

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setTextColor(ContextCompat.getColor(this,R.color.dark_custom_0));
        yAxisLeft.setAxisLineColor(ContextCompat.getColor(this,R.color.dark_custom_1));
        /*if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            this?.textColor = ContextCompat.getColor(requireContext(),R.color.dark_0)
            this?.axisLineColor = ContextCompat.getColor(requireContext(),R.color.dark_1)
        }else{
            this?.textColor = ContextCompat.getColor(requireContext(),R.color.dark_7)
            this?.axisLineColor = ContextCompat.getColor(requireContext(),R.color.dark_5)
        }*/

        YAxis axisRight = chart.getAxisRight();
        axisRight.setAxisLineColor(Color.TRANSPARENT);
        axisRight.setTextColor(Color.TRANSPARENT);
        axisRight.setDrawGridLines(false);

        BarDataSet barDataSet = new BarDataSet(barEntryList,title);
        barDataSet.setValueTextColor(Color.TRANSPARENT);
        barDataSet.setValueTextSize(10.0f);
        barDataSet.setColor(ContextCompat.getColor(this,R.color.dark_custom_1));

        /*val barDataSet: BarDataSet = if(currentNightMode == Configuration.UI_MODE_NIGHT_NO){
            BarDataSet(barEntryList, "Calories Burned").apply {

                valueTextColor = ContextCompat.getColor(requireContext(),R.color.dark_0)
                valueTextSize = 10.0f
                color = ContextCompat.getColor(requireContext(),R.color.green_primary_color)
            }
        }else{
            BarDataSet(barEntryList, "Calories Burned").apply {
                valueTextColor = ContextCompat.getColor(requireContext(),R.color.dark_7)
                valueTextSize = 10.0f
                color = ContextCompat.getColor(requireContext(),R.color.yellow_primary_color)
            }
        }*/
        chart.setData(new BarData(barDataSet));
    }
    class DayAxisValueFormatter extends ValueFormatter {
        List<Pair<LocalDateTime,Float>> list = new ArrayList<>();
        public DayAxisValueFormatter(List<Pair<LocalDateTime,Float>> list){
            this.list = list;
        }

        @Override
        public String getFormattedValue(float value) {
            String result = String.valueOf(((int) value)) + ":" + "00";
            return result ;
        }
    }
    class WeekAxisValueFormatter extends ValueFormatter {
        List<Pair<Integer,Float>> list = new ArrayList<>();
        public WeekAxisValueFormatter(List<Pair<Integer,Float>> list){
            this.list = list;
        }
        @Override
        public String getFormattedValue(float value) {
            if(value == 0){
                return "Today";
            } else if (value == 1) {
                return "Tomorrow";
            } else {
                return String.valueOf(((int) value));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

