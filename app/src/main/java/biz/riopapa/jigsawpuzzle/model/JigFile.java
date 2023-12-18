package biz.riopapa.jigsawpuzzle.model;

public class JigFile {
    public String game; // _00 ~ _xx : internal mipmap else from google drive
    public String thumbnailMap; // if thumbnailMap is null then should be get from google Drive
    public String imageId, keywords;
    public String timeStamp;
    public int latest;      // latest level handled;
    public long []time;      // last GVal done

    public int [] locked;   // pieces locked
    public int [] percent;    // completed percent
    public boolean newFlag, downloaded;
    public JigFile() {
        game = null;
        thumbnailMap = null;    // if thumbnailMap is null then should be get from google Drive
        imageId = null;
        keywords = null;
        timeStamp = "00";
        latest = -1;
        time = new long[4];
        locked = new int [4];
        percent = new int[4];
    }

}
