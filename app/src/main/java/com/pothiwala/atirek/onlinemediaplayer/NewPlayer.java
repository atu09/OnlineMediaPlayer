package com.pothiwala.atirek.onlinemediaplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class NewPlayer extends AppCompatActivity implements ImageButton.OnClickListener {

    String baseUrl = "http://test.alive-mind.com/he-voice/uploads/";
    String[] songsUrl = {"576557b2c66e133cc13757f5-20160620121125.mp3", "57679310e5baa7307ad05ac2-20160620122455.mp3", "576557b2c66e133cc13757f5-20160620155625.mp3"};
    String[] songsName = {"Song Number 1", "Song Number 2", "Song Number 3"};

    ArrayList<SongsRow> arrayList = new ArrayList<>();
    MyListView listView;
    SongsAdapter songsAdapter;

    int pos = 0;

    ImageView btn_previosPlayView = null;
    private MediaPlayer mediaPlayer;
    //ImageButton btn_handlePlay;
    ImageButton btn_play2, btn_next2, btn_prev2;
    TextView tv_songName, tv_duration;

    int drawerFlag = 0;

    double finalTime = 0;
    View adapterView;
    SongsRow songsRow_onClick;
    ImageView imageViewPlay;

    MySlidingDrawer mySlidingDrawer;

    ImageButton btn_close_drawer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        listView = (MyListView) findViewById(R.id.listView_songs);
        mySlidingDrawer = (MySlidingDrawer) findViewById(R.id.slidingDrawerNew);

        //btn_handlePlay = (ImageButton) findViewById(R.id.handle_play);

        btn_play2 = (ImageButton) findViewById(R.id.btnPlay2);
        btn_next2 = (ImageButton) findViewById(R.id.btnForward2);
        btn_prev2 = (ImageButton) findViewById(R.id.btnBackward2);
        tv_songName = (TextView) findViewById(R.id.tv_voiceTitle);
        tv_duration = (TextView) findViewById(R.id.tv_voiceDuration);
        btn_close_drawer = (ImageButton) findViewById(R.id.btn_close_drawer);

        btn_close_drawer.setOnClickListener(this);
        btn_play2.setOnClickListener(this);
        btn_prev2.setOnClickListener(this);
        btn_next2.setOnClickListener(this);
        //btn_handlePlay.setOnClickListener(this);

        btn_close_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerFlag == 1) {
                    mySlidingDrawer.animateClose();
                    drawerFlag = 0;
                }
            }
        });

        for (int i = 0; i < songsUrl.length; i++) {
            SongsRow songsRow = new SongsRow(songsName[i], songsUrl[i]);
            arrayList.add(songsRow);
        }

        songsAdapter = new SongsAdapter(this, arrayList);
        listView.setAdapter(songsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {

                final SongsRow songsRow = (SongsRow) adapterView.getItemAtPosition(i);
                final ImageView btn_play_item = (ImageView) view.findViewById(R.id.play);


                pos = i;

                btn_play_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        btn_play2.setImageResource(R.drawable.home_play);

                        if (btn_previosPlayView != btn_play_item) {

                            if (btn_previosPlayView != null) {
                                btn_previosPlayView.setImageResource(R.drawable.btn_play);
                            }

                            //Label at handle is set to currently playing song name...
                            tv_songName.setText(songsRow.getSongName());

                            //passing the url of the selected song with the current button view of play...
                            PlayOnlineUrl(songsRow.getSongUrl(), btn_play_item);

                            btn_previosPlayView = btn_play_item;

                        } else if (btn_previosPlayView == btn_play_item) {
                            btn_play_item.setImageResource(R.drawable.btn_play);
                            //btn_handlePlay.setImageResource(R.drawable.btn_play);
                            btn_play2.setImageResource(R.drawable.home_play);
                            btn_previosPlayView = null;
                        }


                    }
                });

            }
        });

    }

    private void PlayOnlineUrl(String url, final ImageView btn_play_item) {
        // your URL here

        mediaPlayer.stop();
        mediaPlayer.reset();

        try {
            mediaPlayer.setDataSource(baseUrl + url);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        // You can show progress dialog here untill it prepared to play
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Now dismis progress dialog, Media palyer will start playing
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                tv_duration.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

                if (drawerFlag == 0) {
                    mySlidingDrawer.animateOpen();
                    drawerFlag = 1;
                }

                //Image of current button view of play is set to PAUSE..
                btn_play_item.setImageResource(R.drawable.btn_pause);
                //btn_handlePlay.setImageResource(R.drawable.btn_pause);
                btn_play2.setImageResource(R.drawable.home_pause);

            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                mediaPlayer.stop();
                mediaPlayer.reset();

/*
                if(drawerFlag == 1){
                    mySlidingDrawer.animateClose();
                    drawerFlag = 0;
                }
*/

                btn_play_item.setImageResource(R.drawable.btn_play);
                //btn_handlePlay.setImageResource(R.drawable.btn_play);
                btn_play2.setImageResource(R.drawable.home_play);

                pos = pos + 1;

                if (pos < songsName.length && pos > -1) {

                    View view = listView.getChildAt(pos);
                    ImageView imageViewPlay = (ImageView) view.findViewById(R.id.play);
                    SongsRow songsRow = (SongsRow) listView.getAdapter().getItem(pos);
                    tv_songName.setText(songsRow.getSongName());

                    PlayOnlineUrl(songsRow.getSongUrl(), imageViewPlay);

                } else {

                    pos = 0;
                    tv_songName.setText("Currently No Songs Playing");
                    tv_duration.setText("00:00");
                    btn_previosPlayView = null;
                }

            }
        });
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        mediaPlayer.stop();
        mediaPlayer.reset();

        if (btn_previosPlayView != null) {
            btn_previosPlayView.setImageResource(R.drawable.btn_play);
        }

        if (id == R.id.btnForward2) {
            pos = pos + 1;
        } else if (id == R.id.btnBackward2) {
            pos = pos - 1;
        }

        if (pos < 0) {
            pos = songsName.length - 1;
        } else if (pos > (songsName.length - 1)) {
            pos = 0;
        }

        adapterView = listView.getChildAt(pos);
        songsRow_onClick = (SongsRow) listView.getAdapter().getItem(pos);
        imageViewPlay = (ImageView) adapterView.findViewById(R.id.play);

        imageViewPlay.setImageResource(R.drawable.btn_play);
        //btn_handlePlay.setImageResource(R.drawable.btn_play);
        btn_play2.setImageResource(R.drawable.home_play);


        switch (id) {
            case R.id.btnPlay2:

                if (btn_previosPlayView != imageViewPlay) {

                    //Label at handle is set to currently playing song name...
                    tv_songName.setText(songsRow_onClick.getSongName());

                    //passing the url of the selected song with the current button view of play...
                    PlayOnlineUrl(songsRow_onClick.getSongUrl(), imageViewPlay);

                    btn_previosPlayView = imageViewPlay;

                } else if (btn_previosPlayView == imageViewPlay) {
                    tv_songName.setText("Currently No Songs Playing");
                    tv_duration.setText("00:00");
                    btn_previosPlayView = null;
                }

                break;

            case R.id.btnForward2:

                if (pos >= 0 && pos <= (songsName.length - 1)) {

                    tv_songName.setText(songsRow_onClick.getSongName());
                    PlayOnlineUrl(songsRow_onClick.getSongUrl(), imageViewPlay);
                    btn_previosPlayView = imageViewPlay;

                }

                break;

            case R.id.btnBackward2:

                if (pos >= 0 && pos <= (songsName.length - 1)) {

                    tv_songName.setText(songsRow_onClick.getSongName());
                    PlayOnlineUrl(songsRow_onClick.getSongUrl(), imageViewPlay);
                    btn_previosPlayView = imageViewPlay;

                }

                break;
        }
    }
}
