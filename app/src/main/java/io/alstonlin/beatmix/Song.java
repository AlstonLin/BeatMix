package io.alstonlin.beatmix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Song {
    private ArrayList<Note> notes = new ArrayList<>();
    private int nextNote = 0;
    private OnSongReadyListener listener;
    private String title;
    private String id;
    private String author;

    public void addNote(double time, String command, boolean major, int val){
        notes.add(new Note(time, command, major, val));
    }

    public void reset(){
        nextNote = 0;
    }

    public boolean isFinished(){
        return nextNote >= notes.size();
    }

    public double getNextNoteTime(){
        return notes.get(nextNote).time;
    }

    public Note popNextNode(){
        Note n = notes.get(nextNote);
        nextNote++;
        return n;
    }

    public void loadContentFromJson(JSONObject obj) throws JSONException {
        JSONArray json = obj.getJSONArray("data");
        for (int i = 0; i < json.length(); i++){
            JSONObject jsonNote = json.getJSONObject(i);
            addNote(jsonNote.getDouble("time"), jsonNote.getString("command"), jsonNote.getBoolean("major"),
                    jsonNote.getInt("val"));
        }
        if (listener != null) listener.onSongReady(this);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        JSONArray json = new JSONArray();
        for (Note note : notes){
            JSONObject jsonNote = new JSONObject();
            jsonNote.put("time", note.time);
            jsonNote.put("command", note.command);
            jsonNote.put("major", note.major);
            jsonNote.put("val", note.val);
            json.put(jsonNote);
        }
        obj.put("data", json);
        return obj;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setOnSongReadyListener(OnSongReadyListener listener){
        this.listener = listener;
    }

    static class Note {
        private double time;
        private String command;
        private boolean major;
        private int val;
        private Note(double time, String command, boolean major, int val){
            this.time = time;
            this.command = command;
            this.major = major;
            this.val = val;
        }

        public String getCommand() {
            return command;
        }

        public boolean isMajor() {
            return major;
        }

        public int getVal() {
            return val;
        }
    }
}
