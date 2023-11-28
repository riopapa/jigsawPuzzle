package com.riopapa.jigsawpuzzle.model;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;

public class GVal {

    public int version; // vvrrxx
    public int imgOutSize, imgInSize, imgGapSize;  // real pieceImage size

    public int recSize, picOSize, picISize, picGap, picHSize;
    // recycler size, at PaintView size;
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

    public ArrayList<Integer> allPossibleJigs, activeJigs;
    // allRandomJigs contains jigsaws id which is not moved to floatingPiece
    // activeRecyclerJigs contains available jigsaws currently

    public ArrayList<FloatPiece> fps;    // floating jigsaws
    public int showMaxX, showMaxY;   // how many pieces can be in columns / rows
    public int showShiftX, showShiftY;
    public  int gameLevel;
    public boolean allLocked = false;
    public int offsetC, offsetR; // show offset Column, Row;


}
