package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.rnd;
import static com.riopapa.jigsawpuzzle.Vars.allPossibleJigs;
import static com.riopapa.jigsawpuzzle.Vars.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.Vars.jigROWs;

import java.util.ArrayList;

public class FullRecyclePiece {

        /*
            create recycler bin values to contain all pieces with random appearances
         */
    public  FullRecyclePiece() {

        int mxSize = jigCOLUMNs * jigROWs;
        allPossibleJigs = new ArrayList<>();
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
            if (jigCOLUMNs > jigROWs) {
                r = tmp / jigCOLUMNs;
                c = tmp - r * jigCOLUMNs;
            } else {
                c = tmp / jigROWs;
                r = tmp - c * jigROWs;
            }
            allPossibleJigs.add(c*10000+r);
            temp[tmp] = 1;
            wkIdx = tmp;
        }
    }


}
