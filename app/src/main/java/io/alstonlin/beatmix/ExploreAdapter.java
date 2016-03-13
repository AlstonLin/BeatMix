package io.alstonlin.beatmix;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

public class ExploreAdapter extends BaseAdapter{
    private ArrayList<Song> songs = new ArrayList<>();
    private MainActivity activity;

    public ExploreAdapter(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item, null);
        }
        final Song s = songs.get(position);
        TextView titleView = (TextView) convertView.findViewById(R.id.title);
        TextView authorView = (TextView) convertView.findViewById(R.id.author);
        titleView.setText(s.getTitle());
        authorView.setText(s.getAuthor());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DAO.getInstance().requestSong(s);
                    final Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.song_dialog);
                    dialog.show();
                    s.setOnSongReadyListener(new OnSongReadyListener() {
                        @Override
                        public void onSongReady(Song song) {
                            TextView title = (TextView) dialog.findViewById(R.id.title);
                            TextView author = (TextView) dialog.findViewById(R.id.author);
                            Button close = (Button) dialog.findViewById(R.id.close);
                            title.setText(song.getTitle());
                            author.setText(song.getAuthor());
                            final Playback playback = new Playback(song, activity, false);
                            playback.execute();
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    playback.cancel(false);
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    public void setSongs(ArrayList<Song> songs){
        this.songs = songs;
        notifyDataSetChanged();
    }
}
