package com.riopapa.jigsawpuzzle.model;

public class History {
    public String key;      // image code a00 for R.mipmap.a00_alberta
    public long []time;      // last game done
    public int []percent;    // completed percent
    public History() {
        key = "";
        time = new long[4];
        percent = new int[4];
    }
}
