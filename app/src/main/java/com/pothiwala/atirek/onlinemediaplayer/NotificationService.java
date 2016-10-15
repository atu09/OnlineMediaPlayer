package com.pothiwala.atirek.onlinemediaplayer;

/**
 * Created by Alm on 7/12/2016.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class NotificationService extends Service {

    Notification status;
    private final String LOG_TAG = "NotificationService";
    RemoteViews bigViews;


    public void showNotification() {

        // Using RemoteViews to bind custom layouts into Notification
        //RemoteViews views = new RemoteViews(getPackageName(), R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        //
        //
        //
        //
        //
        //

        // showing default album image
        //views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        //views.setViewVisibility(R.id.status_bar_album_art, View.GONE);

/*
        String profileUrl = Constants.arrayList.get(Constants.pos).getProfileUrl();
        if (profileUrl.isEmpty() || profileUrl == null)
            bigViews.setImageViewBitmap(R.id.civ_user_profile09, Constants.getDefaultAlbumArt(this));
        else
            bigViews.setImageViewUri(R.id.civ_user_profile09, getUriFromURL(profileUrl));
*/

        //bigViews.setImageViewBitmap(R.id.civ_user_profile09, Constants.getDefaultAlbumArt(this));
        loadBitmap(Constants.arrayList.get(Constants.pos).getProfileUrl());

        //
        //
        //
        //
        //
        //

        Intent notificationIntent = new Intent(this, NewMediaPlayer.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);


        //
        //
        //
        //
        //
        //

        bigViews.setOnClickPendingIntent(R.id.btnPlay09, pplayIntent);

        bigViews.setOnClickPendingIntent(R.id.btnForward09, pnextIntent);

        bigViews.setOnClickPendingIntent(R.id.btnBackward09, ppreviousIntent);

        bigViews.setOnClickPendingIntent(R.id.btn_close_drawer09, pcloseIntent);

        //
        //
        //
        //
        //
        //

        if (Constants.change) {
            bigViews.setImageViewResource(R.id.btnPlay09, R.drawable.home_pause);
        } else {
            bigViews.setImageViewResource(R.id.btnPlay09, R.drawable.home_play);
        }

        bigViews.setTextViewText(R.id.tv_userName09, Constants.arrayList.get(Constants.pos).getUserName());

        bigViews.setTextViewText(R.id.tv_voiceTitle09, Constants.arrayList.get(Constants.pos).getSongsName());

        bigViews.setTextViewText(R.id.tv_categoryName09, Constants.arrayList.get(Constants.pos).getSongCategory());

        //
        //
        //
        //
        //
        //

        status = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent).setContent(bigViews).build();
        status.flags = Notification.FLAG_ONGOING_EVENT;

        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);

    }

    private Target loadtarget;

    public void loadBitmap(String url) {

        if (loadtarget == null) loadtarget = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                bigViews.setImageViewBitmap(R.id.civ_user_profile09, bitmap);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                bigViews.setImageViewResource(R.id.civ_user_profile09, R.drawable.ph_user);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this).load(url).into(loadtarget);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {

            showNotification();
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {

            Constants.pos = Constants.pos - 1;
            Constants.change = true;
            stopMedia();

            if (Constants.pos >= 0 && Constants.pos <= Constants.arrayList.size() - 1) {
                play(Constants.pos);
            } else {
                Constants.pos = Constants.arrayList.size() - 1;
                play(Constants.pos);
            }


            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Previous");

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {

            Log.d("Position>>>>", Constants.pos + "");

/*
            for (int i = 0; i < Constants.arrayList.size(); i++) {
                Log.d("Listify : ", Constants.arrayList.get(i).getSongsName());
            }
*/

            stopMedia();
            if (Constants.change) {
                Constants.change = false;
                showNotification();
            } else {
                Constants.change = true;
                play(Constants.pos);
            }

            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Play");


        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {

            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Next");

            Constants.pos = Constants.pos + 1;
            Constants.change = true;
            stopMedia();



            if (Constants.pos >= 0 && Constants.pos <= Constants.arrayList.size() - 1) {
                play(Constants.pos);
            } else {
                Constants.pos = 0;
                play(Constants.pos);
            }


        } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {

            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopMedia();
            stopForeground(true);
            stopSelf();

        }
        return START_STICKY;
    }


    public void play(final int position) {

        showNotification();
        Log.d("Profile URL>>>>", Constants.arrayList.get(position).getProfileUrl());

        try {

            try {

                if(Constants.isRunning) {
                    NewMediaPlayer.changeInitial(position);
                } else {
                    NewMediaPlayer.isPlaying = true;
                    Constants.arrayList.get(position).setPlaying(true);
                    Constants.arrayList.get(position).setBuffer(true);
                }

                Constants.mediaPlayer.setDataSource(Constants.baseUrl + Constants.arrayList.get(position).getSongsUrl());

            } catch (IllegalArgumentException e) {
                //Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                Log.d("Exception1>>>>", "You might not set the URI correctly!");
            } catch (SecurityException e) {
                //Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                Log.d("Exception2>>>>", "You might not set the URI correctly!");
            } catch (IllegalStateException e) {
                //Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                Log.d("Exception3>>>>", "You might not set the URI correctly!");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Exception4>>>>", e.getMessage());
            }

            Constants.mediaPlayer.prepareAsync();

            Constants.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {

                    mediaPlayer.start();

                    if(Constants.isRunning) {
                        NewMediaPlayer.changePrepare(position);
                    } else {
                        Constants.arrayList.get(position).setBuffer(false);
                    }

                }
            });


            Constants.mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(android.media.MediaPlayer mediaPlayer) {

                    stopMedia();
                    Constants.arrayList.get(position).setPlaying(false);

                    Constants.pos = position + 1;
                    if(Constants.isRunning) {
                        NewMediaPlayer.changeComplete(position);
                    } else {
                        Constants.arrayList.get(position).setPlaying(false);
                    }

                    if (Constants.pos >= 0 && Constants.pos <= Constants.arrayList.size() - 1) {

                        play(Constants.pos);

                    } else {

                        Constants.pos = 0;
                        Constants.change = false;
                        showNotification();

                    }

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    public void stopMedia() {
        Constants.mediaPlayer.stop();
        Constants.mediaPlayer.reset();
    }

}
