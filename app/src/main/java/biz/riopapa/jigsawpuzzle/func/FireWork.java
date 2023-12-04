package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import biz.riopapa.jigsawpuzzle.R;

public class FireWork {

    /*
     * predefine masks with imgOutSize for maskup to crop image
     * output : masks[][]
     */
    public Bitmap[] make(Context cxt, int fireSize) {

        // remember to set biz.riopapa.jigsawpuzzle.SettleJigTableWall() nextInt with this value

        int[] maps = {R.mipmap.yfireworks_06, R.mipmap.yfireworks_07, R.mipmap.yfireworks_08,
                R.mipmap.yfireworks_09, R.mipmap.yfireworks_10, R.mipmap.yfireworks_11,
                R.mipmap.yfireworks_12, R.mipmap.yfireworks_20, R.mipmap.yfireworks_21,
                R.mipmap.yfireworks_23, R.mipmap.yfireworks_24, R.mipmap.yfireworks_25
        };
        Bitmap[] fireMaps = new Bitmap[maps.length];

        final float brightContrast  = 1;
        final int BrightBrightness = 120;  // positive is bright
        ColorMatrix brightMatrix = new ColorMatrix(new float[]
                {
                        brightContrast, 0, 0, 0, BrightBrightness,  // Red
                        0, brightContrast, 0, 0, BrightBrightness,  // Green
                        0, 0, brightContrast, 0, BrightBrightness,  // Blue
                        0, 0, 0, 1, 0                   // Alpha
                });
        Paint pBright = new Paint();
        pBright.setColorFilter(new ColorMatrixColorFilter(brightMatrix));

        for (int i = 0; i < maps.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource
                    (mContext.getResources(), maps[i], null);
            fireMaps[i] = Bitmap.createScaledBitmap(bitmap,
                    fireSize , fireSize, false);
            Canvas canvasBright = new Canvas(fireMaps[i]);
            canvasBright.drawBitmap(fireMaps[i], 0, 0, pBright);

        }
        return fireMaps;
    }
}
