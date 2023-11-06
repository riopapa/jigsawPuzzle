package com.riopapa.jigsawpuzzle;

import android.graphics.Bitmap;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;

public class Vars {

    public static int jigOuterSize, jigInnerSize, jigGapSize;  // real pieceImage size

    public static int recySize, picOSize, picISize, picGap, picHSize;
    // recycler size, at PaintView size;
    // picOSize : picture outer size
    // picISize : picture inner size
    // picGap   : gap between picISize and picOSize
    // picHSize : half of picOSize;
    // pieceISize: one PieceSize for slicing images
    // pieceOSize: one Outer for slicing images

    public static JigTable[][] jigTables;

    public static int jigCOLUMNs, jigROWs; // jigsaw slices column by row

    public static int screenX, screenY; // physical screen size, center puzzleBox

    public static float fPhoneSizeX, fPhoneSizeY;
    public static int selectedWidth, selectedHeight; // puzzle photo size (in dpi)
    public static boolean doNotUpdate; // wait while one action completed
    public static Bitmap[][] maskMaps, outMaps;

    public static int baseX, baseY; // puzzle view x, y offset

    public static ArrayList<Integer> allPossibleJigs, activeRecyclerJigs;
    // allRandomJigs contains jigsaws id which is not moved to floatingPiece
    // activeRecyclerJigs contains available jigsaws currently

    public static ArrayList<FloatPiece> fps;    // floating jigsaws
    public static final int aniTO_PAINT = 123;
    public static final int aniANCHOR = 321;
    public static int possibleImageCount, selectedImageNbr;
    public static Bitmap selectedImage, grayedImage, brightImage;
    public static int showMaxX, showMaxY;   // how many pieces can be in columns / rows
    public static int showShiftX, showShiftY;

    public static  int difficulty;
    final static String[] difficulties= {"Easy", "Normal", "Hard", "Expert"};


    public static boolean allLocked = false;

    public static int jigRecyclePos; // jigsaw slide x, y count

    public static int nowC, nowR, jigCR;   // fullImage pieceImage array column, row , x*10000+y


    public static int offsetC, offsetR; // show offset Column, Row;

    public static int jPosX, jPosY; // absolute x,y rightPosition drawing current jigsaw

}
