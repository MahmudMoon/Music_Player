package com.example.moon.music_player;

import android.media.MediaPlayer;

public class StopErrorOfList {
   public static MediaPlayer mediaPlayer;
   public static void setValueToMediaPlayer(MediaPlayer P_mediaPlayer){
       mediaPlayer = P_mediaPlayer;
   }

    public static MediaPlayer getValueToMediaPlayer(){
       return mediaPlayer;
    }
}
