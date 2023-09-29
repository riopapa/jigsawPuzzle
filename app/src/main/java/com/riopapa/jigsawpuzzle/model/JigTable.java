package com.riopapa.jigsawpuzzle.model;

import android.graphics.Bitmap;

public class JigTable {

    public int posX, posY;  // sceen position
    public Bitmap src;      // crop from fullImage
    public Bitmap oLine;    // outlined image
    public Bitmap oLine2;   // outlined outlined image
    public int lType, rType, uType, dType;  // side type
    public boolean outRecycle;  // moved to PaintView from RecycleView
    public boolean locked;  // piece is on right position
    public JigTable() {}

}
