package com.riopapa.zigsawpuzzle;

import static com.riopapa.zigsawpuzzle.MainActivity.piece;
import static com.riopapa.zigsawpuzzle.MainActivity.pw;
import static com.riopapa.zigsawpuzzle.MainActivity.zigInfo;
import static com.riopapa.zigsawpuzzle.MainActivity.maskMaps;
import static com.riopapa.zigsawpuzzle.MainActivity.fullImage;
import static com.riopapa.zigsawpuzzle.MainActivity.zw;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class PieceBitmap {

    public void make(int x, int y) {
        ZigInfo z = zigInfo[x][y];
        Bitmap srcMap = Bitmap.createBitmap(fullImage, 480 + x *pw, 330 + y *pw, zw, zw);
        Bitmap mask = maskMerge(maskMaps[0][z.lType], maskMaps[1][z.rType],
                maskMaps[2][z.uType], maskMaps[3][z.dType], pw, zw);
        z.src = piece.cropZig(srcMap, mask);
        z.oLine = piece.getOutline(z.src, 0xFF8899AA);
        z.oLine2 = piece.getOutline(z.oLine,0xFF667788);
        zigInfo[x][y] = z;
    }
    private Bitmap maskMerge(Bitmap maskL, Bitmap maskR, Bitmap maskU, Bitmap maskD, int pw, int zw) {
        Bitmap tMap = Bitmap.createBitmap(zw, zw, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tMap);
        canvas.drawBitmap(maskL, 0,0, null);
        canvas.drawBitmap(maskR, 0,0, null);
        canvas.drawBitmap(maskU, 0,0, null);
        canvas.drawBitmap(maskD, 0,0, null);
        return tMap;
    }

}
