package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.rnd;
import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

import java.util.ArrayList;

public class FullRecyclePiece {

        /*
            create recycler bin values to contain all pieces with random appearances
         */
    public  FullRecyclePiece() {

        int mxSize = GVal.jigCOLs * GVal.jigROWs;
        GVal.allPossibleJigs = new ArrayList<>();
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
            if (GVal.jigCOLs > GVal.jigROWs) {
                r = tmp / GVal.jigCOLs;
                c = tmp - r * GVal.jigCOLs;
            } else {
                c = tmp / GVal.jigROWs;
                r = tmp - c * GVal.jigROWs;
            }
            GVal.allPossibleJigs.add(c*10000+r);
            temp[tmp] = 1;
            wkIdx = tmp;
        }
    }


}
