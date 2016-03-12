package io.alstonlin.beatmix;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Only job of this class is to be a container Fragment for JoinFragment and CreateFragment
 */
public class RootFragment extends Fragment {
    private MainActivity activity;
    private ChangeFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root, container, false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_frame, JoinFragment.newInstance(activity, listener));
        transaction.commit();
        return view;
    }

    public static RootFragment newInstance(MainActivity activity, ChangeFragmentListener listener){
        RootFragment fragment = new RootFragment();
        fragment.activity = activity;
        fragment.listener = listener;
        return fragment;
    }

}