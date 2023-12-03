package biz.riopapa.jigsawpuzzle.func;

import java.util.Random;

import biz.riopapa.jigsawpuzzle.model.JigTable;

public class SettleJigTableWall {

    /**
     * generate random types of each piece L, R, U, D of each pieces
     * JigTable column, row counts has be settled already
     **/

    public SettleJigTableWall(JigTable[][] zz) {

        int columns = zz.length;
        int rows = zz[0].length;
        Random rnd = new Random(System.currentTimeMillis() & 0xffffff);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                JigTable z = new JigTable();
                if (col == 0) {
                    z.lType = 0;
                } else {
                    z.lType = zz[col-1][row].rType;
                }
                if (col < columns-1) {
                    z.rType = 1 + rnd.nextInt(4);   // rType counts
                } else {
                    z.rType = 0;
                }

                if (row == 0) {
                    z.uType = 0;
                } else {
                    z.uType = zz[col][row-1].dType;
                }
                if (row < rows-1)
                    z.dType = 1 + rnd.nextInt(4);
                else
                    z.dType = 0;
                zz[col][row] = z;
                zz[col][row].outRecycle = false;
            }
        }
    }
}
