package com.psy.musiclib;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;

public class Album {
    private String mName;
    private String mYear;
    private  String mGenre;
    private String mArtist;
    private int mTrackCnt;
    private ArrayList<Track> mTrackList;
    private Uri CoverUri; //todo get from Internet ??
    private String mKey;
    private Bitmap mCover;
    private int mVibrantColor;
    /**
     *
     * @param name - Album title
     * @param year - Album Year
     * @param genre - Album genre
     * @param artist - Album artist;
     */
    Album(String name, String year, String genre, String artist) {
        mName = name;
        mYear = year;
        mGenre = genre;
        mArtist = artist;
        mKey = MediaStore.Audio.keyFor(name);
        mTrackList = new ArrayList<>();
        //add to base
        MainActivity.mAlbumsList.add(this);
    }

    //todo make request name, artist, year -  get Album cover uri

    //--------Getters & Setters

    String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        mYear = year;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public int getTrackCnt() {
        return mTrackCnt;
    }

    public void setTrackCnt(int trackCnt) {
        mTrackCnt = trackCnt;
    }

    ArrayList<Track> getTrackList() {
        return mTrackList;
    }

    public void setTrackList(ArrayList<Track> trackList) {
        mTrackList = trackList;
    }

    public Uri getCoverUri() {
        return CoverUri;
    }

    public void setCoverUri(Uri coverUri) {
        CoverUri = coverUri;
    }

    public String getKey() { return mKey;}

    @Override
    public String toString() {
        return "Name : " + mName + " | " +
                "Year : " + mYear + " | " +
                "Genre : " + mGenre + " | " +
                "Artist : " + mArtist + " | ";
    }

    public void setCover(Bitmap decodeByteArray) {
        mCover = decodeByteArray;
        getBgColor(mCover);
    }

    public Bitmap getCover() {
        return mCover;
    }

    public void getBgColor(Bitmap bitmap){
//        if(bitmap!=null) {
//            mVibrantColor = Palette.from(bitmap).generate().getDominantColor(Color.rgb(128,128,128));
            mVibrantColor = Palette.from(bitmap).generate().getMutedColor(Color.rgb(128,128,128));
            /*Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    mVibrantColor = palette.getVibrantSwatch().getRgb();
                }
            });*/
//        }else{
//         mVibrantColor = Color.rgb(192,192,192);
//        }
    }

    public int getVibrantColor() {
        return mVibrantColor;
    }
}
