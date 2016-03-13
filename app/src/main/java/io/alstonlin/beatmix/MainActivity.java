package io.alstonlin.beatmix;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;

/**
 * Main Activity (and only); Handles all events and the lifecycle of the app.
 */
public class MainActivity extends AppCompatActivity implements Playable {
    private ViewPager pager;
    private static final int MIN_SAMPLE_RATE = 44100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewPager();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Explore"));
        tabLayout.addTab(tabLayout.newTab().setText("Create"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        DAO.getInstance().setActivity(this);
        try {
            setupPd();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupPd() throws IOException {
        AudioParameters.init(this);
        int srate = Math.max(MIN_SAMPLE_RATE, AudioParameters.suggestSampleRate());
        PdAudio.initAudio(srate, 0, 2, 1, true);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BeatMix");
        if (!dir.exists()) dir.mkdirs();
        File patchFile = new File(dir, "chords.pd");
        IoUtils.extractZipResource(getResources().openRawResource(R.raw.patch), dir, true);
        PdBase.openPatch(patchFile.getAbsolutePath());
    }

    /**
     * Sets up the ViewPager, the PagerAdapter and the Listeners.
     */
    private void setupViewPager(){
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
    }

    public synchronized void playbackChord(boolean major, int n){
        PdBase.sendList("playchord", major ? 1 : 0, n);
    }

    /**
     * Called by the KeyboardView when a key has been pressed.
     * @param major If the key is a major or not
     * @param n The note of the key represented as an integer
     */
    public void playChord(boolean major, int n) {
        playbackChord(major, n);
    }

    /**
     * When the pressed key has been released.
     */
    public void endChord() {
        PdBase.sendBang("endchord");
    }

    @Override
    public void onStart() {
        super.onStart();
        PdAudio.startAudio(this);
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

    private void cleanup() {
        PdAudio.release();
        PdBase.release();
    }
}