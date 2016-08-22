package com.shipuli.whattodo.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shipuli.whattodo.R;
import com.shipuli.whattodo.fragments.CompletedFragment;
import com.shipuli.whattodo.fragments.TodoFragment;

/**
 * Adapter which connects Fragments to tabs.
 */
public class TabPagerAdaptor extends FragmentPagerAdapter {

    Context context;

    public TabPagerAdaptor(FragmentManager fm, Context nContext) {
        super(fm);
        context = nContext;
    }

    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position){
        switch (position) {
            case 0:
                return new TodoFragment();
            case 1:
                return new CompletedFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.todo_page_title);
            case 1:
                return context.getString(R.string.completed_page_title);
            default:
                return null;
        }
    }
}
