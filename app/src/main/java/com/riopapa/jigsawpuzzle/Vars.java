package com.riopapa.jigsawpuzzle;

import android.graphics.Bitmap;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;

public class Vars {

    public int jigOuterSize, jigInnerSize, jigGapSize;  // real pieceImage size

    public int recySize, picOSize, picISize, picGap, picHSize;
    // recycler size, at PaintView size;
    // picOSize : picture outer size
    // picISize : picture inner size
    // picGap   : gap between picISize and picOSize
    // picHSize : half of picOSize;
    // pieceISize: one PieceSize for slicing images
    // pieceOSize: one Outer for slicing images

    public JigTable[][] jigTables;

    public int jigCOLUMNs, jigROWs; // jigsaw slices column by row

    public int screenX, screenY; // physical screen size, center puzzleBox

    public float fPhoneSizeX, fPhoneSizeY;
    public int selectedWidth, selectedHeight; // puzzle photo size (in dpi)
    public boolean doNotUpdate; // wait while one action completed
    public Bitmap[][] maskMaps, outMaps;

    public int baseX, baseY; // puzzle view x, y offset

    public ArrayList<Integer> allPossibleJigs, activeRecyclerJigs;
    // allRandomJigs contains jigsaws id which is not moved to floatingPiece
    // activeRecyclerJigs contains available jigsaws currently

    public ArrayList<FloatPiece> fps;    // floating jigsaws
    public final int aniTO_PAINT = 123;
    public final int aniANCHOR = 321;
    public int possibleImageCount, selectedImageNbr;
    public Bitmap selectedImage, grayedImage, brightImage;
    public int showMaxX, showMaxY;   // how many pieces can be in columns / rows
    public int showShiftX, showShiftY;

    public  int difficulty;
    final String[] difficulties= {"Easy", "Normal", "Hard", "Expert"};


    public boolean allLocked = false;

    public int jigRecyclePos; // jigsaw slide x, y count

    public int nowC, nowR, jigCR;   // fullImage pieceImage array column, row , x*10000+y


    public int offsetC, offsetR; // show offset Column, Row;

    public int jPosX, jPosY; // absolute x,y rightPosition drawing current jigsaw

}
