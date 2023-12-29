package biz.riopapa.jigsawpuzzle.model;

import biz.riopapa.jigsawpuzzle.ActivityMain;

public class FloatPiece {
    public int R, C;
    public int posX, posY;
    public int count;
    public ActivityMain.GMode mode;
    public long uId;
    public long anchorId;    // 0 : alone, -1 : just rightPos, timeStamp: anchored
}
