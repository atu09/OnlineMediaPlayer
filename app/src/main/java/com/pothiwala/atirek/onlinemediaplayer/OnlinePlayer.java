package com.pothiwala.atirek.onlinemediaplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Atirek on 6/20/2016.
 */
public class OnlinePlayer extends AppCompatActivity implements ImageButton.OnClickListener {


    ImageButton btnDirectPlay;
    TextView songName;

    private ImageButton btnPlay2;
    private ImageButton btnForward2;
    private ImageButton btnBackward2;

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
    private TextView voluumePlusLabel;
    private TextView volumeMinusLabel;
    // Media Player
    private MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();


    MySlidingDrawer slidingDrawer;

    String baseUrl = "http://test.alive-mind.com/he-voice/uploads/";
    String[] songs = {"576557b2c66e133cc13757f5-20160620121125.mp3", "57679310e5baa7307ad05ac2-20160620122455.mp3", "576557b2c66e133cc13757f5-20160620155625.mp3"};
    int pos = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        voluumePlusLabel = (TextView) findViewById(R.id.textView_VolumePlus);
        volumeMinusLabel = (TextView) findViewById(R.id.textView_VolumeMinus);

        btnDirectPlay = (ImageButton) findViewById(R.id.handle_play);

        slidingDrawer = (MySlidingDrawer) findViewById(R.id.slidingDrawer);


        btnPlay2 = (ImageButton) findViewById(R.id.btnPlay2);
        btnForward2 = (ImageButton) findViewById(R.id.btnForward2);
        btnBackward2 = (ImageButton) findViewById(R.id.btnBackward2);

        btnDirectPlay.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnBackward.setOnClickListener(this);
        btnBackward2.setOnClickListener(this);
        btnPlay2.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnForward2.setOnClickListener(this);


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeProgressBar.setMax(maxVolume);
        volumeProgressBar.setProgress(curVolume);

        songProgressBar.setClickable(false);


        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        volumeProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
            }
        });

        voluumePlusLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                volumeUp();
            }
        });

        volumeMinusLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                volumeDown();
            }
        });

    }

    private void PlayOnlineUrl(String url) {
        // your URL here

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
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    songProgressBar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                songProgress();

                songProgressBar.setProgress((int) startTime);
                mHandler.postDelayed(UpdateSongTime, 100);

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
                songCurrentDurationLabel.setText("00 min, 00 sec");
                songTotalDurationLabel.setText("00 min, 00 sec");
                pos++;
                if (pos < songs.length) {
                    PlayOnlineUrl(songs[pos]);
                    oneTimeOnly = 0;
                } else {
                    pos = 0;
                    PlayOnlineUrl(songs[pos]);
                    oneTimeOnly = 0;
                }
            }
        });
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
            AlertDialog alertDialog = new AlertDialog.Builder(OnlinePlayer.this).create();
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
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnPlay:
            case R.id.btnPlay2:
            case R.id.handle_play:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    //mediaPlayer.stop();
                    btnPlay.setImageResource(R.drawable.btn_play);
                    btnPlay2.setImageResource(R.drawable.btn_play);
                    btnDirectPlay.setImageResource(R.drawable.btn_play);
                } else {
                    PlayOnlineUrl(songs[pos]);
                    btnPlay.setImageResource(R.drawable.btn_pause);
                    btnPlay2.setImageResource(R.drawable.btn_pause);
                    btnDirectPlay.setImageResource(R.drawable.btn_pause);
                }
                break;

            case R.id.btnForward:
            case R.id.btnForward2:
                mediaPlayer.stop();
                mediaPlayer.reset();

                songCurrentDurationLabel.setText("00 min, 00 sec");
                songTotalDurationLabel.setText("00 min, 00 sec");

                pos++;
                if (pos < songs.length) {
                    PlayOnlineUrl(songs[pos]);
                    oneTimeOnly = 0;
                } else {
                    pos = 0;
                    PlayOnlineUrl(songs[pos]);
                    oneTimeOnly = 0;
                }
                break;

            case R.id.btnBackward:
            case R.id.btnBackward2:
                mediaPlayer.stop();
                mediaPlayer.reset();

                songCurrentDurationLabel.setText("00 min, 00 sec");
                songTotalDurationLabel.setText("00 min, 00 sec");

                pos--;
                if (pos >= 0) {
                    PlayOnlineUrl(songs[pos]);
                    oneTimeOnly = 0;
                } else {
                    pos = songs.length - 1;
                    PlayOnlineUrl(songs[pos]);
                    oneTimeOnly = 0;
                }
                break;

        }
    }
}