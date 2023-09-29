package com.riopapa.jigsawpuzzle.model;

import android.graphics.Bitmap;

public class JigTable {

    public int posX, posY;  // sceen position
    public Bitmap src;      // crop from fullImage  sized by outerSize

    public Bitmap pic;      // resized to fit screen sized by picOSize;
    public Bitmap oLine;    // outlined image   by picOSize
    public Bitmap oLine2;   // outlined outlined image   by picOSize
    public int lType, rType, uType, dType;  // side type
    public boolean outRecycle;  // moved to PaintView from RecycleView
    public boolean locked;  // piece is on right position
    public JigTable() {}

}
