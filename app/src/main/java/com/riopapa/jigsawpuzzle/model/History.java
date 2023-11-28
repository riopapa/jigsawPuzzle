package com.riopapa.jigsawpuzzle.model;

public class History {
    public String game;      // image code a00 for R.mipmap.a00_alberta
    public long []time;      // last GVal done

    public int latest;      // latest level handled;
    public int []percent;    // completed percent

    public History() {
        game = "";
        latest = -1;
        time = new long[4];
        percent = new int[4];
    }
}
