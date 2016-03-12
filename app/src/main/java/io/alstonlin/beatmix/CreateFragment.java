package io.alstonlin.beatmix;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;


public class CreateFragment extends Fragment implements Playable {

    private MainActivity activity;
    private static final int MIN_SAMPLE_RATE = 44100;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (v.getId()) {
                        case R.id.guitar0:
                            DAO.getInstance().sendPlayChord(true, 0);
                            break;
                        case R.id.guitar1:
                            DAO.getInstance().sendPlayChord(true, 1);
                            break;
                        case R.id.guitar2:
                            DAO.getInstance().sendPlayChord(true, 2);
                            break;
                        case R.id.guitar3:
                            DAO.getInstance().sendPlayChord(true, 3);
                            break;
                        case R.id.guitar4:
                            DAO.getInstance().sendPlayChord(true, 4);
                            break;
                        case R.id.guitar5:
                            DAO.getInstance().sendPlayChord(true, 5);
                            break;
                        case R.id.guitar6:
                            DAO.getInstance().sendPlayChord(true, 6);
                            break;
                        case R.id.guitar7:
                            DAO.getInstance().sendPlayChord(true, 7);
                            break;
                        case R.id.guitar8:
                            DAO.getInstance().sendPlayChord(true, 8);
                            break;
                        case R.id.guitar9:
                            DAO.getInstance().sendPlayChord(true, 9);
                            break;
                        case R.id.guitar10:
                            DAO.getInstance().sendPlayChord(true, 10);
                            break;
                        case R.id.guitar11:
                            DAO.getInstance().sendPlayChord(true, 11);
                            break;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP){
                    DAO.getInstance().sendEndChord();
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return false;
        }
    };

    public static CreateFragment newInstance(MainActivity activity) {
        CreateFragment fragment = new CreateFragment();
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_create, container, false);
        try {
            setupPd();
            DAO.getInstance().setPlayable(this);
            setupButtons(view);
        } catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        PdAudio.startAudio(activity);
    }

    @Override
    public void onStop() {
        PdAudio.stopAudio();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        cleanup();
        super.onDestroy();
    }

    private void setupPd() throws IOException {
        AudioParameters.init(activity);
        int srate = Math.max(MIN_SAMPLE_RATE, AudioParameters.suggestSampleRate());
        PdAudio.initAudio(srate, 0, 2, 1, true);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BeatMix");
        if (!dir.exists()) dir.mkdirs();
        File patchFile = new File(dir, "chords.pd");
        IoUtils.extractZipResource(getResources().openRawResource(R.raw.patch), dir, true);
        PdBase.openPatch(patchFile.getAbsolutePath());
    }

    private void setupButtons(View view){
        view.findViewById(R.id.guitar0).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar1).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar2).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar3).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar4).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar5).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar6).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar7).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar8).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar9).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar10).setOnTouchListener(touchListener);
        view.findViewById(R.id.guitar11).setOnTouchListener(touchListener);
    }

    private void cleanup() {
        PdAudio.release();
        PdBase.release();
    }

    /**
     * Called by the KeyboardView when a key has been pressed.
     * @param major If the key is a major or not
     * @param n The note of the key represented as an integer
     */
    public void playChord(boolean major, int n) {
        PdBase.sendList("playchord", major ? 1 : 0, n);
    }

    /**
     * When the pressed key has been released.
     */
    public void endChord() {
        PdBase.sendBang("endchord");
    }
}
