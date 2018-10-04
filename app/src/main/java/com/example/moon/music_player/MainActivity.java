package com.example.moon.music_player;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.sql.Types.NULL;

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
    LinearLayout linearLayout;
    Cursor cursor;
    ArrayList<SongsObject> SongsDetails;
    ArrayList<SongsObject> finalSelectionOfSongs;
    TextView songTitle,songArtist,songDuration;
    Thread thread1 = null;


    int[] back = {R.drawable.mba,R.drawable.mba1,R.drawable.mba2,R.drawable.mba3,R.drawable.mba4,R.drawable.mback1};


  //  int[] songs = {R.raw.song1,R.raw.song2,R.raw.song3,R.raw.song4,R.raw.song5};
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (ImageButton)findViewById(R.id.imageButton);
        previous = (ImageButton)findViewById(R.id.imageButton3);
        next = (ImageButton)findViewById(R.id.imageButton2);
        v_up = (ImageButton)findViewById(R.id.button2);
        v_down = (ImageButton)findViewById(R.id.button);
        shuffle = (ImageButton)findViewById(R.id.imageButton4);
        playlist = (ImageButton)findViewById(R.id.imageButton5);
        linearLayout = (LinearLayout) findViewById(R.id.backgroud);
        songTitle = (TextView)findViewById(R.id.textView);
        songArtist = (TextView)findViewById(R.id.textView2);
        songDuration = (TextView)findViewById(R.id.textView3);


        SongsDetails = new ArrayList<>();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for(int i= 0;i<back.length;i++) {

                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayout.setBackgroundResource(back[finalI]);
                        }
                    });

                     SystemClock.sleep(1000*25);
                    if(i==back.length)
                        i=0;
                }
            }
        };

        final Thread thread = new Thread(runnable);
        thread.start();






      // songs = loadSongs();

//        shuffle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),SongsDetails.get(0).getPath(),Toast.LENGTH_SHORT).show();
//
//                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(SongsDetails.get(0).getPath()));
//                mediaPlayer.start();
//            }
//        });

         finalSelectionOfSongs = loadSongs();

       song_names  =  new String[finalSelectionOfSongs.size()];

       for(int i=0;i<finalSelectionOfSongs.size();i++)
       {
           String demo = finalSelectionOfSongs.get(i).getDisplayName();
           song_names[i] = demo;
       }



          Intent intent = getIntent();
         pos = intent.getIntExtra("position",-1);

         if(pos>=0){
             position = pos;

             MediaPlayer valueToMediaPlayer = StopErrorOfList.getValueToMediaPlayer();
             if(valueToMediaPlayer!=null){
                 valueToMediaPlayer.pause();
                 Toast.makeText(getApplicationContext(),"Playing pause",Toast.LENGTH_SHORT).show();
             }

             mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(finalSelectionOfSongs.get(position).getPath()));
             songTitle.setText("Song Title: " + finalSelectionOfSongs.get(position).getDisplayName());
             songArtist.setText("Artist: " +finalSelectionOfSongs.get(position).getArtist());
             int duration = Integer.valueOf(finalSelectionOfSongs.get(position).getDuration());
             int min = duration/(1000*60);
             int sec = duration%(60);
             songDuration.setText("Duration: " + min+":" + sec);

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
                    if(mediaPlayer==null) {

                        if(SavedValue.isShuffelOn()) {
                            Random random = new Random();
                            position = random.nextInt(finalSelectionOfSongs.size());
                        }

                        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(finalSelectionOfSongs.get(position).getPath()));
                    }

                    songTitle.setText("Song Title: " + finalSelectionOfSongs.get(position).getDisplayName());
                    songArtist.setText("Artist: " +finalSelectionOfSongs.get(position).getArtist());
                    int duration = Integer.valueOf(finalSelectionOfSongs.get(position).getDuration());
                    int min = duration/(1000*60);
                    int sec = duration%(1000*60);
                    songDuration.setText("Duration: " + min+":" + sec);
                    mediaPlayer.start();


                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Toast.makeText(getApplicationContext(),"Song Completed",Toast.LENGTH_SHORT).show();

                            if(SavedValue.isShuffelOn()){
                                Random random = new Random();
                                position = random.nextInt(finalSelectionOfSongs.size());
                            }else
                               position++;


                            if(position>=finalSelectionOfSongs.size()){
                                position = 0;
                            }

                            mp.stop();


                            songTitle.setText("Song Title: " + finalSelectionOfSongs.get(position).getDisplayName());
                            songArtist.setText("Artist: " +finalSelectionOfSongs.get(position).getArtist());
                            int duration = Integer.valueOf(finalSelectionOfSongs.get(position).getDuration());
                            int min = duration/(1000*60);
                            int sec = duration%(1000*60);
                            songDuration.setText("Duration: " + min+":" + sec);
                            mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(finalSelectionOfSongs.get(position).getPath()));
                            mediaPlayer.start();
                        }
                    });



                }
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SavedValue.isShuffelOn()){
                    Random random = new Random();
                    position = random.nextInt(finalSelectionOfSongs.size());
                }else
                    position++;

                if(position>=finalSelectionOfSongs.size()){
                    position = 0;
                }

                //SavedValue.setValue(position);

                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(finalSelectionOfSongs.get(position).getPath()));

                songTitle.setText("Song Title: " + finalSelectionOfSongs.get(position).getDisplayName());
                songArtist.setText("Artist: " +finalSelectionOfSongs.get(position).getArtist());
                int duration = Integer.valueOf(finalSelectionOfSongs.get(position).getDuration());
                int min = duration/(1000*60);
                int sec = duration%(1000*60);
                songDuration.setText("Duration: " + min+":" + sec);
                mediaPlayer.start();


                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Toast.makeText(getApplicationContext(),"Song Completed",Toast.LENGTH_SHORT).show();

                        if(SavedValue.isShuffelOn()){
                            Random random = new Random();
                            position = random.nextInt(finalSelectionOfSongs.size());
                        }else
                            position++;

                        if(position>=finalSelectionOfSongs.size()){
                            position = 0;
                        }

                        mp.stop();

                        songTitle.setText("Song Title: " + finalSelectionOfSongs.get(position).getDisplayName());
                        songArtist.setText("Artist: " +finalSelectionOfSongs.get(position).getArtist());
                        int duration = Integer.valueOf(finalSelectionOfSongs.get(position).getDuration());
                        int min = duration/(1000*60);
                        int sec = duration%(1000*60);
                        songDuration.setText("Duration: " + min+":" + sec);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(finalSelectionOfSongs.get(position).getPath()));
                        mediaPlayer.start();
                    }
                });






            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SavedValue.isShuffelOn()){
                    Random random = new Random();
                    position = random.nextInt(finalSelectionOfSongs.size());
                }else
                    position--;

                if(position<0){
                    position = finalSelectionOfSongs.size()-1;
                }
                if(mediaPlayer.isPlaying())
                    mediaPlayer.stop();

                mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(finalSelectionOfSongs.get(position).getPath()));

                songTitle.setText("Song Title: " + finalSelectionOfSongs.get(position).getDisplayName());
                songArtist.setText("Artist: " +finalSelectionOfSongs.get(position).getArtist());
                int duration = Integer.valueOf(finalSelectionOfSongs.get(position).getDuration());
                int min = duration/(1000*60);
                int sec = duration%(1000*60);
                songDuration.setText("Duration: " + min+":" + sec);
                mediaPlayer.start();


                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Toast.makeText(getApplicationContext(),"Song Completed",Toast.LENGTH_SHORT).show();

                        if(SavedValue.isShuffelOn()){
                            Random random = new Random();
                            position = random.nextInt(finalSelectionOfSongs.size());
                        }else
                            position--;

                        if(position>=finalSelectionOfSongs.size()){
                            position = 0;
                        }

                        mp.stop();


                        songTitle.setText("Song Title: " + finalSelectionOfSongs.get(position).getDisplayName());
                        songArtist.setText("Artist: " +finalSelectionOfSongs.get(position).getArtist());
                        int duration = Integer.valueOf(finalSelectionOfSongs.get(position).getDuration());
                        int min = duration/(1000*60);
                        int sec = duration%(1000*60);
                        songDuration.setText("Duration: " + min+":" + sec);

                        mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(finalSelectionOfSongs.get(position).getPath()));
                        mediaPlayer.start();
                    }
                });


            }
        });


        v_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currVolume-=5;
                float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
                mediaPlayer.setVolume(1-log1,1-log1);
                //changeIcon("down");
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
                StopErrorOfList.setValueToMediaPlayer(mediaPlayer);
                Intent intent = new Intent(MainActivity.this,songsList.class);
                intent.putExtra("songs",song_names);
                startActivity(intent);
            }
        });


        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SavedValue.isShuffelOn()) {
                    SavedValue.setShuffelOn(false);
                    shuffle.setImageResource(R.drawable.shuffel_green);
                }else
                    SavedValue.setShuffelOn(true);
                    shuffle.setImageResource(R.drawable.shuffel_black);

            }
        });



    }

//    private void changeIcon(final String icon) {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(icon.equals("up"))
//                    volume_.setImageResource(R.drawable.speaker);
//                else
//                    volume_.setImageResource(R.drawable.down);
//
//                SystemClock.sleep(300);
//                volume_.setImageResource(NULL);
//            }
//        },100);
//    }


    private ArrayList<SongsObject> loadSongs() {



        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

       // List<String> songs = new ArrayList<String>();
        while(cursor.moveToNext()) {
            SongsObject songsObject = new SongsObject(cursor.getString(1),cursor.getString(2)
                                                     ,cursor.getString(3),cursor.getString(4),cursor.getString(5));

            SongsDetails.add(songsObject);


//            songs.add(cursor.getString(0) + "||"
//                    + cursor.getString(1) + "||"
//                    + cursor.getString(2) + "||"
//                    + cursor.getString(3) + "||"
//                    + cursor.getString(4) + "||"
//                    + cursor.getString(5));
        }


        for(int i =0;i<SongsDetails.size();i++){
            Log.i("SongDetail",
                    SongsDetails.get(i).getSongTitle()+
                    "\n"+SongsDetails.get(i).getArtist()+
                    "\n"+SongsDetails.get(i).getPath()+
                    "\n"+SongsDetails.get(i).getDisplayName()+
                    "\n"+SongsDetails.get(i).getDuration());
        }

        return SongsDetails;

















//        String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Audio";
//        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//            //Log.i("FolderExists","FolderExists");
//               File file = new File(root);
//               File[] songs_collection = file.listFiles();
//               return songs_collection;
//
//        }else
//            Log.i("FolderExists","FolderNotExists");
//        return null;


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
//
//
//
// }

    private class Myrunnable implements Runnable{


        MediaPlayer Player;
        boolean isCompleted ;
        public Myrunnable(MediaPlayer mediaPlayer) {
            Player = mediaPlayer;
            isCompleted = false;
        }

        @Override
        public void run() {
            while (!thread1.isInterrupted()) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if(isCompleted) {

                            Toast.makeText(getApplicationContext(), "song completed ", Toast.LENGTH_SHORT).show();

                            position++;
                            SavedValue.setValue(position);
                            if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }


                            if(position<finalSelectionOfSongs.size()) {
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(finalSelectionOfSongs.get(position).getPath()));
                                mediaPlayer.start();

                            }
                            isCompleted = false;
                            Toast.makeText(getApplicationContext(), "Auto move to the next song ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                if(mediaPlayer.getCurrentPosition()>=mediaPlayer.getDuration()){
                    isCompleted = true;
                }


                if(thread1.isInterrupted()){
                    Log.i("Interrupted","Thread Interrupted");
                      break;
                }

                SystemClock.sleep(1000);
            }


        }
    }
}



