package com.psy.musiclib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import java.io.File;

class Track {
    private static String mAlbumKey;
    private String mTitle;
    private String mArtist;
    private String mDuration;
    private String mYear; //todo: get from album or set to album
    private String mGenre; //todo: get from album or set to album
    private String mAlbumTitle;
    private Album mAlbum; //Link to Album Object???
    private File mTrackFile; // save file destination



    public Track(File trackFile) {
        mTrackFile = trackFile;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(trackFile.getAbsolutePath());
        mTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        mArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        mDuration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mYear = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
        mGenre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        mAlbumTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        mAlbumKey = MediaStore.Audio.keyFor(mAlbumTitle);

        mAlbum = StructureBuilder.findAlbum(this,MainActivity.mAlbumsList);//???
        byte[] src = mmr.getEmbeddedPicture();
        mAlbum.setCover(BitmapFactory.decodeByteArray(src,0, src.length));

    }

    public static String getAlbumKey() {
        return mAlbumKey;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public Album getAlbum() {
        return mAlbum;
    }

    public void setAlbum(Album album) {
        mAlbum = album;
    }

    String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        mYear = year;
    }

    String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public File getTrackFile() {
        return mTrackFile;
    }

    public void setTrackFile(File trackFile) {
        mTrackFile = trackFile;
    }

    String getAlbumTitle() { return mAlbumTitle; }
}
