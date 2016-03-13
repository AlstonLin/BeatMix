package io.alstonlin.beatmix;

import android.content.Context;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Data Access Object; Handles all interaction with the server.
 */
public class DAO {
    public static final String BASE_URL = "http://ec2-52-87-190-6.compute-1.amazonaws.com/";
    private static DAO instance;
    private Socket socket;
    private Playable player;
    private Song requestedSong;
    private MainActivity activity;
    private ExploreAdapter adapter;
    private Emitter.Listener connectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        }
    };
    private Emitter.Listener playChordListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject obj = (JSONObject) args[0];
            try {
                boolean major = obj.getBoolean("major");
                int n = obj.getInt("note");
                player.playChord(major, n);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Emitter.Listener getSongsListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONArray array = (JSONArray) args[0];
            final ArrayList<Song> songs = new ArrayList<>();
            for (int i = 0; i < array.length();  i++){
                try {
                    JSONObject obj = array.getJSONObject(i);
                    String id = obj.getString("_id");
                    String title = obj.getString("title");
                    String author = obj.getString("author");
                    Song s = new Song();
                    s.setId(id);
                    s.setTitle(title);
                    s.setAuthor(author);
                    songs.add(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setSongs(songs);
                }
            });
        }
    };
    private Emitter.Listener getSongListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONArray array = (JSONArray) args[0];
                JSONObject obj = array.getJSONObject(0);
                // Removes escaped chars
                String contentString = obj.getString("content").replace("\\\"", "\"");
                contentString = "{" + contentString.substring(1, contentString.length() - 1) + "}";
                final JSONObject song = new JSONObject(contentString);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            requestedSong.loadContentFromJson(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    };
    private Emitter.Listener endChordListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            player.endChord();
        }
    };
    private Emitter.Listener disconntectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        }
    };

    /**
     * Gets the Singleton instance (lazy instantiation).
     * @return The Singleton instance
     */
    public static DAO getInstance(){
        if (instance == null) instance = new DAO();
        return instance;
    }

    /**
     * Prevents being instantiated outside of Singleton
     */
    private DAO(){
        try {
            socket = IO.socket(BASE_URL);
        } catch (URISyntaxException e) {}
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, connectListener).on("command", playChordListener).on("end", endChordListener)
                .on("songs", getSongsListener).on("song", getSongListener).on(Socket.EVENT_DISCONNECT, disconntectListener);
    }

    public void joinRoom(String code) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("code", code);
        socket.emit("join", object);
    }

    public void sendPlayChord(boolean major, int n) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("major", major);
        object.put("note", n);
        socket.emit("command", object);
    }

    public void sendEndChord(){
        socket.emit("end");
    }

    public void setPlayable(Playable player){
        this.player = player;
    }

    public void sendSong(Song s) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("title", s.getTitle());
        object.put("author", s.getAuthor());
        object.put("content", s.toJSON());
        socket.emit("addSong", object);
    }

    public void requestSongs(ExploreAdapter adapter){
        socket.emit("getSongs");
        this.adapter = adapter;
    }

    public void requestSong(Song s) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("songId", s.getId());
        socket.emit("getSong", object);
        this.requestedSong = s;
    }

    public void setActivity(MainActivity activity){
        this.activity = activity;
    }
}