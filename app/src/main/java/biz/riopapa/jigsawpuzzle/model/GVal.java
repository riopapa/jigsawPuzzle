package biz.riopapa.jigsawpuzzle.model;

import java.util.ArrayList;

public class GVal {

    public String version; // vvrrxx

    public String game;
    public  int level;
    public long time; // last saved time
    public int imgFullWidth, imgFullHeight, imgOutSize, imgInSize, imgGapSize;  // real pieceImage size

    public int recSize, picOSize, picISize, picGap, picHSize;
    // recycler size, at ForeView size;
    // picOSize : picture outer size
    // picISize : picture inner size
    // picGap   : gap between picISize and picOSize
    // picHSize : half of picOSize;
    // pieceISize: one PieceSize for slicing images
    // pieceOSize: one Outer for slicing images

    // gameMode 0: newly installed;
    public JigTable[][] jigTables;

    public int colNbr, rowNbr; // jigsaw slices column by row

    public int baseX, baseY; // puzzle view x, y offset

    public ArrayList<Integer> allPossibleJigs;
    // allRandomJigs contains jigsaws id which is not moved to floatingPiece
    // activeRecyclerJigs contains available jigsaws currently

    public ArrayList<FloatPiece> fps;    // floating jigsaws
    public int showMaxX, showMaxY;   // how many pieces can be in columns / rows
    public int showShiftX, showShiftY;
    public boolean allLocked = false;
    public int offsetC, offsetR; // show offset Column, Row;


}
