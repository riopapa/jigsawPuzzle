package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

public class PiecePosition {

    int gapAllowed;
    public PiecePosition() {
        gapAllowed = gVal.picGap + gVal.picGap + gVal.picGap;
    }

    public boolean isLockable(int ac, int ar, int posX, int posY) {

        if (isNearLocked(ac, ar)) {
            int x = gVal.baseX + (ac - gVal.offsetC) * gVal.picISize;
            int y = gVal.baseY + (ar - gVal.offsetR) * gVal.picISize;
            return Math.abs(posX - x) <= gapAllowed && Math.abs(posY - y) <= gapAllowed;
        } else
            return false;
    }

    boolean isNearLocked(int ac, int ar) {

        // return true if near by pieces are already locked

        boolean left, right = false, up, down = false;

        if (ac == 0 || ar == 0 || ac == gVal.colNbr - 1 || ar == gVal.rowNbr - 1)
            return true;
        left = gVal.jigTables[ac-1][ar].locked;
        if (ac != gVal.colNbr -1)
            right = gVal.jigTables[ac+1][ar].locked;
        up = gVal.jigTables[ac][ar-1].locked;
        if (ar != gVal.rowNbr -1)
            down = gVal.jigTables[ac][ar+1].locked;

        return left | right | up | down;
    }


}

