package com.riopapa.jigsawpuzzle.model;

import android.graphics.Bitmap;

public class FloatPiece {
    public int R, C;
    public Bitmap oLine, bigMap;
    public long time;
    public int count;
    public JigTable jig;

    public int posX, posY;
    public long uId, anchorId;    // by timeStamp
}
