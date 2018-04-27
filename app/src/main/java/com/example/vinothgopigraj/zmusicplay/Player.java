package com.example.vinothgopigraj.zmusicplay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {

    static MediaPlayer mp;
    Button btnPlay,btnNext,btnPrev,btnF,btnB,btnStop;
    SeekBar seekBar;
    int position;
    TextView textTitle;
    TextView textStatus,textLength;
    Thread updateSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        final ArrayList<File> mySongs = (ArrayList) b.getParcelableArrayList("songList");
        position = b.getInt("pos",0);

        textTitle = (TextView)findViewById(R.id.textTitle);
        textTitle.setText(mySongs.get(position).getName());
        textStatus = (TextView)findViewById(R.id.textStatus);
        textStatus.setText("Playing ...");
        textLength = (TextView)findViewById(R.id.textLength);

        btnPlay = (Button)findViewById(R.id.buttonPlay);
        btnPrev = (Button)findViewById(R.id.buttonPrevious);
        btnNext = (Button)findViewById(R.id.buttonNext);
        btnF = (Button)findViewById(R.id.buttonNext5);
        btnB = (Button)findViewById(R.id.buttonPrev5);
        btnStop = (Button)findViewById(R.id.buttonStop);
       // btnPlay.setText("Pause");

        if(mp!=null)
        {
            mp.stop();
            mp.release();
        }

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp!=null)
                {
                    textStatus.setText("Stopped");
                    //btnPlay.setText("Play");
                    mp.pause();
                    mp.seekTo(0);
                    seekBar.setProgress(0);
                    btnPlay.setBackgroundResource(R.drawable.play);

                }
            }
        });
        seekBar = (SeekBar)findViewById(R.id.seekBar);


        updateSeekBar = new Thread()
        {
            @Override
            public void run() {
                super.run();
                int totalDuration = mp.getDuration();
                int currentPosition=0;
                seekBar.setMax(totalDuration);
                while (currentPosition<totalDuration)
                {
                    try {
                        sleep(1000);
                        currentPosition=mp.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        Uri uri = Uri.parse(mySongs.get(position).toString());
       // MediaStore m =  MediaStore.Audio
         //       mySongs.get(position);
        mp = MediaPlayer.create(getApplicationContext(),uri);
        Toast.makeText(this, "Duration is "+mp.getDuration(), Toast.LENGTH_SHORT).show();
        long len =mp.getDuration();
        long min = TimeUnit.MILLISECONDS.toMinutes(len);
        long sec = TimeUnit.MILLISECONDS.toSeconds(len);
        textLength.setText(min+":"+sec);
        mp.start();
        updateSeekBar.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()) {
                    mp.pause();
                    textStatus.setText("Paused");
                   // btnPlay.setText("Play");
                    btnPlay.setBackgroundResource(R.drawable.play);
                }
                else
                {
                    mp.start();
                    textStatus.setText("Playing ...");
                   // btnPlay.setText("Pause");
                    btnPlay.setBackgroundResource(R.drawable.pause);
                }

            }
        });
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.seekTo(mp.getCurrentPosition()+5000);
            }
        });
        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.seekTo(mp.getCurrentPosition()-5000);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mp!=null) {

                    position = (position + 1) % mySongs.size();
                    mp.stop();

                    mp.release();
                    Uri u = Uri.parse(mySongs.get(position).toString());
                    textTitle.setText(mySongs.get(position).getName());
                    mp = MediaPlayer.create(getApplicationContext(), u);
                    seekBar.setProgress(0);
                    long len = mp.getDuration();
                    long min = TimeUnit.MILLISECONDS.toMinutes(len);
                    long sec = TimeUnit.MILLISECONDS.toSeconds(len);
                    textLength.setText(min + ":" + sec);
                    mp.start();
                    
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp != null) {
                    if (position == 0)
                        position = mySongs.size() - 1;
                    else
                        position = (position - 1);
                    mp.stop();
                    mp.release();
                    Uri u = Uri.parse(mySongs.get(position).toString());
                    textTitle.setText(mySongs.get(position).getName());
                    mp = MediaPlayer.create(getApplicationContext(), u);
                    seekBar.setProgress(0);
                    long len = mp.getDuration();
                    long min = TimeUnit.MILLISECONDS.toMinutes(len);
                    long sec = TimeUnit.MILLISECONDS.toSeconds(len);
                    textLength.setText(min + ":" + sec);
                    mp.start();
                }
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnNext.performClick();
            }
        });
    }

}
