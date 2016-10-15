package com.pothiwala.atirek.onlinemediaplayer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.TimeUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Player extends Activity {


    String baseUrl = "http://test.alive-mind.com/he-voice/uploads/";
    String[] songs = {"576557b2c66e133cc13757f5-20160620121125.mp3", "57679310e5baa7307ad05ac2-20160620122455.mp3", "576557b2c66e133cc13757f5-20160620155625.mp3"};
    int pos = 0;

    private ImageButton btnPlay;
    private ImageButton btnClose;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnRepeat;
    private ImageButton btnShuffle;
    private SeekBar songProgressBar;
    private SeekBar volumeProgressBar = null;
    private AudioManager audioManager = null;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    // Media Player
    private MediaPlayer mediaPlayer;

    private TextView voluumePlusLabel;
    private TextView volumeMinusLabel;


    private double startTime = 0;
    private double finalTime = 0;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds

    private boolean isShuffle = false;
    private boolean isRepeat = false;
    public static int oneTimeOnly = 0;
    public boolean flag = false;

    Utilities utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // All player buttons
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        volumeProgressBar = (SeekBar) findViewById(R.id.volumeProgressBar);
        voluumePlusLabel = (TextView) findViewById(R.id.textView_VolumePlus);
        volumeMinusLabel = (TextView) findViewById(R.id.textView_VolumeMinus);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

        utils = new Utilities();

        // Mediaplayer
        mediaPlayer = MediaPlayer.create(this, R.raw.dhoom_remix);

        //mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeProgressBar.setMax(maxVolume);
        volumeProgressBar.setProgress(curVolume);

        songProgressBar.setClickable(false);
        songProgressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(UpdateSongTime);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(UpdateSongTime);
                int currentPosition = seekBar.getProgress();

                // forward or backward to certain seconds
                mediaPlayer.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();

            }
        });

        volumeProgressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
            }
        });

        btnPlay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // check for already playing
                if (mediaPlayer.isPlaying()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                        Toast.makeText(getApplicationContext(), "Pause", Toast.LENGTH_SHORT).show();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                } else {
                    // Resume song
                    if (mediaPlayer.getCurrentPosition() > 0) {

                        mediaPlayer.start();
                        finalTime = mediaPlayer.getDuration();
                        startTime = mediaPlayer.getCurrentPosition();

                        if (oneTimeOnly == 0) {
                            songProgressBar.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }

                        songProgress();

                        songProgressBar.setProgress((int) startTime);
                        mHandler.postDelayed(UpdateSongTime, 100);

                        Toast.makeText(getApplicationContext(), "Resume", Toast.LENGTH_SHORT).show();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    } else if (mediaPlayer.getCurrentPosition() == 0) {

                        mediaPlayer.start();
                        finalTime = mediaPlayer.getDuration();
                        startTime = mediaPlayer.getCurrentPosition();

                        if (oneTimeOnly == 0) {
                            songProgressBar.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }

                        songProgress();

                        songProgressBar.setProgress((int) startTime);
                        mHandler.postDelayed(UpdateSongTime, 100);

                        Toast.makeText(getApplicationContext(), "Play", Toast.LENGTH_SHORT).show();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);

                    }

                }

            }
        });

        btnForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // get current song position
/*
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                    // forward song
                    mediaPlayer.seekTo(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }
*/


            }
        });

        btnBackward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

/*
                // get current song position
                int currentPosition = mediaPlayer.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if (currentPosition - seekBackwardTime >= 0) {
                    // backward song
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime);
                } else {
                    // backward to starting position
                    mediaPlayer.seekTo(0);
                }
*/

            }
        });


        btnRepeat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                } else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);

                    mediaPlayer.setLooping(true);

                }
            }
        });

        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.seekTo(0);
                btnPlay.setImageResource(R.drawable.btn_play);
                Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_SHORT).show();
            }
        });

        btnShuffle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                } else {
                    // make repeat to true
                    isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);

                }
            }
        });

        btnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                btnClose.setImageResource(R.drawable.img_btn_close_pressed);
                Toast.makeText(getApplicationContext(), "Closing BeatsAudio", Toast.LENGTH_SHORT).show();
                releaseMediaPlayer();
                finish();
                onDestroy();
            }
        });

        voluumePlusLabel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                volumeUp();
            }
        });
        volumeMinusLabel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                volumeDown();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            //Do something
            volumeDown();
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            //Do something
            volumeUp();
        }
        return true;
    }

    private void volumeUp() {
        int index = volumeProgressBar.getProgress() + 1;
        volumeProgressBar.setProgress(index);

        if (index == volumeProgressBar.getMax()) {
            AlertDialog alertDialog = new AlertDialog.Builder(Player.this).create();
            alertDialog.setTitle("BeatsAudio");
            alertDialog.setMessage("High volume for long duration may damage your hearing.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });

            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.show();
        }

    }

    private void volumeDown() {
        int index = volumeProgressBar.getProgress() - 1;
        volumeProgressBar.setProgress(index);
        if (index == 0) {
            Toast.makeText(getApplicationContext(), "Mute", Toast.LENGTH_LONG).show();
        }
    }

    private void songProgress() {

        songTotalDurationLabel.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

        songCurrentDurationLabel.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            //   mp1.release();
            //	mp2.release();
            mediaPlayer = null;
            //   mp1 = null;
            //  mp2 = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releaseMediaPlayer();
        finish();
        onDestroy();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (UpdateSongTime != null)
            mHandler.removeCallbacks(UpdateSongTime);
        super.onDestroy();
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            songCurrentDurationLabel.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            songProgressBar.setProgress((int) startTime);
            mHandler.postDelayed(this, 100);
        }
    };

    public void updateProgressBar() {
        mHandler.postDelayed(UpdateSongTime, 100);
    }

}
