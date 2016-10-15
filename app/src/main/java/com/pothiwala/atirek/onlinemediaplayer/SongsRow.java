package com.pothiwala.atirek.onlinemediaplayer;

/**
 * Created by Alm on 6/29/2016.
 */
public class SongsRow {

    public String songName;
    public String songUrl;
    public String profileUrl;

    public SongsRow(String songName, String songUrl) {
        this.songName = songName;
        this.songUrl = songUrl;
    }

    public SongsRow(String songName, String songUrl, String profileUrl) {
        this.songName = songName;
        this.songUrl = songUrl;
        this.profileUrl = profileUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }
}
