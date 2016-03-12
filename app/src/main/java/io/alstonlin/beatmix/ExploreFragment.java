package io.alstonlin.beatmix;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ExploreFragment extends Fragment {

    private static final String ARG_ACTIVITY = "activity";
    private MainActivity activity;

    /**
     * Use this Factory method to create the Fragment instead of the constructor.
     * @param activity The Activity this Fragment will be attached to
     * @return The new Fragment instance
     */
    public static ExploreFragment newInstance(MainActivity activity) {
        final ExploreFragment fragment = new ExploreFragment();
        fragment.activity = activity;
        return fragment;
    }

    /**
     * Sets up the Fragment.
     * @param savedInstanceState The previous saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activity = (MainActivity) getArguments().getSerializable(ARG_ACTIVITY);
        }
    }

    /**
     * Once the View has been created, sets up the View.
     * @param inflater The Inflator of the Activity
     * @param container The container that the Fragement is in
     * @param savedInstanceState The previously saved instance of the Activity
     * @return The View once it is set up
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        return v;
    }

}