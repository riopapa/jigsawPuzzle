package biz.riopapa.jigsawpuzzle.model;

public class JigTable {

//    public int posX, posY;  // sceen position
    public int lType, rType, uType, dType;  // side type
    public boolean fp;  // moved to ForeView from RecycleView
    public boolean locked;  // pieceImage is on right position

    public int count;   // for animation

}
