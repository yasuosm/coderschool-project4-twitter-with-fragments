package xyz.annt.twitterclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import xyz.annt.twitterclient.fragments.HomeTimelineFragment;
import xyz.annt.twitterclient.fragments.MentionsTimelineFragment;

/**
 * Created by annt on 4/2/16.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles = { "Home", "Mentions" };
    private ArrayList<Fragment> fragments;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);

        fragments = new ArrayList<>();
        fragments.add(new HomeTimelineFragment());
        fragments.add(new MentionsTimelineFragment());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
