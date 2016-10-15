package com.pothiwala.atirek.onlinemediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alm on 6/29/2016.
 */
public class SongsAdapter extends BaseAdapter {

    String baseUrl = "http://test.alive-mind.com/he-voice/uploads/";
    //String[] songs = {"576557b2c66e133cc13757f5-20160620121125.mp3", "57679310e5baa7307ad05ac2-20160620122455.mp3", "576557b2c66e133cc13757f5-20160620155625.mp3"};
    //int pos = 0;

    Context context;
    LayoutInflater inflater;
    List<SongsRow> songsRow;
    ArrayList<SongsRow> arraylist;

    public SongsAdapter(Context context, List<SongsRow> songsRow) {
        this.context = context;
        this.songsRow = songsRow;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<SongsRow>();
        this.arraylist.addAll(songsRow);
    }

    public class ViewHolder {
        ImageView imageButtonPlay;
        TextView songName;
        String url;
    }

    @Override
    public int getCount() {
        return songsRow.size();
    }

    @Override
    public Object getItem(int i) {
        return songsRow.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.songs_list_item, null);

            holder.songName = (TextView) convertView.findViewById(R.id.songName);
            holder.imageButtonPlay = (ImageView) convertView.findViewById(R.id.play);
            holder.url = null;

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Set the results into TextViews
        holder.songName.setText(songsRow.get(i).getSongName());
        holder.url = songsRow.get(i).getSongUrl();

        return convertView;
    }
}