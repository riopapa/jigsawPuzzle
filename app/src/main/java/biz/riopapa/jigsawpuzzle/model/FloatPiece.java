package biz.riopapa.jigsawpuzzle.model;

public class FloatPiece {
    public int R, C;
    public int posX, posY;
    public int count;
    public int mode;
    public long uId;
    public long anchorId;    // 0 : alone, -1 : just rightPos, timeStamp: anchored
}
