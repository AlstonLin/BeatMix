package io.alstonlin.beatmix;


import android.os.AsyncTask;

public class Playback extends AsyncTask<Void, Void, Void> {
    public static final int SLEEP_TIME = 50;
    private Song song;
    private Playable player;
    private boolean repeat;
    private double startTime;

    public Playback (Song song, Playable player, boolean repeat){
        this.song = song;
        this.player = player;
        this.repeat = repeat;
    }

    @Override
    protected Void doInBackground(Void... params) {
        do {
            song.reset();
            startTime = System.currentTimeMillis();
            while (!song.isFinished()) {
                if (isCancelled()) return null;
                if (System.currentTimeMillis() >= startTime + song.getNextNoteTime()) {
                    Song.Note note = song.popNextNode();
                    player.playbackChord(note.isMajor(), note.getVal());
                }
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (repeat);
        return null;
    }
}
