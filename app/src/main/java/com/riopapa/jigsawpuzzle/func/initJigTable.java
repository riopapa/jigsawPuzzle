package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.rnd;

import com.riopapa.jigsawpuzzle.model.JigTable;

public class initJigTable {

    /**
     * generate random types of each piece L, R, U, D of each pieces
     * @param colSize, rowSize : jigTable size
     * @return JigTable
     **/

    public initJigTable(JigTable[][] zz, int colSize, int rowSize) {

        //

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
