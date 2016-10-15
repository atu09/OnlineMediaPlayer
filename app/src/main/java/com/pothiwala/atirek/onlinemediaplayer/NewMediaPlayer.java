package com.pothiwala.atirek.onlinemediaplayer;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Alm on 6/30/2016.
 */


public class NewMediaPlayer extends AppCompatActivity implements ImageButton.OnClickListener {

    public static Handler mHandler = new Handler();

    //String baseUrl = "http://test.alive-mind.com/he-voice/uploads/";

    //ArrayList<SongRow> arrayList = new ArrayList<>();
    MyListView listView;
    JSONArray jsonArray;

    public static SongsAdapter_New songsAdapter;
    public static boolean isPlaying = false;
    public static boolean isClicked = false;

    //MediaPlayer mediaPlayer;
    public static ImageButton btn_play2, btn_next2, btn_prev2;
    public static TextView tv_songName;
    public static TextView tv_voiceTotalDuration;
    public static TextView tv_currentVoiceProgress;
    public static TextView tv_songCategory;
    public static TextView tv_userName;
    public static ImageButton btn_close_drawer;
    public static CircleImageView civ_userProfile;

    public static LinearLayout linearLayout_back;

    int drawerFlag = 0;
    public static double startTime = 0;

    public static double finalTime = 0;

    static MySlidingDrawer mySlidingDrawer;
    static CircleImageView civ_open_drawer;

    ViewHolder holder;

    Thread updateThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        try {
            jsonArray = new JSONArray("[{" +

                    "    \"_id\" : ObjectId(\"576e262a091bc117ebb24e47\"),\n" +
                    "    \"audio_url\" : \"576557b2c66e133cc13757f5-20160625120522.mp3\",\n" +
                    "    \"category\" : \"Lifestyle\",\n" +
                    "    \"colorCode\" : \"#57ca19\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"19840\",\n" +
                    "    \"language\" : \"Urdu\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"5772bab61d9fb9f6c7382efd\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [],\n" +
                    "    \"timeStamp\" : \"25-06-2016 12:05:22\",\n" +
                    "    \"title\" : \"android\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576557b2c66e133cc13757f5\",\n" +
                    "    \"userImage\" : \"https://lh5.googleusercontent.com/-IKpdQW1WUEE/AAAAAAAAAAI/AAAAAAAAAHs/O0vZ-A_fygs/photo.jpg\",\n" +
                    "    \"username\" : \"sahil239\"\n" +
                    "}\n" +
                    "\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"576dab8c1fcad2a0aa9c52f2\"),\n" +
                    "    \"audio_url\" : \"576daa891fcad2a0aa9c52f1-20160625032347.mp3\",\n" +
                    "    \"category\" : \"Spiritual\",\n" +
                    "    \"colorCode\" : \"#2d3e50\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"12288\",\n" +
                    "    \"language\" : \"Arabic\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"576daa891fcad2a0aa9c52f1\", \n" +
                    "        \"576daa891fcad2a0aa9c52f1\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [ \n" +
                    "        \"576daa891fcad2a0aa9c52f1\", \n" +
                    "        \"576daa891fcad2a0aa9c52f1\"\n" +
                    "    ],\n" +
                    "    \"timeStamp\" : \"25-06-2016 12:52:12\",\n" +
                    "    \"title\" : \"hhhhhhhhhh\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576daa891fcad2a0aa9c52f1\",\n" +
                    "    \"userImage\" : \"\",\n" +
                    "    \"username\" : \"Alrezqy\"\n" +
                    "}\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"576d4b636de8ed408859805a\"),\n" +
                    "    \"audio_url\" : \"576941e4a9a42afb57eee1de-20160624203152.mp3\",\n" +
                    "    \"category\" : \"Tech\",\n" +
                    "    \"colorCode\" : \"#1ca39d\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"5739\",\n" +
                    "    \"language\" : \"Arabic\",\n" +
                    "    \"like\" : [],\n" +
                    "    \"retweet\" : [ \n" +
                    "        \"576557b2c66e133cc13757f5\"\n" +
                    "    ],\n" +
                    "    \"timeStamp\" : \"24-06-2016 06:01:55\",\n" +
                    "    \"title\" : \"تجربة\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576941e4a9a42afb57eee1de\",\n" +
                    "    \"userImage\" : null,\n" +
                    "    \"username\" : \"بندر الناصر\"\n" +
                    "}\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"57716a6f83a148e72bb0ee8b\"),\n" +
                    "    \"audio_url\" : \"576941e4a9a42afb57eee1de-20160627233325.mp3\",\n" +
                    "    \"category\" : \"Politics\",\n" +
                    "    \"colorCode\" : \"#446cb4\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"5035\",\n" +
                    "    \"language\" : \"English\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"5772bab61d9fb9f6c7382efd\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [],\n" +
                    "    \"timeStamp\" : \"27-06-2016 09:03:27\",\n" +
                    "    \"title\" : \"با\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576941e4a9a42afb57eee1de\",\n" +
                    "    \"userImage\" : null,\n" +
                    "    \"username\" : \"بندر الناصر\"\n" +
                    "}\n" +
                    "\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"5767c4d2e5baa762170dbed0\"),\n" +
                    "    \"audio_url\" : \"576557b2c66e133cc13757f5-20160620155625.mp3\",\n" +
                    "    \"category\" : \"Resturant\",\n" +
                    "    \"colorCode\" : \"#2eb57b\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"4096\",\n" +
                    "    \"language\" : \"English\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [],\n" +
                    "    \"timeStamp\" : \"20-06-2016 03:56:26\",\n" +
                    "    \"title\" : \"Numbers\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576557b2c66e133cc13757f5\",\n" +
                    "    \"userImage\" : \"https://lh5.googleusercontent.com/-IKpdQW1WUEE/AAAAAAAAAAI/AAAAAAAAAHs/O0vZ-A_fygs/photo.jpg\",\n" +
                    "    \"username\" : \"sahil239\"\n" +
                    "}\n" +
                    "\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"576e96180847f7ec574e6224\"),\n" +
                    "    \"audio_url\" : \"576941e4a9a42afb57eee1de-20160625200254.mp3\",\n" +
                    "    \"category\" : \"Spiritual\",\n" +
                    "    \"colorCode\" : \"#2d3e50\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"43712\",\n" +
                    "    \"language\" : \"Arabic\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [],\n" +
                    "    \"timeStamp\" : \"25-06-2016 05:32:56\",\n" +
                    "    \"title\" : \"افتتاح\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576941e4a9a42afb57eee1de\",\n" +
                    "    \"userImage\" : null,\n" +
                    "    \"username\" : \"بندر الناصر\"\n" +
                    "}\n" +
                    "\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"576d4b776de8ed408859805e\"),\n" +
                    "    \"audio_url\" : \"576941e4a9a42afb57eee1de-20160624203212.mp3\",\n" +
                    "    \"category\" : \"Tech\",\n" +
                    "    \"colorCode\" : \"#1ca39d\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"5739\",\n" +
                    "    \"language\" : \"Arabic\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [ \n" +
                    "        \"5772bab61d9fb9f6c7382efd\"\n" +
                    "    ],\n" +
                    "    \"timeStamp\" : \"24-06-2016 06:02:15\",\n" +
                    "    \"title\" : \"تجربة4\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576941e4a9a42afb57eee1de\",\n" +
                    "    \"userImage\" : null,\n" +
                    "    \"username\" : \"بندر الناصر\"\n" +
                    "}\n" +
                    "\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"576dbab9aedb785ec6470afb\"),\n" +
                    "    \"audio_url\" : \"576941e4a9a42afb57eee1de-20160625042655.mp3\",\n" +
                    "    \"category\" : \"News\",\n" +
                    "    \"colorCode\" : \"#6d7a8a\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"7488\",\n" +
                    "    \"language\" : \"Arabic\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [ \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576557b2c66e133cc13757f5\"\n" +
                    "    ],\n" +
                    "    \"timeStamp\" : \"25-06-2016 01:56:57\",\n" +
                    "    \"title\" : \"انا وطارق #نكته\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576941e4a9a42afb57eee1de\",\n" +
                    "    \"userImage\" : null,\n" +
                    "    \"username\" : \"بندر الناصر\"\n" +
                    "}\n" +
                    "\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"576af5052a0286c195b096c4\"),\n" +
                    "    \"audio_url\" : \"576941e4a9a42afb57eee1de-20160623015850.mp3\",\n" +
                    "    \"category\" : \"Tips\",\n" +
                    "    \"colorCode\" : \"#346e7c\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"4373\",\n" +
                    "    \"language\" : \"Arabic\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"576557b2c66e133cc13757f5\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [ \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576557b2c66e133cc13757f5\"\n" +
                    "    ],\n" +
                    "    \"timeStamp\" : \"22-06-2016 11:28:53\",\n" +
                    "    \"title\" : \"hhhh\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576941e4a9a42afb57eee1de\",\n" +
                    "    \"userImage\" : null,\n" +
                    "    \"username\" : \"بندر الناصر\"\n" +
                    "}\n" +
                    "\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"57749b5853d08444d341ba88\"),\n" +
                    "    \"audio_url\" : \"577499cf53d08444d341ba84-20160630070819.mp3\",\n" +
                    "    \"category\" : \"Sports\",\n" +
                    "    \"colorCode\" : \"#23a7f1\",\n" +
                    "    \"comment\" : [],\n" +
                    "    \"duration\" : \"8853\",\n" +
                    "    \"language\" : \"English\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"5772bab61d9fb9f6c7382efd\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [ \n" +
                    "        \"5772bab61d9fb9f6c7382efd\"\n" +
                    "    ],\n" +
                    "    \"timeStamp\" : \"30-06-2016 07:08:56\",\n" +
                    "    \"title\" : \"testing\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"577499cf53d08444d341ba84\",\n" +
                    "    \"userImage\" : \"\",\n" +
                    "    \"username\" : \"prashant2415\"\n" +
                    "}\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"5772baf61d9fb9f6c7382efe\"),\n" +
                    "    \"audio_url\" : \"5772bab61d9fb9f6c7382efd-20160628232916.mp3\",\n" +
                    "    \"category\" : \"Politics\",\n" +
                    "    \"colorCode\" : \"#446cb4\",\n" +
                    "    \"comment\" : [ \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"nice\\\"}\", \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"nice 2\\\"}\", \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"nice 3\\\"}\", \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"nice 4\\\"}\", \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"nice 5\\\"}\", \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"nice 6\\\"}\", \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"nice 7\\\"}\", \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"nice 8\\\"}\", \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"nice 9\\\"}\"\n" +
                    "    ],\n" +
                    "    \"duration\" : \"11499\",\n" +
                    "    \"language\" : \"English\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"577499cf53d08444d341ba84\", \n" +
                    "        \"576557b2c66e133cc13757f5\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [ \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"576557b2c66e133cc13757f5\"\n" +
                    "    ],\n" +
                    "    \"timeStamp\" : \"28-06-2016 08:59:18\",\n" +
                    "    \"title\" : \"#test\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"5772bab61d9fb9f6c7382efd\",\n" +
                    "    \"userImage\" : \"\",\n" +
                    "    \"username\" : \"bnalnasser\"\n" +
                    "}\n" +
                    ",\n" +
                    "{\n" +
                    "    \"_id\" : ObjectId(\"5770d5edb689d03cbe948af7\"),\n" +
                    "    \"audio_url\" : \"576557b2c66e133cc13757f5-20160627125948.mp3\",\n" +
                    "    \"category\" : \"Music\",\n" +
                    "    \"colorCode\" : \"#674172\",\n" +
                    "    \"comment\" : [ \n" +
                    "        \"{\\\"userId\\\":\\\"576557b2c66e133cc13757f5\\\",\\\"username\\\":\\\"sahil239\\\",\\\"profileImage\\\":\\\"https:\\\\/\\\\/lh5.googleusercontent.com\\\\/-IKpdQW1WUEE\\\\/AAAAAAAAAAI\\\\/AAAAAAAAAHs\\\\/O0vZ-A_fygs\\\\/photo.jpg\\\",\\\"userComment\\\":\\\"my audio\\\"}\"\n" +
                    "    ],\n" +
                    "    \"duration\" : \"10496\",\n" +
                    "    \"language\" : \"English\",\n" +
                    "    \"like\" : [ \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"576941e4a9a42afb57eee1de\", \n" +
                    "        \"5772bab61d9fb9f6c7382efd\", \n" +
                    "        \"577499cf53d08444d341ba84\", \n" +
                    "        \"576557b2c66e133cc13757f5\"\n" +
                    "    ],\n" +
                    "    \"retweet\" : [],\n" +
                    "    \"timeStamp\" : \"27-06-2016 12:59:49\",\n" +
                    "    \"title\" : \"My audio\",\n" +
                    "    \"type\" : \"post\",\n" +
                    "    \"userId\" : \"576557b2c66e133cc13757f5\",\n" +
                    "    \"userImage\" : \"https://lh5.googleusercontent.com/-IKpdQW1WUEE/AAAAAAAAAAI/AAAAAAAAAHs/O0vZ-A_fygs/photo.jpg\",\n" +
                    "    \"username\" : \"sahil239\"\n" +
                    "}]");

            //Iterate the jsonArray and print the info of JSONObjects


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Constants.mediaPlayer = new MediaPlayer();
        Constants.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        listView = (MyListView) findViewById(R.id.listView_songs);
        mySlidingDrawer = (MySlidingDrawer) findViewById(R.id.slidingDrawerNew);

        civ_open_drawer = (CircleImageView) findViewById(R.id.civ_OpenDrawer);

        btn_play2 = (ImageButton) findViewById(R.id.btnPlay2);
        btn_next2 = (ImageButton) findViewById(R.id.btnForward2);
        btn_prev2 = (ImageButton) findViewById(R.id.btnBackward2);
        tv_songName = (TextView) findViewById(R.id.tv_voiceTitle);
        tv_voiceTotalDuration = (TextView) findViewById(R.id.tv_voiceDuration);
        btn_close_drawer = (ImageButton) findViewById(R.id.btn_close_drawer);

        civ_userProfile = (CircleImageView) findViewById(R.id.civ_user_profile);
        linearLayout_back = (LinearLayout) findViewById(R.id.content_background);
        tv_currentVoiceProgress = (TextView) findViewById(R.id.tv_currentVoiceProgress);
        tv_songCategory = (TextView) findViewById(R.id.tv_categoryName);
        tv_userName = (TextView) findViewById(R.id.tv_userName);


        btn_play2.setOnClickListener(this);
        btn_next2.setOnClickListener(this);
        btn_prev2.setOnClickListener(this);

        btn_close_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerFlag == 1) {
                    mySlidingDrawer.close();
                    //dialogPlus.dismiss();
                    drawerFlag = 0;
                }
            }
        });

        civ_open_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerFlag == 1) {
                    mySlidingDrawer.close();
                    //dialogPlus.dismiss();
                    drawerFlag = 0;
                } else {
                    mySlidingDrawer.open();
                    //dialogPlus.show();
                    drawerFlag = 1;
                }

            }
        });

        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                SongRow songRow = new SongRow(jsonObject.optString("audio_url").toString(),
                        jsonObject.optString("title").toString(),
                        jsonObject.optString("userImage").toString(),
                        jsonObject.optString("duration").toString(),
                        jsonObject.optString("username").toString(),
                        jsonObject.optString("category").toString(),
                        jsonObject.optString("colorCode").toString(), false, false);

                Constants.arrayList.add(songRow);

            }
        } catch (Exception e) {
            Toast.makeText(NewMediaPlayer.this, "Error" + e, Toast.LENGTH_SHORT).show();
        }

        songsAdapter = new SongsAdapter_New();
        listView.setAdapter(songsAdapter);

        //startService();
    }

    public void startService() {
        Intent serviceIntent = new Intent(NewMediaPlayer.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }


    @Override
    public void onClick(View view) {


        int id = view.getId();

        if (Constants.pos != -1) {
            Constants.arrayList.get(Constants.pos).setPlaying(false);
            Constants.arrayList.get(Constants.pos).setBuffer(false);
            songsAdapter.notifyDataSetChanged();
        }

        switch (id) {
            case R.id.btnPlay2:
                if (isPlaying) {

                    Constants.mediaPlayer.stop();
                    Constants.mediaPlayer.reset();
                    isPlaying = false;
                    isClicked = true;
                    btn_play2.setImageResource(R.drawable.home_play);
                    mHandler.removeCallbacks(UpdateSongTime);
                    tv_currentVoiceProgress.setText("00:00");

                } else {
                    play(Constants.pos);
                }
                break;

            case R.id.btnForward2:
            case R.id.btnBackward2:

                if (id == R.id.btnForward2) {
                    Constants.pos = Constants.pos + 1;
                    if (Constants.pos > (Constants.arrayList.size() - 1)) {
                        Constants.pos = 0;
                    }
                } else if (id == R.id.btnBackward2) {
                    Constants.pos = Constants.pos - 1;

                    if (Constants.pos < 0) {
                        Constants.pos = Constants.arrayList.size() - 1;
                    }

                }

                play(Constants.pos);

                break;
            default:
                break;
        }

    }

    public static void changeInitial(int position) {

        mHandler.removeCallbacks(UpdateSongTime);
        Constants.mediaPlayer.stop();
        Constants.mediaPlayer.reset();

        isPlaying = true;

        linearLayout_back.setBackgroundColor(Color.parseColor(Constants.arrayList.get(position).getColorCode()));
        btn_play2.setImageResource(R.drawable.home_pause);
        tv_songName.setText(Constants.arrayList.get(position).getSongsName());
        tv_songCategory.setText(Constants.arrayList.get(position).getSongCategory());
        tv_userName.setText(Constants.arrayList.get(position).getUserName());
        Constants.arrayList.get(position).setPlaying(true);
        Constants.arrayList.get(position).setBuffer(true);
        songsAdapter.notifyDataSetChanged();
    }

    public static void changePrepare(int position) {

        Constants.arrayList.get(position).setBuffer(false);
        songsAdapter.notifyDataSetChanged();

        finalTime = Constants.mediaPlayer.getDuration();
        startTime = Constants.mediaPlayer.getCurrentPosition();

        songProgress();
        mHandler.post(UpdateSongTime);

    }

    public static void changeComplete(int position) {
        mHandler.removeCallbacks(UpdateSongTime);
        btn_play2.setImageResource(R.drawable.home_play);
        tv_currentVoiceProgress.setText("00:00");
        Constants.arrayList.get(position).setPlaying(false);
        songsAdapter.notifyDataSetChanged();
        isPlaying = false;
    }

    public void play(final int position) {

        changeInitial(position);

        try {

            try {

                Constants.mediaPlayer.setDataSource(Constants.baseUrl + Constants.arrayList.get(position).getSongsUrl());

            } catch (IllegalArgumentException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Constants.mediaPlayer.prepareAsync();

            Constants.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {

                    mediaPlayer.start();

                    changePrepare(position);

                }
            });


            Constants.mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(android.media.MediaPlayer mediaPlayer) {

                    changeComplete(position);

                    if (!isClicked) {
                        Constants.pos = position + 1;
                        if (Constants.pos >= 0 && Constants.pos <= Constants.arrayList.size() - 1) {
                            play(Constants.pos);
                        } else {
                            Constants.pos = 0;
                            play(Constants.pos);
                        }
                    } else {
                        isClicked = false;
                    }

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        try {
            Picasso.with(getApplicationContext()).load(Constants.arrayList.get(position).getProfileUrl()).placeholder(R.drawable.user).error(R.drawable.user).resize(civ_userProfile.getWidth(), civ_userProfile.getHeight()).into(civ_userProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class ViewHolder {
        ImageView imageButtonPlay;
        TextView songName, songUrl, songTotalDuration, songCurrentDuration;
        CircularProgressView circularProgressView;
        LinearLayout layout;

    }

    public static void songProgress() {

        tv_voiceTotalDuration.setText(String.format("(%02d:%02d)",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

        tv_currentVoiceProgress.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

    }

    public static Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = Constants.mediaPlayer.getCurrentPosition();
            tv_currentVoiceProgress.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

            mHandler.postDelayed(UpdateSongTime, 200);
            songsAdapter.notifyDataSetChanged();
        }
    };

    private void startAnimationThreadStuff(long delay, final CircularProgressView progressView) {
        if (updateThread != null && updateThread.isAlive())
            updateThread.interrupt();
        // Start animation after a delay so there's no missed frames while the app loads up
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!progressView.isIndeterminate()) {
                    progressView.setProgress(0f);
                    // Run thread to update progress every quarter second until full
                    updateThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (progressView.getProgress() < progressView.getMaxProgress() && !Thread.interrupted()) {
                                // Must set progress in UI thread
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressView.setProgress(progressView.getProgress() + 20);
                                    }
                                });
                                SystemClock.sleep(50);
                            }
                        }
                    });
                    updateThread.start();
                }
                // Alias for resetAnimation, it's all the same
                progressView.startAnimation();
            }
        }, delay);
    }

    public class SongsAdapter_New extends BaseAdapter {

        LayoutInflater inflater = getLayoutInflater();

        @Override
        public int getCount() {
            return Constants.arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return Constants.arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {

            holder = new ViewHolder();
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.song_listing_item, null);

                holder.songName = (TextView) convertView.findViewById(R.id.songName);
                holder.songUrl = (TextView) convertView.findViewById(R.id.songUrl);
                holder.imageButtonPlay = (ImageView) convertView.findViewById(R.id.play);
                holder.imageButtonPlay.setFocusable(true);
                holder.songTotalDuration = (TextView) convertView.findViewById(R.id.songTotalDuration);
                holder.songCurrentDuration = (TextView) convertView.findViewById(R.id.songCurrentDuration);
                holder.layout = (LinearLayout) convertView.findViewById(R.id.listing_item_back);
                holder.circularProgressView = (CircularProgressView) convertView.findViewById(R.id.circularProgress);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final SongRow songRow = Constants.arrayList.get(i);

            holder.songName.setText(songRow.getSongsName());
            holder.songUrl.setText(songRow.getSongsUrl());

            double duration = Double.parseDouble((songRow.getSongDuration()));
            holder.songTotalDuration.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) duration),
                    TimeUnit.MILLISECONDS.toSeconds((long) duration)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) duration))));
            holder.layout.setBackgroundColor(Color.parseColor(songRow.getColorCode()));


            if (songRow.isPlaying() && Constants.arrayList.get(Constants.pos) == songRow) {
                holder.imageButtonPlay.setImageResource(R.drawable.home_pause);

                holder.songCurrentDuration.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

                if (songRow.isBuffer()) {
                    holder.circularProgressView.setVisibility(View.VISIBLE);
                    holder.circularProgressView.setIndeterminate(true);
                    startAnimationThreadStuff(0, holder.circularProgressView);

                } else {
                    if (holder.circularProgressView.getVisibility() == View.VISIBLE) {
                        holder.circularProgressView.stopAnimation();
                        holder.circularProgressView.setVisibility(View.INVISIBLE);
                    }
                }

            } else {
                holder.imageButtonPlay.setImageResource(R.drawable.home_play);
                holder.songCurrentDuration.setText("00:00");
                if (holder.circularProgressView.getVisibility() == View.VISIBLE) {
                    holder.circularProgressView.stopAnimation();
                    holder.circularProgressView.setVisibility(View.INVISIBLE);
                }
            }


            holder.imageButtonPlay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Constants.mediaPlayer.stop();
                    Constants.mediaPlayer.reset();

                    if (i == Constants.pos) {

                        isPlaying = false;
                        isClicked = true;
                        Constants.arrayList.get(Constants.pos).setBuffer(false);
                        Constants.arrayList.get(Constants.pos).setPlaying(false);
                        notifyDataSetChanged();
                        Constants.pos = -1;

                    } else {

                        if (Constants.pos != -1) {
                            isClicked = false;
                            Constants.arrayList.get(Constants.pos).setPlaying(false);
                            Constants.arrayList.get(Constants.pos).setBuffer(false);
                            notifyDataSetChanged();
                        }

                        Constants.pos = i;
                        play(Constants.pos);

                    }
                }
            });

            return convertView;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Constants.isRunning = true;

    }

    @Override
    protected void onStop() {
        super.onStop();

        Constants.isRunning = false;

    }
}


class SongRow {

    String songsUrl;
    String songsName;
    String profileUrl;
    String songDuration;
    String userName;
    String songCategory;
    String colorCode;
    boolean isPlaying;
    boolean isBuffer;

    public SongRow(String songsUrl, String songsName, String profileUrl, String songDuration, String userName, String songCategory, String colorCode, boolean isPlaying, boolean isBuffer) {
        this.songsUrl = songsUrl;
        this.songsName = songsName;
        this.profileUrl = profileUrl;
        this.songDuration = songDuration;
        this.userName = userName;
        this.songCategory = songCategory;
        this.colorCode = colorCode;
        this.isPlaying = isPlaying;
        this.isBuffer = isBuffer;
    }

    public boolean isBuffer() {
        return isBuffer;
    }

    public void setBuffer(boolean buffer) {
        isBuffer = buffer;
    }

    public String getSongsUrl() {
        return songsUrl;
    }

    public void setSongsUrl(String songsUrl) {
        this.songsUrl = songsUrl;
    }

    public String getSongsName() {
        return songsName;
    }

    public void setSongsName(String songsName) {
        this.songsName = songsName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSongCategory() {
        return songCategory;
    }

    public void setSongCategory(String songCategory) {
        this.songCategory = songCategory;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}