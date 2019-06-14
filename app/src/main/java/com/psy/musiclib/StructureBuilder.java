package com.psy.musiclib;

import android.provider.MediaStore;

import java.util.ArrayList;

class StructureBuilder {

    /**
     *
     * Find matching Album or create new in AlbumsList
     * @param track - current track
     * @param albums - Albums base
     * @return Album link
     */
    static Album findAlbum(Track track, ArrayList<Album> albums){

//        String name = track.getAlbumTitle();//to do get album name from track ;
//        String year = track.getYear();
        for (int i = 0; i < albums.size(); i++) {
            Album album = albums.get(i);
            if(album.getKey().contentEquals(Track.getAlbumKey()))
            {
                /*
                 * add track link to Album
                 */
                album.getTrackList().add(track);
                return album;
            }
        }

        /*
         * if Album with same name and year not found in base -> create new Album
         */
        Album newAlbum =new Album(track.getAlbumTitle(),track.getYear(),track.getGenre(),track.getArtist());
//        albums.add(newAlbum);
        newAlbum.getTrackList().add(track);
        return newAlbum;
    }


}
