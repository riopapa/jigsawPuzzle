package com.riopapa.zigsawpuzzle;

import android.util.Log;

import com.riopapa.zigsawpuzzle.model.JigTable;

import java.util.Random;

public class SetBoundaryVals {

    public SetBoundaryVals(JigTable[][] zz, int xx, int yy) {

        Random rnd = new Random(System.currentTimeMillis());

        for (int y = 0; y < yy; y++) {
            for (int x = 0; x < xx; x++) {
                JigTable z = new JigTable();
                z.src = null;
                if (x == 0) {
                    z.lType = 0;
                } else {
                    z.lType = zz[x-1][y].rType;
                }
                if (x < xx-1) {
                    z.rType = 1 + rnd.nextInt(4);   // rType counts
                } else {
                    z.rType = 0;
                }

                if (y == 0) {
                    z.uType = 0;
                } else {
                    z.uType = zz[x][y-1].dType;
                }
                if (y < yy-1)
                    z.dType = 1 + rnd.nextInt(4);
                else
                    z.dType = 0;
                zz[x][y] = z;
            }
        }

        for (int y = 0; y < yy ; y++) {
            String s = " "+y+" ";
            for (int x = 0; x < xx; x++) {
                s += "("+zz[x][y].uType + "," + zz[x][y].dType+")";
            }
            Log.w("y ="+y, s);
        }
    }
}
