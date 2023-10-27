package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigROWs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;

import android.app.Activity;

public class NearBy {

    Activity activity;
    public NearBy(Activity activity) {
        this.activity = activity;
    }
    public boolean isLockable() {

        boolean left = false, right = false, up = false, down = false;
        if (nowC == 0 && nowR == 0 || (nowC == jigCOLUMNs - 1 && nowR == 0) ||
            (nowC == 0 && nowR == jigROWs -1) || (nowC == jigCOLUMNs -1 && nowR == jigROWs -1))
            return true;
        if (nowC != 0)
            left = jigTables[nowC-1][nowR].locked;
        if (nowC != jigCOLUMNs-1)
            right = jigTables[nowC+1][nowR].locked;
        if (nowR != 0)
            up = jigTables[nowC][nowR-1].locked;
        if (nowR != jigROWs-1)
            down = jigTables[nowC][nowR+1].locked;

        return left | right | up | down;
    }
}
