package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetC;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetR;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.PaintView.calcC;
import static com.riopapa.jigsawpuzzle.PaintView.calcR;

import android.app.Activity;
import android.util.Log;


public class RightPosition {

    Activity activity;
    public RightPosition(Activity activity) {
        this.activity = activity;
    }
    public boolean isHere(int cc, int rr, int posX, int posY) {

        calcC = (posX - baseX - picHSize) / picISize + offsetC;
        calcR = (posY - baseY - picHSize) / picISize + offsetR;
        int x = baseX +(cc - offsetC) * picISize ;
        int y = baseY + (rr - offsetR) * picISize;
//        activity.runOnUiThread(() -> tvLeft.setText(txt));
//        if (calcC != cc || calcR != rr)
//            return false;
        Log.w("Gap", Math.abs(posX - x)+" x "+ Math.abs(posY - y));
        if (Math.abs(posX - x) > picGap || Math.abs(posY - y) > picGap)
            return false;
        return true;
    }

}
