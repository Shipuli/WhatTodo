package com.shipuli.whattodo.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shipuli.whattodo.R;
import com.shipuli.whattodo.fragments.CompletedFragment;
import com.shipuli.whattodo.fragments.TodoFragment;

import java.util.ArrayList;

/**
 * Adapter which connects Fragments to tabs.
 */
public class TabPagerAdaptor extends FragmentPagerAdapter {

    private final Context context;
    private static ArrayList<Fragment> fragments = new ArrayList<>(2);

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
                try {
                    return fragments.get(0);
                }catch (IndexOutOfBoundsException e) {
                    Fragment nF = new TodoFragment();
                    fragments.add(0, nF);
                    return nF;
                }
            case 1:
                try {
                    return fragments.get(1);
                }catch (IndexOutOfBoundsException e) {
                    Fragment nF = new CompletedFragment();
                    fragments.add(1, nF);
                    return nF;
                }
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
