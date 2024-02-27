package biz.riopapa.jigsawpuzzle.model;

public class JigTable {

//    public int posX, posY;  // sceen position
    public int le, ri, up, dn;  // side type
    public boolean fp;  // moved to ForeView from RecycleView
    public boolean locked;  // pieceImage is on right position
    public int count;   // for animation

}
