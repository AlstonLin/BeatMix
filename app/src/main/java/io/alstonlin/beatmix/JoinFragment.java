package io.alstonlin.beatmix;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

public class JoinFragment extends Fragment {

    private MainActivity activity;
    private ChangeFragmentListener listener;

    public static JoinFragment newInstance(MainActivity activity, ChangeFragmentListener listener) {
        JoinFragment fragment = new JoinFragment();
        fragment.activity = activity;
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join, container, false);
        Button b = (Button) view.findViewById(R.id.join_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickJoin();
            }
        });
        return view;
    }

    public void clickJoin(){
        EditText text = (EditText) activity.findViewById(R.id.code);
        String s = String.valueOf(text.getText());
        joinRoom(s);
    }

    private void joinRoom(String code){
        try {
            DAO.getInstance().joinRoom(code);
            // Replace fragment
            listener.onSwitchToNextFragment();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
