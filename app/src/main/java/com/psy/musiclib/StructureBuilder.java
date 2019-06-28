package com.psy.musiclib;


import android.provider.MediaStore;

import java.util.ArrayList;

class StructureBuilder {

    /**
     *
     * Find matching Album or create new in AlbumsList
     * @param trackNumber - current track number in global TrackList
     * @param albums - Albums base
     * @return Album link
     */
    static int findAlbum(int trackNumber, ArrayList<Album> albums){

        Track track = MainActivity.mTrackList.get(trackNumber);
        String albumKey = MediaStore.Audio.keyFor(track.getAlbumTitle());
//        String name = track.getAlbumTitle();//to do get album name from track ;
//        String year = track.getYear();
        for (int i = 0; i < albums.size(); i++) {
            Album album = albums.get(i);
            if(album!=null&&album.getKey().contentEquals(albumKey))
            {
                /*
                 * add track link to Album
                 */
                album.addTrack(trackNumber);

                return albums.indexOf(album);
            }
        }

        /*
         * if Album with the same name and year not found in base -> create new Album
         */
        Album newAlbum =new Album(track.getAlbumTitle(),track.getYear(),track.getGenre(),track.getArtist());
//        albums.add(newAlbum);
        newAlbum.addTrack(trackNumber);
        return albums.indexOf(newAlbum);
//        return newAlbum;
    }


}
