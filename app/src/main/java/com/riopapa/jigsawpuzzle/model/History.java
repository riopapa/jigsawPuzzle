package com.riopapa.jigsawpuzzle.model;

public class History {
    public String gameLevel;      // image code a00 for R.mipmap.a00_alberta
    public long []time;      // last GVal done
    public int []percent;    // completed percent
    public History() {
        gameLevel = "";
        time = new long[4];
        percent = new int[4];
    }
}
