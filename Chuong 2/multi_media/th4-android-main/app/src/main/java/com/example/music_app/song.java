package com.example.music_app;

public class song {
    private String title;
    private int File;

    public song(String title, int file) {
        this.title = title;
        File = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFile() {
        return File;
    }

    public void setFile(int file) {
        File = file;
    }
}
