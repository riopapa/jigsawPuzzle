package biz.riopapa.jigsawpuzzle.model;

public class JigFile {
    public String game; // _00 ~ _xx : internal mipmap else from google drive
    public String imageId;
    public int latestLvl;      // latest level handled;
    public long []time;      // last GVal done

    public int [] locked;   // pieces locked
    public int [] percent;    // completed percent
    public boolean newFlag;
    public JigFile() {
        latestLvl = -1;
        time = new long[4];
        locked = new int [4];
        percent = new int[4];
    }

}
