package com.psy.musiclib;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import static com.psy.musiclib.MainActivity.mAlbumsList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ALBUM_INDEX = "index";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mAlbumIndex;
    private static ArrayList<Track> mTrackList;
    ListView mlvPlaylist;
    View currentPlying;
//    private String mParam2;
protected MediaPlayer mMediaPlayer;

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<Track> mAdapterPlayList;


    public PlayListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param albumIndex album Index
     * @return A new instance of fragment PlayListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListFragment newInstance(int albumIndex) {
        PlayListFragment fragment = new PlayListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ALBUM_INDEX, albumIndex);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAlbumIndex = getArguments().getInt(ARG_ALBUM_INDEX);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_play_list, container, false);
        mTrackList = mAlbumsList.get(mAlbumIndex).getTrackList();
        mlvPlaylist = layout.findViewById(R.id.lvPlaylist);

        mAdapterPlayList = new ArrayAdapter<Track>(inflater.getContext(), R.layout.playlist_item, R.id.tvTrackName, mTrackList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View v = super.getView(position, convertView, parent);

                Track track = this.getItem(position);
                TextView tvTrackNumber = v.findViewById(R.id.tvTrackNumber);
                TextView tvTrackName = v.findViewById(R.id.tvTrackName);
                tvTrackNumber.setText(String.valueOf(position+1));
                tvTrackName.setText(track.getTitle());
//                Log.d("COUNT", "++++++++++" + track.getTitle());
                final ImageButton ibPlay = v.findViewById(R.id.ibPlay);
                final ImageButton ibPause = v.findViewById(R.id.ibPause);
//                ImageView ivCover = v.findViewById(R.id.ivAlbumCoverPL);
//                if(mAlbumsList.get(mAlbumIndex).getCover()!=null){
//                    ivCover.setImageBitmap(mAlbumsList.get(mAlbumIndex).getCover());
//                }
                View.OnClickListener l = new View.OnClickListener() {
                    @Override
                    public void onClick(View clickedView) {

                        Log.d("ON PLAYLIST CLICK", "  position" + position );
//                        ImageButton ibPlay = view.findViewById(R.id.ibPlay);

                        switch (clickedView.getId())
                        {
                            case R.id.ibPlay:
                                currentPlying = v;
                                playMusic(mAlbumIndex,position, v);
                                ibPlay.setVisibility(View.GONE);
                                ibPause.setVisibility(View.VISIBLE);
                                break;
                            case R.id.ibPause:
                                ibPlay.setVisibility(View.VISIBLE);
                                ibPause.setVisibility(View.GONE);
                                currentPlying = null;
                                mMediaPlayer.stop();
                                mMediaPlayer=null;

                                break;
                        }
                    mAdapterPlayList.notifyDataSetInvalidated();
                    }
                };
                ibPlay.setOnClickListener(l);
                ibPause.setOnClickListener(l);
//                v.setTag(track);
                if(v==currentPlying){
                    ibPlay.setVisibility(View.GONE);
                    ibPause.setVisibility(View.VISIBLE);
                }
                else{
                    ibPlay.setVisibility(View.VISIBLE);
                    ibPause.setVisibility(View.GONE);
                }
                return v;

            }
        };
        mlvPlaylist.setAdapter(mAdapterPlayList);
//        mlvPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //todo  create listener
//
//            }
//        });

        ImageView close = layout.findViewById(R.id.ivCloseFragment);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(PlayListFragment.this).commit();
            }
        });
        TextView tvAlbumInfo = layout.findViewById(R.id.tvAlbumInfo);
        Album curAlbum = mAlbumsList.get(mAlbumIndex);
        tvAlbumInfo.setText(curAlbum.getArtist() + "\n" + curAlbum.getName());

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Todo pass link to main activity, to keep playing
        if(mMediaPlayer!=null) {
            mMediaPlayer.stop();
        }
        mMediaPlayer=null;
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void playMusic(int albumIndex, int trackPosition, final View view){
        Uri curTrack = Uri.parse(mAlbumsList.get(albumIndex).getTrackList().get(trackPosition).getTrackFile().getAbsolutePath());
        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            mMediaPlayer=null;
        }
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(view.getContext(), curTrack);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ImageButton ibPlay = currentPlying.findViewById(R.id.ibPlay);
                    ImageButton ibPause = currentPlying.findViewById(R.id.ibPause);
                    ibPlay.setVisibility(View.VISIBLE);
                    ibPause.setVisibility(View.GONE);
                    currentPlying = null;
                    mp = null;

                    mAdapterPlayList.notifyDataSetChanged();
                }
            });
        } catch (IllegalArgumentException e) {
            Log.e("MEDIA PLAYER",  "You might not set the URI correctly!"+ e.getMessage());
        } catch (SecurityException e) {
            Log.e("MEDIA PLAYER", 	"URI cannot be accessed, permissed needed"+ e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("MEDIA PLAYER", 	"Media Player is not in correct state"+ e.getMessage());

        }catch (IOException e){
            Log.e("MEDIA PLAYER", "Can't get media source. " + e.getMessage());
        }

        Log.d("ON PLAYLIST CLICK", "  trackPosition  " + trackPosition );
    }
}
