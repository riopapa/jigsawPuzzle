package biz.riopapa.jigsawpuzzle.model;

import android.graphics.Bitmap;

public class JigTable {

//    public int posX, posY;  // sceen position
    public int lType, rType, uType, dType;  // side type
    public boolean outRecycle;  // moved to PaintView from RecycleView
    public boolean locked;  // pieceImage is on right position

    public int count;   // for animation

}
