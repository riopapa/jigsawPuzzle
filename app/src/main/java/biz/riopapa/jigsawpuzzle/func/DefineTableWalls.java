package biz.riopapa.jigsawpuzzle.func;


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
        Random rnd = new Random(System.currentTimeMillis() & 0xfffffff);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                JigTable z = new JigTable();
                if (col == 0) {
                    z.le = 0;
                } else {
                    z.le = zz[col - 1][row].ri;
                }
                if (col < columns - 1) {
                    z.ri = 1 + rnd.nextInt(5);   // rType counts
                } else {
                    z.ri = 0;
                }

                if (row == 0) {
                    z.up = 0;
                } else {
                    z.up = zz[col][row - 1].dn;
                }
                if (row < rows - 1)
                    z.dn = 1 + rnd.nextInt(5);
                else
                    z.dn = 0;
                zz[col][row] = z;
                zz[col][row].fp = false;
            }
        }
    }
}
