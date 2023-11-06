package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.Vars.baseX;
import static com.riopapa.jigsawpuzzle.Vars.baseY;
import static com.riopapa.jigsawpuzzle.Vars.offsetC;
import static com.riopapa.jigsawpuzzle.Vars.offsetR;
import static com.riopapa.jigsawpuzzle.Vars.picGap;
import static com.riopapa.jigsawpuzzle.Vars.picHSize;
import static com.riopapa.jigsawpuzzle.Vars.picISize;

import android.app.Activity;


public class RightPosition {

    Activity activity;
    int calcC, calcR;

    public RightPosition(Activity activity) {
        this.activity = activity;
    }
    public boolean isHere(int cc, int rr, int posX, int posY) {

        // return true if this piece is on right position

        calcC = (posX - baseX - picHSize) / picISize + offsetC;
        calcR = (posY - baseY - picHSize) / picISize + offsetR;
        int x = baseX +(cc - offsetC) * picISize ;
        int y = baseY + (rr - offsetR) * picISize;
//        activity.runOnUiThread(() -> tvLeft.setText(txt));
//        if (calcC != cc || calcR != rr)
//            return false;
//        Log.w("Gap", Math.abs(posX - x)+" x "+ Math.abs(posY - y));
        return Math.abs(posX - x) <= picGap && Math.abs(posY - y) <= picGap;
    }

}
