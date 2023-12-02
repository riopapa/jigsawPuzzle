package com.riopapa.jigsawpuzzle.model;

public class History {
    public String game;      // image code a00 for R.mipmap.a00_alberta
    public int latest;      // latest level handled;
    public long []time;      // last GVal done

    public int [] locked;   // pieces locked
    public int [] percent;    // completed percent
    public History() {
        latest = -1;
        time = new long[4];
        locked = new int [4];
        percent = new int[4];
    }
}
