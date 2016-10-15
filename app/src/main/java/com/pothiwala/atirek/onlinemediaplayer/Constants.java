package com.pothiwala.atirek.onlinemediaplayer;

/**
 * Created by Alm on 7/12/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Constants {

    public static MediaPlayer mediaPlayer;
    public static ArrayList<SongRow> arrayList = new ArrayList<>();
    public static int pos = 0;
    public static String baseUrl = "http://test.alive-mind.com/he-voice/uploads/";
    public static boolean change = false;
    public static boolean isRunning = false;


    public interface ACTION {
        String MAIN_ACTION = "com.atirek.alm.musicplayerbar.action.main";
        String INIT_ACTION = "com.atirek.alm.musicplayerbar.action.init";
        String PREV_ACTION = "com.atirek.alm.musicplayerbar.action.prev";
        String PLAY_ACTION = "com.atirek.alm.musicplayerbar.action.play";
        String NEXT_ACTION = "com.atirek.alm.musicplayerbar.action.next";
        String STARTFOREGROUND_ACTION = "com.atirek.alm.musicplayerbar.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.atirek.alm.musicplayerbar.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {

        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.user, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }

        return bm;
    }

}
