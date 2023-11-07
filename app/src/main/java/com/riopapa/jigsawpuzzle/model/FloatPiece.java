package com.riopapa.jigsawpuzzle.model;

import android.graphics.Bitmap;

public class FloatPiece {
    public int R, C;
    public Bitmap oLine;
    public int count;
    public int mode;

    public long uId;
    public long anchorId;    // 0 : alone, -1 : just rightPos, timeStamp: anchored
}
