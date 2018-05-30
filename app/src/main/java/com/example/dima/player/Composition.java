package com.example.dima.player;

/**
 * Created by Dima on 5/15/2018.
 */

public class Composition {
    private String title;
    private String author;
    private int icon;
    private int song;
    private int imageGroup;


    public Composition(String title, String author, int icon, int song, int imageGroup) {
        this.title = title;
        this.author = author;
        this.icon = icon;
        this.song = song;
        this.imageGroup = imageGroup;
    }

    public int getSong() {
        return song;
    }

    public int getImageGroup() {
        return imageGroup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
