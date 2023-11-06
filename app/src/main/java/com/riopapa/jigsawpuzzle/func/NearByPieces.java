package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.Vars.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.Vars.jigROWs;
import static com.riopapa.jigsawpuzzle.Vars.jigTables;

import android.app.Activity;

public class NearByPieces {

    Activity activity;
    public NearByPieces(Activity activity) {
        this.activity = activity;
    }
    public boolean lockable(int cc, int rr) {

        // return true if near by pieces are already locked

        boolean left, right = false, up, down = false;

        if (cc == 0 || rr == 0 || cc == jigCOLUMNs - 1 || rr == jigROWs - 1)
            return true;
        left = jigTables[cc-1][rr].locked;
        if (cc != jigCOLUMNs-1)
            right = jigTables[cc+1][rr].locked;
        up = jigTables[cc][rr-1].locked;
        if (rr != jigROWs-1)
            down = jigTables[cc][rr+1].locked;

        return left | right | up | down;
    }
}
