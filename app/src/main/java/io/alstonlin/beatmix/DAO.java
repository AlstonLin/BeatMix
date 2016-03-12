package io.alstonlin.beatmix;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Data Access Object; Handles all interaction with the server.
 */
public class DAO {
    public static final String BASE_URL = "http://ec2-52-87-190-6.compute-1.amazonaws.com/";
    private static DAO instance;
    private Socket socket;
    private Playable player;
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
    private Emitter.Listener endChordListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
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
        else if (instance.player == null) throw new IllegalStateException("Player must first be set " +
                "immediately after first get!");
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
                .on(Socket.EVENT_DISCONNECT, disconntectListener);
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
}