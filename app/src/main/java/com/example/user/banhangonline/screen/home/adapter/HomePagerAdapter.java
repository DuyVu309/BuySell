package com.example.user.banhangonline.screen.home.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.user.banhangonline.screen.home.fragment.home.HomeFragment;
import com.example.user.banhangonline.screen.home.fragment.pay.PayFragment;

import com.example.user.banhangonline.model.Categories;

import java.util.List;

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private List<Categories> mlist;

    public HomePagerAdapter(Context context, FragmentManager fm, List<Categories> categories) {
        super(fm);
        this.context = context;
        this.mlist = categories;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else {
            return PayFragment.newInstance(mlist.get(position));
        }
    }

    @Override
    public int getCount() {
        return mlist != null ? mlist.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mlist.get(position).getTitle().toString().trim();
    }
}
