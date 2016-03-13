package io.alstonlin.beatmix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Song {
    private ArrayList<Note> notes = new ArrayList<>();
    private int nextNote = 0;
    private String title;
    private String author;

    public static Song fromJSON(JSONObject obj) throws JSONException {
        JSONArray json = obj.getJSONArray("data");
        Song song = new Song();
        for (int i = 0; i < json.length(); i++){
            JSONObject jsonNote = json.getJSONObject(i);
            song.addNote(jsonNote.getDouble("time"), jsonNote.getString("command"), jsonNote.getBoolean("major"),
                    jsonNote.getInt("val"));
        }
        return song;
    }

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
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
