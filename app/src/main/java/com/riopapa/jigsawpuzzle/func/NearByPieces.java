package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.app.Activity;

public class NearByPieces {

    Activity activity;
    public NearByPieces(Activity activity) {
        this.activity = activity;
    }
    public boolean lockable(int cc, int rr) {

        // return true if near by pieces are already locked

        boolean left, right = false, up, down = false;

        if (cc == 0 || rr == 0 || cc == vars.jigCOLs - 1 || rr == vars.jigROWs - 1)
            return true;
        left = vars.jigTables[cc-1][rr].locked;
        if (cc != vars.jigCOLs -1)
            right = vars.jigTables[cc+1][rr].locked;
        up = vars.jigTables[cc][rr-1].locked;
        if (rr != vars.jigROWs-1)
            down = vars.jigTables[cc][rr+1].locked;

        return left | right | up | down;
    }
}