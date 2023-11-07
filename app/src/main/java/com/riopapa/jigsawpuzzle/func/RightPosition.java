package com.riopapa.jigsawpuzzle.func;


import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.app.Activity;


public class RightPosition {

    Activity activity;
    int calcC, calcR;

    public RightPosition(Activity activity) {
        this.activity = activity;
    }
    public boolean isHere(int cc, int rr, int posX, int posY) {

        // return true if this piece is on right position

        calcC = (posX - vars.baseX - vars.picHSize) / vars.picISize + vars.offsetC;
        calcR = (posY - vars.baseY - vars.picHSize) / vars.picISize + vars.offsetR;
        int x = vars.baseX +(cc - vars.offsetC) * vars.picISize ;
        int y = vars.baseY + (rr - vars.offsetR) * vars.picISize;
//        activity.runOnUiThread(() -> tvLeft.setText(txt));
//        if (calcC != cc || calcR != rr)
//            return false;
//        Log.w("Gap", Math.abs(posX - x)+" x "+ Math.abs(posY - y));
        return Math.abs(posX - x) <= vars.picGap && Math.abs(posY - y) <= vars.picGap;
    }

}
