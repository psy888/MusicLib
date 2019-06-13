package com.psy.musiclib;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.webkit.PermissionRequest;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Permission;
import java.security.Permissions;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String BASE = "media.base";
    private final static String TAG = "----"+MainActivity.class.getName();
    private final static String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    protected static ArrayList<Album> mAlbumsList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(PERMISSIONS, 88);

        if(savedInstanceState==null){
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
            }
            finally {
                if(mAlbumsList==null)
                {
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

//        onRequestPermissionsResult(88, PERMISSIONS, new int[]{PackageManager.PERMISSION_GRANTED,PackageManager.PERMISSION_GRANTED});

        File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        for (File f:
             musicDir.listFiles()) {
            Log.d(TAG,f.getName());
            if(f.getName().substring(f.getName().lastIndexOf(".")+1).contentEquals("mp3")){
                Log.d(TAG,"ADDED " + f.getName());
                new Track(f);
            }
        }

        RecyclerView rvCardsList = findViewById(R.id.rvCardsList);
        rvCardsList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
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
        }
    }
}