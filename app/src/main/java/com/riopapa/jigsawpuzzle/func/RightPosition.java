package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetC;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetR;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.tvLeft;
import static com.riopapa.jigsawpuzzle.PaintView.calcC;
import static com.riopapa.jigsawpuzzle.PaintView.calcR;

import android.app.Activity;

public class RightPosition {

    Activity activity;
    public RightPosition(Activity activity) {
        this.activity = activity;
    }
    public boolean isHere() {

        calcC = (jPosX - baseX - picHSize) / picISize + offsetC;
        calcR = (jPosY - baseY - picHSize) / picISize + offsetR;
        int xR = baseX +(nowC - offsetC) * picISize + picISize + picGap;
        int xL = xR - picGap - picGap;
        int yB = baseY + (nowR - offsetR) * picISize + picISize + picGap;
        int yT = yB - picGap - picGap;
        String txt = "x "+xL+" <"+jPosX+">"+ " " + xR +
                "\n"+ yT+" <"+jPosY+"> " + yB  +
                "\nnow "+nowC+"x"+nowR;
        activity.runOnUiThread(() -> tvLeft.setText(txt));
        if (calcC != nowC || calcR != nowR)
            return false;
        if (jPosX < xL || jPosX > xR)
            return false;
        if (jPosY < yT || jPosY > yB)
            return false;
        return true;
    }

}
