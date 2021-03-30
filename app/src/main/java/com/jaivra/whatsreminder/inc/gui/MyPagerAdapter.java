package com.jaivra.whatsreminder.inc.gui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private List<FragmentTab> fragments;

    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        FragmentTab fragmentTab = fragments.get(position);
        return fragmentTab;
    }

    public void addFragment(FragmentTab fragment) {
        fragments.add(fragment);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        FragmentTab fragmentTab = fragments.get(position);
        return fragmentTab.getTabTitle();
    }

    public void refreshAll(){
        for (FragmentTab fragmentTab : fragments)
            fragmentTab.refresh();
    }
}
