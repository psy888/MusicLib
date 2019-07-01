package com.psy.musiclib;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback {
    private final static String BASE = "media.base";
    private final static int PERMISSION_REQUEST_ID =88;

    private final static String TAG = "----" + MainActivity.class.getName();
    private final static String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//    private final static int[] PERMISSIONS_RESULT = new int[]{PermissionChecker.PERMISSION_GRANTED,PermissionChecker.PERMISSION_GRANTED};
    protected static ArrayList<Album> mAlbumsList;
    protected static ArrayList<Track> mTrackList;
    protected static ArrayList<Album> mSearchResult;
    Toolbar mToolbar;

    protected  RecyclerView rvCardsList;
    protected CardAdapter mCardAdapter;
    ArrayList<Track> mTracks;
    DrawerLayout mDrawerLayout;
    static final String KEY_ALBUMS = "albums";
    static final String KEY_TRACKS = "tracks";


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_REQUEST_ID)
        {
            if(permissions[0].contentEquals(PERMISSIONS[0]) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    permissions[1].contentEquals(Manifest.permission.READ_EXTERNAL_STORAGE)&&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                if(mAlbumsList==null) {

                    scanFiles();
                }
            }
            else{
                this.finish();
            }
        }

    }

    private void scanFiles() {
        if (mAlbumsList == null) {
            mAlbumsList = new ArrayList<>();
            mTrackList= new ArrayList<>();

            File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

//            File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            for (File f :
                    musicDir.listFiles()) {
                Log.d(TAG, f.getName());
                if (f.getName().substring(f.getName().lastIndexOf(".") + 1).contentEquals("mp3")) {
                    Log.d(TAG, "ADDED " + f.getName());
                    new Track(f);
                }
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        onRequestPermissionsResult(PERMISSION_REQUEST_ID,PERMISSIONS,PERMISSIONS_RESULT);
//        checkSelfPermission(PERMISSIONS[0]);


        if (savedInstanceState == null) {
            try {
                FileInputStream fis = openFileInput(BASE);
                ObjectInputStream ois = new ObjectInputStream(fis);
                mAlbumsList = (ArrayList<Album>) ois.readObject();
                mTrackList = (ArrayList<Track>) ois.readObject();
                ois.close();
                Log.e(TAG, "Readed");
            } catch (IOException e) {
                Log.e(TAG, "Can't open base" + e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Can't read base" + e.getMessage());
                e.printStackTrace();
            }
        }else{
            mAlbumsList = (ArrayList<Album>) savedInstanceState.getSerializable(KEY_ALBUMS);
            mTrackList = (ArrayList<Track>) savedInstanceState.getSerializable(KEY_TRACKS);
        }
        requestPermissions(PERMISSIONS, PERMISSION_REQUEST_ID);

            if (mAlbumsList == null) {
                File base = new File(BASE);
                try {
                    base.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                scanFiles();

            }




//mAlbumsList = new ArrayList<>();
//        onRequestPermissionsResult(88, PERMISSIONS, new int[]{PackageManager.PERMISSION_GRANTED,PackageManager.PERMISSION_GRANTED});

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.findViewById(R.id.ibSearch).setOnClickListener(toolbarListener);
        mToolbar.findViewById(R.id.ibBack).setOnClickListener(toolbarListener);
        mDrawerLayout = findViewById(R.id.mainContainer);
        EditText etSearch = mToolbar.findViewById(R.id.etSearch);
        final NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId()){
//                    case R.id.one:
                mAlbumsList = null;
                mTrackList = null;
                        reloadBase();
                        mDrawerLayout.closeDrawer(Gravity.START);

                Log.e(TAG, "reloadBase-----------------------------");
//                        break;
//                }
                return true;
            }
        });
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
        etSearch.clearFocus();



        rvCardsList = findViewById(R.id.rvCardsList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            rvCardsList.setFocusedByDefault(true);
        }
//        rvCardsList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        rvCardsList.setLayoutManager(llm);
        mCardAdapter = new CardAdapter(mAlbumsList);
        mCardAdapter.setOnItemClickListener(listener);
        rvCardsList.setAdapter(mCardAdapter);







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

    private void reloadBase() {
        mAlbumsList = null;
        mTrackList = null;
        scanFiles();
//        mCardAdapter.notifyDataSetChanged();
        mCardAdapter = new CardAdapter(mAlbumsList);
        rvCardsList.setAdapter( mCardAdapter);
        mCardAdapter.setOnItemClickListener(listener);
        Log.d("TAG", "RELOADED");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //todo save album and track list
        outState.putSerializable(KEY_ALBUMS, mAlbumsList);
        outState.putSerializable(KEY_TRACKS, mTrackList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            FileOutputStream fos = openFileOutput(BASE, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mAlbumsList);
            oos.writeObject(mTrackList);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Can't write base");
            e.printStackTrace();
        }
    }
    /**
     * on card click listener
     */
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = v.getId();
    //                View nv = LayoutInflater.from(v.getContext()).inflate(R.layout.playlist, null,false);
            Fragment playlistFragment = PlayListFragment.newInstance(v.getId());

            //toDo get context AND START fragment activity
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.containerPlaylist ,playlistFragment, String.valueOf(v.getId())).commit();
            Log.d("CLICK LIST", "CLICKED on " + position);

        }
    };

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
                    rvCardsList.setAdapter(mCardAdapter);
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
            CardAdapter searchAdapter = new CardAdapter(mSearchResult);
            searchAdapter.setOnItemClickListener(listener);
            rvCardsList.setAdapter(searchAdapter);

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

