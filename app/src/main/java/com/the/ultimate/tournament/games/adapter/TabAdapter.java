package com.the.ultimate.tournament.games.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

class TabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList();
    private final List<String> mFragmentTitleList = new ArrayList();

    TabAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment getItem(int i) {
        return (Fragment) mFragmentList.get(i);
    }

    public void addFragment(Fragment fragment, String str) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(str);
    }

    @Nullable
    public CharSequence getPageTitle(int i) {
        return (CharSequence) mFragmentTitleList.get(i);
    }

    public int getCount() {
        return mFragmentList.size();
    }
}
