package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.rnd;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import java.util.ArrayList;

public class FullRecyclePiece {

        /*
            create recycler bin values to contain all pieces with random appearances
         */
    public  FullRecyclePiece() {

        int mxSize = vars.jigCOLs * vars.jigROWs;
        vars.allPossibleJigs = new ArrayList<>();
        int []temp = new int[mxSize];
        int wkIdx = rnd.nextInt(mxSize/2);
        int r,c;
        for (int i = 0; i < mxSize ; i++) {
            int tmp = wkIdx + rnd.nextInt(mxSize/3);
            if (tmp >= mxSize) {
                tmp -= mxSize;
            }
            if (temp[tmp] != 0) {
                while (temp[tmp] == 1) {
                    tmp++;
                    if (tmp >= mxSize)
                        tmp = 0;
                }
            }
            if (vars.jigCOLs > vars.jigROWs) {
                r = tmp / vars.jigCOLs;
                c = tmp - r * vars.jigCOLs;
            } else {
                c = tmp / vars.jigROWs;
                r = tmp - c * vars.jigROWs;
            }
            vars.allPossibleJigs.add(c*10000+r);
            temp[tmp] = 1;
            wkIdx = tmp;
        }
    }


}
