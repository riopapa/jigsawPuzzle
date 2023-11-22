package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.app.Activity;

public class PiecePosition {

    Activity activity;
    public PiecePosition(Activity activity) {
        this.activity = activity;
    }
    public boolean isLockable(int cc, int rr, int posX, int posY) {

        if (isNearLocked(cc, rr)) {
            int x = gVal.baseX + (cc - gVal.offsetC) * gVal.picISize;
            int y = gVal.baseY + (rr - gVal.offsetR) * gVal.picISize;
            return Math.abs(posX - x) <= gVal.picGap && Math.abs(posY - y) <= gVal.picGap;
        } else
            return false;
    }
    boolean isNearLocked(int cc, int rr) {

        // return true if near by pieces are already locked

        boolean left, right = false, up, down = false;

        if (cc == 0 || rr == 0 || cc == gVal.jigCOLs - 1 || rr == gVal.jigROWs - 1)
            return true;
        left = gVal.jigTables[cc-1][rr].locked;
        if (cc != gVal.jigCOLs -1)
            right = gVal.jigTables[cc+1][rr].locked;
        up = gVal.jigTables[cc][rr-1].locked;
        if (rr != gVal.jigROWs-1)
            down = gVal.jigTables[cc][rr+1].locked;

        return left | right | up | down;
    }


}

