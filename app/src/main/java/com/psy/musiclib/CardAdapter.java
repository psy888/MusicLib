package com.psy.musiclib;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

    int cnt=0;
    ArrayList<Album> mAlbumsList;
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
    public void onBindViewHolder(@NonNull CardHolder cardHolder, int i) {
        int textColor = getTextColor(mAlbumsList.get(i).getVibrantColor());
        //Set Text color based on background
        cardHolder.tvAlbumTitle.setTextColor(textColor);
        cardHolder.tvAlbumArtist.setTextColor(textColor);
        cardHolder.tvAlbumYear.setTextColor(textColor);
        //set Text
        cardHolder.tvAlbumTitle.setText(mAlbumsList.get(i).getName());
        cardHolder.tvAlbumArtist.setText(mAlbumsList.get(i).getArtist());
        cardHolder.tvAlbumYear.setText(mAlbumsList.get(i).getYear());
        Bitmap cover = mAlbumsList.get(i).getCover();

        if(cover!=null) {

            cardHolder.ivAlbumCover.setImageBitmap(cover);
            cardHolder.ivAlbumCover.setVisibility(View.VISIBLE);

        }
        else
        {
            cardHolder.ivAlbumCover.setVisibility(View.GONE);
        }
        cardHolder.cardContainer.setBackgroundColor(mAlbumsList.get(i).getVibrantColor());
//        Log.d("CARD", " ------- " + cnt++);
    }


    @Override
    public int getItemCount() {
        return mAlbumsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
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

        LinearLayout cardContainer;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            cvCard = itemView.findViewById(R.id.cvCard);
            tvAlbumTitle = itemView.findViewById(R.id.tvAlbumTitle);
            tvAlbumArtist = itemView.findViewById(R.id.tvAlbumArtist);
            tvAlbumYear = itemView.findViewById(R.id.tvAlbumYear);
            ivAlbumCover = itemView.findViewById(R.id.ivAlbumCover);
            cardContainer = itemView.findViewById(R.id.cardContainer);
        }
    }

    int getTextColor(int bgColor){
        /**
         * Y = (299*R + 587*G + 114*B)/1000
         */

        double darkness = 1-(0.299* Color.red(bgColor) + 0.587*Color.green(bgColor) + 0.114*Color.blue(bgColor))/255;
        Log.d("TEXT COLOR", "--------------" + darkness);
        if(darkness<0.5){// It's a light bg color
            return Color.rgb(32,32,32); //dark text color
        }else{// It's a dark bg color
            return Color.rgb(224,224,224); //light text color
        }
    }
}
