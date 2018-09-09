package com.example.moon.music_player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    int position =0;
    MediaPlayer mediaPlayer;
    ImageButton play,previous,next;
    ImageButton v_up,v_down,playlist,shuffle,repeat;
    File[] songs;
    int counter = 0;
    int maxVolume = 50;
    int currVolume = 25;
    String[] song_names;
    int pos = 0;


  //  int[] songs = {R.raw.song1,R.raw.song2,R.raw.song3,R.raw.song4,R.raw.song5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (ImageButton)findViewById(R.id.imageButton);
        previous = (ImageButton)findViewById(R.id.imageButton2);
        next = (ImageButton)findViewById(R.id.imageButton3);
        v_up = (ImageButton)findViewById(R.id.button2);
        v_down = (ImageButton)findViewById(R.id.button);
        shuffle = (ImageButton)findViewById(R.id.imageButton4);
        repeat = (ImageButton)findViewById(R.id.imageButton6);
        playlist = (ImageButton)findViewById(R.id.imageButton5);



       songs = loadSongs();
       song_names  =  new String[songs.length];

       for(int i=0;i<songs.length;i++)
       {
           String demo = songs[i].getName();
           song_names[i] = demo;
       }


          Intent intent = getIntent();
         pos = intent.getIntExtra("position",-1);

         if(pos>=0){
             position = pos;
             mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(songs[position].getAbsolutePath()));
             mediaPlayer.start();
             counter=1;
             play.setImageResource(R.drawable.pause);
             play.setScaleType(ImageView.ScaleType.FIT_CENTER);

         }


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if(counter%2==0){
                    if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        play.setImageResource(R.drawable.play);
                        play.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                }else {
                    play.setImageResource(R.drawable.pause);
                    play.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    if(mediaPlayer==null)
                        mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(songs[position].getAbsolutePath()));

                   mediaPlayer.start();
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position>=songs.length){
                    position = 0;
                }
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(songs[position].getAbsolutePath()));
                mediaPlayer.start();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position<0){
                    position = songs.length-1;
                }
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();

                mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(songs[position].getAbsolutePath()));
                mediaPlayer.start();
            }
        });


        v_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currVolume-=5;
                float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
                mediaPlayer.setVolume(1-log1,1-log1);
            }
        });

        v_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currVolume+=5;
                float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
                mediaPlayer.setVolume(1-log1,1-log1);
            }
        });

        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,songsList.class);
                intent.putExtra("songs",song_names);
                startActivity(intent);
            }
        });
    }


    private File[] loadSongs() {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Audio";
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //Log.i("FolderExists","FolderExists");
               File file = new File(root);
               File[] songs_collection = file.listFiles();
               return songs_collection;

        }else
            Log.i("FolderExists","FolderNotExists");
        return null;


    }


    //for volume operations


    //int maxVolume = 50;
    //float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
    //yourMediaPlayer.setVolume(1-log1);

//
//    String path;
//    File sdCardRoot = Environment.getExternalStorageDirectory();
//    File dir = new File(sdCardRoot.getAbsolutePath() + "/yourDirectory/");
//
//if (dir.exists()) {
//
//        if (dir.listFiles() != null) {
//            for (File f : dir.listFiles()) {
//                if (f.isFile())
//                    path = f.getName();
//
//                if (path.contains(".mp3")) {
//                    yourArrayList.add(path);
//
//                }
//            }
//        }
//    }
}
