package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class DefineFullRecyclePieces {

        /*
            create recycler bin values to contain all pieces with random appearances
         */
        public DefineFullRecyclePieces() {
            final int col = gVal.colNbr;
            final int row = gVal.rowNbr;
            final int colByRow = col * row;
            int [] rNc = new int[colByRow];
            int r,c, tmp;
            for (int cr = 0; cr < colByRow; cr++) {
                r = cr / col;
                c = cr - r * col;
                rNc[cr] = 10000 + c*100 + r;
            }

            for (int j = 0; j < colByRow; j++) {
                Random rnd = new Random(System.currentTimeMillis() & 0xfffff);
                for (int i = 0; i < colByRow; i++) {
                    int sw = rnd.nextInt(colByRow);
                    if (i != sw) {
                        tmp = rNc[i];
                        rNc[i] = rNc[sw];
                        rNc[sw] = tmp;
                    }
                }
            }
            gVal.allPossibleJigs = new ArrayList<>();
            for (int i = 0; i < colByRow; i++)
                gVal.allPossibleJigs.add(rNc[i]);

            Log.w("chk",gVal.allPossibleJigs.toString());
        }

}
