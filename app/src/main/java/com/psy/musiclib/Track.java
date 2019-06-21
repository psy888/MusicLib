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
    private String mYear;
    private String mGenre;
    private String mAlbumTitle;
    private Album mAlbum; //Link to Album Object???
    private File mTrackFile; // save file destination



    public Track(File trackFile) {
        String unknown = "unknown";
        mTrackFile = trackFile;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(trackFile.getAbsolutePath());
        mTitle = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE):unknown;
        mArtist = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST):unknown;
//        mDuration = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!=null);
        mYear = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR):"-";
        mGenre = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE):"-";
        mAlbumTitle = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM):unknown;
//        mAlbumKey = MediaStore.Audio.keyFor(mAlbumTitle);

        MainActivity.mTrackList.add(this);
        mAlbum = StructureBuilder.findAlbum((MainActivity.mTrackList.size()-1),MainActivity.mAlbumsList);//???
        byte[] src = mmr.getEmbeddedPicture();
        if(src!=null) {
            mAlbum.setCover(src);
        }

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
