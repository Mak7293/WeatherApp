package com.example.weatherapp.domin.util;

import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.weatherapp.R;
import com.example.weatherapp.presentation.activities.MainActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.List;

public class TapTargetView {
    public static void weatherFragmentTapTargetView(FragmentActivity activity, List<View> view){
            new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(view.get(0), "Latest Weather Forecast",
                                "You can get latest weather forecast.")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_0),
                        TapTarget.forView(view.get(1), "Weekly weather forecast",
                                "Weather forecast for the next 7 days.")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_0),
                        TapTarget.forView(view.get(2), "Locale location", "Set your" +
                                        " location wherever you are and Get the right weather forecast.")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_0),
                        TapTarget.forView(MainActivity.view, "Change theme",
                                        "You can change application theme.")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_1))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        Toast.makeText(activity, "The introduction of Weather tab is over.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        // Perform action for the current target
                    }
                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();
    }
    public static void locationFragmentTapTargetViewEmptyList(FragmentActivity activity, List<View> view){
        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(view.get(0), "Add location",
                                        "You can add specific location and get its weather forecast.")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_0),
                        TapTarget.forView(MainActivity.view, "Change theme",
                                        "You can change application theme.")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_1))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        Toast.makeText(activity, "The introduction of Location List tab is over.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        // Perform action for the current target
                    }
                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();
    }
    public static void locationFragmentTapTargetView(FragmentActivity activity, List<View> view){
        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(view.get(0), "Set as current location",
                                        "You can add this specific location as your current" +
                                                " location and get its weather forecast.")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_0),
                        TapTarget.forView(view.get(1), "Add location",
                                        "You can add specific location and get its weather forecast.")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_0),
                        TapTarget.forView(view.get(2), "Delete Location",
                                        "You can delete this location if you don't want get" +
                                                " weather forecast for this specific location")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_0),
                        TapTarget.forView(MainActivity.view, "Change theme",
                                        "You can change application theme.")
                                .dimColor(android.R.color.white)
                                .outerCircleColor(R.color.dark_green_4)
                                .targetCircleColor(R.color.custom_yellow)
                                .transparentTarget(true)
                                .targetRadius(45)
                                .titleTextColor(R.color.dark_green_0)
                                .descriptionTextColor(R.color.dark_green_1))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        Toast.makeText(activity, "The introduction of Location List tab is over.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        // Perform action for the current target
                    }
                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                }).start();
    }
}
