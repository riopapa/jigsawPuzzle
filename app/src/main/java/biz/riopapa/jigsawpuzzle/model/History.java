package biz.riopapa.jigsawpuzzle.model;

public class History {
    public String game;      // image code a00 for R.mipmap.a00_alberta
    public int latestLvl;      // latest level handled;
    public long []time;      // last GVal done

    public int [] locked;   // pieces locked
    public int [] percent;    // completed percent
    public History() {
        latestLvl = -1;
        time = new long[4];
        locked = new int [4];
        percent = new int[4];
    }
}
