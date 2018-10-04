package com.example.moon.music_player;

public class SavedValue {
    public static int postiton = -1;
    public static boolean shuffelOn  = false;

    public static boolean isShuffelOn() {
        return shuffelOn;
    }

    public static void setShuffelOn(boolean shuffelOn) {
        SavedValue.shuffelOn = shuffelOn;
    }

    public static void setValue(int pos){
        SavedValue.postiton = pos;
    }


    public static int getValue(){
       return postiton;
    }
}
