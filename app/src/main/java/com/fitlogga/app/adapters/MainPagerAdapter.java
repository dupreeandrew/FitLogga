package com.fitlogga.app.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fitlogga.app.fragments.PlansFragment;
import com.fitlogga.app.fragments.PowerupFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PowerupFragment();
            case 1:
                return new PlansFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


}
