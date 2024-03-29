package biz.riopapa.jigsawpuzzle.func;


import android.util.Log;

import java.util.Random;

import biz.riopapa.jigsawpuzzle.model.JigTable;

public class DefineTableWalls {

    /**
     * generate random types of each piece L, R, U, D of each pieces
     * JigTable column, row counts has be settled already
     **/

    public DefineTableWalls(JigTable[][] zz) {

        int columns = zz.length;
        int rows = zz[0].length;
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                Random rnd = new Random(System.nanoTime() + columns + rows);
                JigTable z = new JigTable();
                if (col == 0) {
                    z.le = 0;
                } else {
                    z.le = zz[col - 1][row].ri;
                }
                if (col < columns - 1) {
                    z.ri = 1 + rnd.nextInt(60) / 10;   // rType counts
                    z.ri = 1 + rnd.nextInt(60) / 10;   // rType counts
                } else {
                    z.ri = 0;
                }

                if (row == 0) {
                    z.up = 0;
                } else {
                    z.up = zz[col][row - 1].dn;
                }
                if (row < rows - 1) {
                    z.dn = 1 + rnd.nextInt(60) / 10;
                    z.dn = 1 + rnd.nextInt(60) / 10;
                }
                else
                    z.dn = 0;
                zz[col][row] = z;
            }
        }
    }
}
