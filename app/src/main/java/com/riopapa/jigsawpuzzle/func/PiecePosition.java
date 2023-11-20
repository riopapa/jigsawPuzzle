package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

import android.app.Activity;

public class PiecePosition {

    Activity activity;
    public PiecePosition(Activity activity) {
        this.activity = activity;
    }
    public boolean isLockable(int cc, int rr, int posX, int posY) {

        if (isNearLocked(cc, rr)) {
            int x = GVal.baseX + (cc - GVal.offsetC) * GVal.picISize;
            int y = GVal.baseY + (rr - GVal.offsetR) * GVal.picISize;
            return Math.abs(posX - x) <= GVal.picGap && Math.abs(posY - y) <= GVal.picGap;
        } else
            return false;
    }
    boolean isNearLocked(int cc, int rr) {

        // return true if near by pieces are already locked

        boolean left, right = false, up, down = false;

        if (cc == 0 || rr == 0 || cc == GVal.jigCOLs - 1 || rr == GVal.jigROWs - 1)
            return true;
        left = GVal.jigTables[cc-1][rr].locked;
        if (cc != GVal.jigCOLs -1)
            right = GVal.jigTables[cc+1][rr].locked;
        up = GVal.jigTables[cc][rr-1].locked;
        if (rr != GVal.jigROWs-1)
            down = GVal.jigTables[cc][rr+1].locked;

        return left | right | up | down;
    }


}

