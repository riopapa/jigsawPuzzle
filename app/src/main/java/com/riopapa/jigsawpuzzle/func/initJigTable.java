package com.riopapa.jigsawpuzzle.func;

import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.Random;

public class initJigTable {

    public initJigTable(JigTable[][] zz, int colSize, int rowSize) {

        //
        Random rnd = new Random(System.currentTimeMillis());

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                JigTable z = new JigTable();
                z.src = null;
                if (col == 0) {
                    z.lType = 0;
                } else {
                    z.lType = zz[col-1][row].rType;
                }
                if (col < colSize-1) {
                    z.rType = 1 + rnd.nextInt(4);   // rType counts
                } else {
                    z.rType = 0;
                }

                if (row == 0) {
                    z.uType = 0;
                } else {
                    z.uType = zz[col][row-1].dType;
                }
                if (row < rowSize-1)
                    z.dType = 1 + rnd.nextInt(4);
                else
                    z.dType = 0;
                zz[col][row] = z;
                zz[col][row].outRecycle = false;
            }
        }
    }
}