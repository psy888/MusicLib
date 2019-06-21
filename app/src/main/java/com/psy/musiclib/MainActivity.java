package com.psy.musiclib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String BASE = "media.base";
    private final static String TAG = "----" + MainActivity.class.getName();
    private final static String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    protected static ArrayList<Album> mAlbumsList;
    protected static ArrayList<Track> mTrackList;
    protected static ArrayList<Album> mSearchResult;
    Toolbar mToolbar;
    protected  RecyclerView rvCardsList;
    ArrayList<Track> mTracks;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(PERMISSIONS, 88);
/*
        if (savedInstanceState == null) {
            try {
                FileInputStream fis = openFileInput(BASE);
                ObjectInputStream ois = new ObjectInputStream(fis);
                mAlbumsList = (ArrayList<Album>) ois.readObject();
                ois.close();
            } catch (IOException e) {
                Log.e(TAG, "Can't open base");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Can't read base");
                e.printStackTrace();
            } finally {
                if (mAlbumsList == null) {
                    File base = new File(BASE);
                    try {
                        base.createNewFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    mAlbumsList = new ArrayList<>();
                }
            }
        }
*/
mAlbumsList = new ArrayList<>();
mTrackList= new ArrayList<>();
//        onRequestPermissionsResult(88, PERMISSIONS, new int[]{PackageManager.PERMISSION_GRANTED,PackageManager.PERMISSION_GRANTED});
        if (mAlbumsList == null | mAlbumsList.size() < 1) {
            File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            for (File f :
                    musicDir.listFiles()) {
                Log.d(TAG, f.getName());
                if (f.getName().substring(f.getName().lastIndexOf(".") + 1).contentEquals("mp3")) {
                    Log.d(TAG, "ADDED " + f.getName());
                    new Track(f);
                }
            }

        }
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.findViewById(R.id.ibSearch).setOnClickListener(toolbarListener);
        mToolbar.findViewById(R.id.ibBack).setOnClickListener(toolbarListener);
        EditText etSearch = mToolbar.findViewById(R.id.etSearch);
        /**
         * on ENTER KEY listener
         */
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    search();
//                    hideKeyboardFrom(MainActivity.this);
                    mToolbar.findViewById(R.id.ibSearch).callOnClick();
                    return true;

                }

                return false;
            }
        });


        rvCardsList = findViewById(R.id.rvCardsList);
//        rvCardsList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        rvCardsList.setLayoutManager(llm);
        rvCardsList.setAdapter(new CardAdapter(mAlbumsList));






        /*
        Log.d(TAG,"ALBUMS CNT " + mAlbumsList.size());
        TextView tv = findViewById(R.id.output);
        tv.setText(mAlbumsList.get(0).getName());
        ImageView iv = findViewById(R.id.img);
        iv.setImageBitmap(mAlbumsList.get(0).getCover());
        */
       /* for (Album a:
             mAlbumsList) {
            tv.append(a.toString() + "\n");
        }*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        try {
            FileOutputStream fos = openFileOutput(BASE, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mAlbumsList);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Can't write base");
            e.printStackTrace();
        }*/
    }

    View.OnClickListener toolbarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ibSearch:
                    search();
                    hideKeyboardFrom(MainActivity.this);
                    break;
                case R.id.ibBack:
                    toggleToolbarState();
                    rvCardsList.setAdapter(new CardAdapter(mAlbumsList));
                    break;
            }
        }
    };

    public void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(MainActivity.this.findViewById(R.id.mainContainer).getWindowToken(), 0);
    }

    void search() {
        EditText etSearch = mToolbar.findViewById(R.id.etSearch);
        String query = etSearch.getText().toString();
        searchInAlbums(query);
        if (mSearchResult.size() > 0) {
            rvCardsList.setAdapter(new CardAdapter(mSearchResult));
            toggleToolbarState();
        }

    }

    void toggleToolbarState() {
        if (mToolbar.findViewById(R.id.etSearch).getVisibility() == View.VISIBLE) {
            mToolbar.findViewById(R.id.etSearch).setVisibility(View.GONE);
            mToolbar.findViewById(R.id.ibSearch).setVisibility(View.GONE);
            mToolbar.findViewById(R.id.ibBack).setVisibility(View.VISIBLE);
        } else {
            mToolbar.findViewById(R.id.etSearch).setVisibility(View.VISIBLE);
            mToolbar.findViewById(R.id.ibSearch).setVisibility(View.VISIBLE);
            mToolbar.findViewById(R.id.ibBack).setVisibility(View.GONE);
            ((TextView) mToolbar.findViewById(R.id.etSearch)).setText("");
        }
    }

    void searchInAlbums(String query) {
        //clear previous results
        mSearchResult = new ArrayList<>();
        query = query.trim().toLowerCase();
        //search
        for (Album album :
                mAlbumsList) {
            boolean name = (album.getName() != null) && album.getName().toLowerCase().contains(query);
            boolean year = (album.getYear() != null) && album.getYear().toLowerCase().contentEquals(query);
            boolean artist = (album.getArtist() != null) && album.getArtist().toLowerCase().contains(query);
            if (name | year | artist) {
                mSearchResult.add(album);
            }
        }
    }

}

