package io.alstonlin.beatmix;

import android.app.Activity;
import android.os.Bundle;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;

/**
 * Main Activity (and only); Handles all events and the lifecycle of the app.
 */
public class MainActivity extends Activity {

    private static final int MIN_SAMPLE_RATE = 44100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        try {
            setupPd();
        } catch (IOException e) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PdAudio.startAudio(this);
    }

    @Override
    protected void onStop() {
        PdAudio.stopAudio();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        cleanup();
        super.onDestroy();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        KeyboardView keyboard = (KeyboardView) findViewById(R.id.circleview);
        keyboard .setActivity(this);
    }

    private void setupPd() throws IOException {
        AudioParameters.init(this);
        int srate = Math.max(MIN_SAMPLE_RATE, AudioParameters.suggestSampleRate());
        PdAudio.initAudio(srate, 0, 2, 1, true);

        File dir = getFilesDir();
        File patchFile = new File(dir, "chords.pd");
        IoUtils.extractZipResource(getResources().openRawResource(R.raw.patch), dir, true);
        PdBase.openPatch(patchFile.getAbsolutePath());
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
