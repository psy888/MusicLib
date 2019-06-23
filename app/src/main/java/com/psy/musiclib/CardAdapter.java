package com.psy.musiclib;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

    ArrayList<Album> mAlbumsList;
    int mSelrctedView = -9999;
    View.OnClickListener mOnClickListener;
    public CardAdapter(ArrayList<Album> albums) {
        mAlbumsList = albums;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_item,viewGroup,false);
        CardHolder cardHolder = new CardHolder(v);


        return cardHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final CardHolder cardHolder, final int i) {
        cardHolder.cvCard.setId(i);
        Log.d("CLICK", " ID = " + cardHolder.cvCard.getId() + "     Selected " + mSelrctedView);
        /*if(i==mSelrctedView){
            cardHolder.lvPlayList.setVisibility(View.VISIBLE);
            cardHolder.tvAlbumTitle.setVisibility(View.GONE);
            cardHolder.tvAlbumArtist.setVisibility(View.GONE);
            cardHolder.tvAlbumYear.setVisibility(View.GONE);
            cardHolder.ivAlbumCover.setVisibility(View.GONE);
        }
        else{
            cardHolder.lvPlayList.setVisibility(View.GONE);
            cardHolder.tvAlbumTitle.setVisibility(View.VISIBLE);
            cardHolder.tvAlbumArtist.setVisibility(View.VISIBLE);
            cardHolder.tvAlbumYear.setVisibility(View.VISIBLE);
            cardHolder.ivAlbumCover.setVisibility(View.VISIBLE);
        }*/
        int textColor = getTextColor(mAlbumsList.get(i).getVibrantColor());
        //Set Text color based on background
        cardHolder.tvAlbumTitle.setTextColor(textColor);
        cardHolder.tvAlbumArtist.setTextColor(textColor);
        cardHolder.tvAlbumYear.setTextColor(textColor);
        //set Text
        cardHolder.tvAlbumTitle.setText(mAlbumsList.get(i).getName());
        cardHolder.tvAlbumArtist.setText(mAlbumsList.get(i).getArtist());
        cardHolder.tvAlbumYear.setText(mAlbumsList.get(i).getYear());

        cardHolder.cardContainer.setBackgroundColor(mAlbumsList.get(i).getVibrantColor());

        if(mAlbumsList.get(i).getCover()!=null) {

            cardHolder.ivAlbumCover.setImageBitmap(mAlbumsList.get(i).getCover());
            cardHolder.ivAlbumCover.setVisibility(View.VISIBLE);

        }
        else
        {
            cardHolder.ivAlbumCover.setVisibility(View.INVISIBLE);
        }

        if(mOnClickListener!=null){
            cardHolder.cvCard.setOnClickListener(mOnClickListener);
        }
//        Log.d("CARD", " ------- " + cnt++);

//        cardHolder.lvPlayList.setVisibility(View.VISIBLE);

//        if(mOnClickListener!=null){
//            cardHolder.cvCard.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = v.getId();
//                    View nv = LayoutInflater.from(v.getContext()).inflate(R.layout.playlist, null,false);
//                    Fragment playlist = new PlayListFragment();
//                    //toDo get context AND START fragment activity
//                    FragmentManager fm =
//                }
//            });
//        }

        /*

        cardHolder.cvCard.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mSelrctedView=v.getId();
                if(cardHolder.tvAlbumTitle.getVisibility()==View.VISIBLE) {
                    cardHolder.lvPlayList.setVisibility(View.VISIBLE);
                    cardHolder.tvAlbumTitle.setVisibility(View.GONE);
                    cardHolder.tvAlbumArtist.setVisibility(View.GONE);
                    cardHolder.tvAlbumYear.setVisibility(View.GONE);
                    cardHolder.ivAlbumCover.setVisibility(View.GONE);
                    Log.d("CLICK", v.toString());
//                     Album curAlbum = mAlbumsList.get(i);
//                    for (Track t:
//                            curAlbum.getTrackList()) {
//                        Log.d("TRACK", t.getTitle() + " " + t.getYear() + " " + t.getTrackFile().getAbsolutePath());

//            int index = rvCardsList.getChildAdapterPosition(v);
//                CardView playlist = (CardView) LayoutInflater.from(v.getContext()).inflate(R.layout.playlist, (ViewGroup) v.getParent(), false);
//                ListView lvPlaylist = playlist.findViewById(R.id.lvPlaylist);
                    cardHolder.lvPlayList.setAdapter(new ArrayAdapter<Track>(cardHolder.cardContainer.getContext(), R.layout.playlist_item, R.id.tvTrackName, mAlbumsList.get(i).getTrackList()) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            Track track = this.getItem(position);
                            TextView tvTrackNumber = v.findViewById(R.id.tvTrackNumber);
                            TextView tvTrackName = v.findViewById(R.id.tvTrackName);
                            tvTrackNumber.setText(String.valueOf(position+1));
                            tvTrackName.setText(track.getTitle());
                            Log.d("COUNT", "++++++++++" + track.getTitle());
                            v.setTag(track);

                            return v;

                        }
                    });
//                    cardHolder.lvPlayList.setAdapter(new ArrayAdapter<Track>(v.getContext(),android.R.layout.simple_list_item_1,curAlbum.getTrackList()));
//                    cardHolder.cardContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                else{
                    cardHolder.lvPlayList.setVisibility(View.GONE);
                    cardHolder.tvAlbumTitle.setVisibility(View.VISIBLE);
                    cardHolder.tvAlbumArtist.setVisibility(View.VISIBLE);
                    cardHolder.tvAlbumYear.setVisibility(View.VISIBLE);
                    cardHolder.ivAlbumCover.setVisibility(View.VISIBLE);
                    mSelrctedView=-999;
                }
            }

        });
        */

    }
    /*
    public void add(){
        this
    }
*/
//    mOnClickListener = ;
    public Album getItem(int position){
        return mAlbumsList.get(position);
    }

    public void setOnItemClickListener(View.OnClickListener listener){
        mOnClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mAlbumsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * VIEW HOLDER
     */

    public static class CardHolder extends RecyclerView.ViewHolder{

        CardView cvCard;
        TextView tvAlbumTitle;
        TextView tvAlbumArtist;
        TextView tvAlbumYear;
        ImageView ivAlbumCover;
        ListView lvPlayList;

        LinearLayout cardContainer;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            cvCard = itemView.findViewById(R.id.cvCard);
            tvAlbumTitle = itemView.findViewById(R.id.tvAlbumTitle);
            tvAlbumArtist = itemView.findViewById(R.id.tvAlbumArtist);
            tvAlbumYear = itemView.findViewById(R.id.tvAlbumYear);
            ivAlbumCover = itemView.findViewById(R.id.ivAlbumCover);
            cardContainer = itemView.findViewById(R.id.cardContainer);
            lvPlayList = itemView.findViewById(R.id.lvPlaylist);
        }


    }

    //------------------------------------------------------------------------------
    int getTextColor(int bgColor){
        /**
         * Y = (299*R + 587*G + 114*B)/1000
         */
        Log.d("TEXT COLOR", "-------------- bgColor IN" + bgColor);
        double darkness = 1-(0.299* Color.red(bgColor) + 0.587*Color.green(bgColor) + 0.114*Color.blue(bgColor))/255;
        Log.d("TEXT COLOR", "--------------" + darkness);
        if(darkness<0.5){// It's a light bg color
            return Color.rgb(32,32,32); //dark text color
        }else{// It's a dark bg color
            return Color.rgb(224,224,224); //light text color
        }
    }



}
