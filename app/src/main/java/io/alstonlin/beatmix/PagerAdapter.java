package io.alstonlin.beatmix;

import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

/**
 * The Adapter for MainActivity to handle all the Fragments.
 */
public class PagerAdapter extends FragmentPagerAdapter implements ChangeFragmentListener {

    private static final int NUM_TABS = 2;
    private MainActivity activity;

    private FragmentManager fm;
    private Fragment exploreFragment;

    public PagerAdapter(FragmentManager fm, MainActivity activity) {
        super(fm);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ExploreFragment.newInstance(activity);
            case 1:
                return RootFragment.newInstance(activity, this);
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Explore";
            case 1:
                return "Create";
        }
        return null;
    }

    @Override
    public void onSwitchToNextFragment() {
        exploreFragment = CreateFragment.newInstance(activity);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.root_frame, exploreFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}