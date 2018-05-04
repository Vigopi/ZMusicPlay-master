package com.example.vinothgopigraj.zmusicplay;

import android.app.usage.ExternalStorageStats;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String []items;
    ArrayList<File> mySongs;
    ArrayList<String> songs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        File f = new File("/sdcard/");
        //File f=Environment.getExternalStorageDirectory();

        System.out.println(f.getAbsolutePath());
        //String path = f.getPath()+"/storage/sdcard1";
        //File file = new File(path);
       // Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
        //f = new File(file.getAbsolutePath());

       // f= new File(Environment.getRootDirectory().getParent());
        //Toast.makeText(getApplicationContext(), f.getName(), Toast.LENGTH_SHORT).show();
        mySongs = findsongs(f);
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            items[i] = mySongs.get(i).getName().toString();
        }
        getMusic();
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);
        listView.setAdapter(adp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",i).putExtra("songList",mySongs));
            }
        });
    }

    private ArrayList<File> findsongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        if(root.isDirectory())
        {
            File []files = root.listFiles();
            for(File file:files)
            {
                if(file.isDirectory() && !file.isHidden())
                {
                    al.addAll(findsongs(file));
                }
                else
                {
                    if(file.getName().endsWith(".mp3"))
                    {
                        al.add(file);
                    }
                }
            }
            return al;
        }
        else
        {
            al.add(root);
            return al;
        }

    }
    public void getMusic()
    {
        songs = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songcursor = contentResolver.query(uri,null,null,null,null);
        if(songcursor!=null && songcursor.moveToFirst())
        {
            do {
                int songtitle = songcursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                String song = songcursor.getString(songtitle);

                songs.add(song);
            }
            while(songcursor.moveToNext());


        }
    }
}
