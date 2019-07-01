package com.psy.musiclib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import java.io.File;
import java.io.Serializable;

class Track implements Serializable {
    private static String mAlbumKey;
    private String mTitle;
    private String mArtist;
    private String mDuration;
    private String mYear;
    private String mGenre;
    private String mAlbumTitle;
    private Album mAlbum; //Link to Album Object???
    private File mTrackFile; // save file destination
    private int mAlbumIndex;



    public Track(File trackFile) {
        String unknown = "unknown";
        mTrackFile = trackFile;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(trackFile.getAbsolutePath());
            mTitle = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE):getTitleFromFileName(trackFile);
            mArtist = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST):getArtistFromFileName(trackFile);
            mYear = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR):"-";
            mGenre = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE):"-";
            mAlbumTitle = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)!=null)?mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM):unknown;


        }catch (RuntimeException e){

            mTitle = getTitleFromFileName(trackFile);
            mArtist =getArtistFromFileName(trackFile);
            mYear = "-";
            mGenre ="-";
            mAlbumTitle = unknown;
        }

        MainActivity.mTrackList.add(this);
        mAlbumIndex = StructureBuilder.findAlbum((MainActivity.mTrackList.size()-1),MainActivity.mAlbumsList);//???
        byte[] src = mmr.getEmbeddedPicture();
        if(src!=null) {
            MainActivity.mAlbumsList.get(mAlbumIndex).setCover(src);
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

    String getArtistFromFileName(File f){
        String artist = f.getName();
        artist = artist.substring(0,getIndexOfDevider(artist));
        return artist;
    }
    //index + 1
    String getTitleFromFileName(File f){
        String title = f.getName();

        title = title.substring(getIndexOfDevider(title)+ 1,title.lastIndexOf("."));
        return title;
    }

    int getIndexOfDevider(String str){
        int index = str.indexOf("-");
        if(index>-1){
            return index;
        }
        index = str.indexOf(" ");
        if(index>-1){
            return index;
        }
        return 0;
    }
}
