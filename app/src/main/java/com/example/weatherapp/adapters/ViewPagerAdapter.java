package com.example.weatherapp.adapters;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;
    FragmentManager supportFragmentManager;

    public ViewPagerAdapter(List<Fragment> fragmentList, FragmentManager supportFragmentManager){
        super(supportFragmentManager);
        this.fragmentList = fragmentList;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
         Fragment fragment;
         switch(position){
            case 0 :
                fragment = fragmentList.get(0);
                break;
            default:
                fragment = fragmentList.get(1);
                break;
         };
         return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if(position == 0) {
            title = "Weather Forecast";
        } else{
            title = "Location List";
        }
        return title;
    }

}
