package com.psy.musiclib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.graphics.Palette;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private ArrayList<Integer> mTrackList;
//    private ArrayList<Track> mTrackList;
    private String mName;
    private String mYear;
    private  String mGenre;
    private String mArtist;
    private int mTrackCnt;
    private Uri CoverUri; //todo get from Internet ??
    private String mKey;
//    private Bitmap mCover;
    private byte[] mCover;
    transient private Bitmap mCoverBitmap;
    private int mVibrantColor;
    /**
     *_
     * @param name - Album title
     * @param year - Album Year
     * @param genre - Album genre
     * @param artist - Album artist;
     */
    Album(String name, String year, String genre, String artist) {
        String unknown = "Unknown";
        mName = (name!=null)?name:unknown;
        mYear = (year!=null)?year:"-";
        mGenre = (genre!=null)?genre:"-";
        mArtist = (artist!=null)?artist:unknown;
        mKey = MediaStore.Audio.keyFor(name);
        mTrackList = new ArrayList<>();
        mVibrantColor = Color.rgb(128,128,128);

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
        ArrayList<Track> trackList = new ArrayList<>();
        for (int i = 0; i < mTrackList.size(); i++) {
            trackList.add(MainActivity.mTrackList.get(this.mTrackList.get(i)));
        }
        return trackList;
    }

    public void addTrack(int i){
        mTrackList.add(i);
    }


    public String getKey() { return mKey;}

    @Override
    public String toString() {
        return "Name : " + mName + " | " +
                "Year : " + mYear + " | " +
                "Genre : " + mGenre + " | " +
                "Artist : " + mArtist + " | ";
    }

    public void setCover(byte[] BitmapByteArray) {
        mCover = BitmapByteArray;
        //ToDo compress Bitmap byte array
        Log.d("ALBUM COVER", "Byte Array IN = " + (mCover.length / 1024) + " kb");
        //Compress bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options,50,50);
        mCoverBitmap = BitmapFactory.decodeByteArray(mCover, 0, mCover.length, options);
        //compress byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mCoverBitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        mCover = baos.toByteArray();
        Log.d("ALBUM COVER", "Byte Array OUT = " + (mCover.length / 1024) + " kb");
        getBgColor(mCoverBitmap);
    }

    public Bitmap getCover() {
        if(mCover==null) return null;
        if(mCoverBitmap==null) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options,150,150);
        mCoverBitmap = BitmapFactory.decodeByteArray(mCover, 0, mCover.length, options); }
        return mCoverBitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public void getBgColor(Bitmap bitmap){
        mVibrantColor = Palette.from(bitmap).generate().getMutedColor(Color.rgb(128, 128, 128));
    }

    public int getVibrantColor() {
        return mVibrantColor;
    }

}
