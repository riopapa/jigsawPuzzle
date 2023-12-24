package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

public class PiecePosition {

    int gapAllowed;
    public PiecePosition() {
        gapAllowed = gVal.picGap + gVal.picGap;
    }
    public boolean isLockable(int cc, int rr, int posX, int posY) {

        if (isNearLocked(cc, rr)) {
            int x = gVal.baseX + (cc - gVal.offsetC) * gVal.picISize;
            int y = gVal.baseY + (rr - gVal.offsetR) * gVal.picISize;
            return Math.abs(posX - x) <= gapAllowed && Math.abs(posY - y) <= gapAllowed;
        } else
            return false;
    }
    boolean isNearLocked(int cc, int rr) {

        // return true if near by pieces are already locked

        boolean left, right = false, up, down = false;

        if (cc == 0 || rr == 0 || cc == gVal.colNbr - 1 || rr == gVal.rowNbr - 1)
            return true;
        left = gVal.jigTables[cc-1][rr].locked;
        if (cc != gVal.colNbr -1)
            right = gVal.jigTables[cc+1][rr].locked;
        up = gVal.jigTables[cc][rr-1].locked;
        if (rr != gVal.rowNbr -1)
            down = gVal.jigTables[cc][rr+1].locked;

        return left | right | up | down;
    }


}

