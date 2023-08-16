package com.riopapa.zigsawpuzzle;

import java.util.Random;

public class ZigSawLRUD {

    public ZigSawLRUD (ZigInfo[][] zz, int zigX, int zigY) {

        Random rnd = new Random(System.currentTimeMillis());

        for (int y = 0; y < zigY; y++) {
            for (int x = 0; x < zigX; x++) {
                ZigInfo z = new ZigInfo();
                if (x == 0) {
                    z.lType = 0;
                } else {
                    z.lType = zz[x-1][y].rType;
                }
                if (x < zigX-1) {
                    z.rType = 1 + rnd.nextInt(3);
                } else {
                    z.rType = 0;
                }

                if (y == 0) {
                    z.uType = 0;
                } else {
                    z.uType = zz[x][y-1].dType;
                }
                if (y < zigY-1)
                    z.dType = 1 + rnd.nextInt(3);
                else
                    z.dType = 0;
                zz[x][y] = z;
            }
        }
    }
}
