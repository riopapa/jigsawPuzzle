package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigROWs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;

import android.app.Activity;

public class NearBy {

    Activity activity;
    public NearBy(Activity activity) {
        this.activity = activity;
    }
    public boolean isLockable(int cc, int rr) {

        boolean left = false, right = false, up = false, down = false;
        if (cc == 0 && rr == 0 || (cc == jigCOLUMNs - 1 && rr == 0) ||
            (cc == 0 && rr == jigROWs -1) || (cc == jigCOLUMNs -1 && rr == jigROWs -1))
            return true;
        if (cc != 0)
            left = jigTables[cc-1][rr].locked;
        if (cc != jigCOLUMNs-1)
            right = jigTables[cc+1][rr].locked;
        if (rr != 0)
            up = jigTables[cc][rr-1].locked;
        if (rr != jigROWs-1)
            down = jigTables[cc][rr+1].locked;

        return left | right | up | down;
    }
}
