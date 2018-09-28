package com.example.moon.music_player;

class SongsObject {
    String SongTitle;
    String Artist;
    String Duration;
    String Path;
    String DisplayName;

    public SongsObject(String songTitle, String artist,String path,String displayName,String duration) {
        SongTitle = songTitle;
        Artist = artist;
        Path = path;
        DisplayName = displayName;
        Duration = duration;
    }

    public String getSongTitle() {
        return SongTitle;
    }

    public void setSongTitle(String songTitle) {
        SongTitle = songTitle;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
}
