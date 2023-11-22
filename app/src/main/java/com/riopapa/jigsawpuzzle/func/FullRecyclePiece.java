package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import java.util.ArrayList;
import java.util.Random;

public class FullRecyclePiece {

        /*
            create recycler bin values to contain all pieces with random appearances
         */
    public  FullRecyclePiece() {

        Random rnd = new Random(System.currentTimeMillis() & 0xfffff);
        int mxSize = gVal.jigCOLs * gVal.jigROWs;
        gVal.allPossibleJigs = new ArrayList<>();
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
            if (gVal.jigCOLs > gVal.jigROWs) {
                r = tmp / gVal.jigCOLs;
                c = tmp - r * gVal.jigCOLs;
            } else {
                c = tmp / gVal.jigROWs;
                r = tmp - c * gVal.jigROWs;
            }
            gVal.allPossibleJigs.add(c*10000+r);
            temp[tmp] = 1;
            wkIdx = tmp;
        }
    }


}
