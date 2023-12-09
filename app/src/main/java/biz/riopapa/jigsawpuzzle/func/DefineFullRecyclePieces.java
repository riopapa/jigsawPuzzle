package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class DefineFullRecyclePieces {

        /*
            create recycler bin values to contain all pieces with random appearances
         */
    public DefineFullRecyclePieces() {

        Random rnd = new Random(System.currentTimeMillis() & 0xfffff);
        int mxSize = gVal.colNbr * gVal.rowNbr;
        ArrayList<Integer> arrayList = new ArrayList<>();
        int []temp = new int[mxSize];
        int wkIdx = rnd.nextInt(mxSize/2);
        int r = 0,c;
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
            if (gVal.colNbr > gVal.rowNbr) {
                r = tmp / gVal.colNbr;
                c = tmp - r * gVal.colNbr;
            } else {
                c = tmp / gVal.rowNbr;
                r = tmp - c * gVal.rowNbr;
            }
            arrayList.add(c*10000+r);
            temp[tmp] = 1;
            wkIdx = tmp;
        }
        // shuffle more
        c = rnd.nextInt(mxSize/3);
        r = rnd.nextInt(mxSize/2);
        for (int i = 0; i <mxSize; i++) {
            c += rnd.nextInt(mxSize/2);
            if (c > mxSize-1)
                c = c % mxSize;
            r += rnd.nextInt(mxSize/2);
            if (r > mxSize-1)
                r = r % mxSize;
            Collections.swap(arrayList, r, c);
        }
        gVal.allPossibleJigs = arrayList;
    }


}
