package io.alstonlin.beatmix;

/**
 * Interacts between DAO and Activity
 */
public interface Playable {
    void playChord(boolean major, int n);
    void playbackChord(boolean major, int n);
}
