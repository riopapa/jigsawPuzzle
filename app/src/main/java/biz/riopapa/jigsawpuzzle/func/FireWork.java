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

//        int[] maps = {R.mipmap.yfireworks_06, R.mipmap.yfireworks_07, R.mipmap.yfireworks_08,
//                R.mipmap.yfireworks_09, R.mipmap.yfireworks_10, R.mipmap.yfireworks_11,
//                R.mipmap.yfireworks_12, R.mipmap.yfireworks_20, R.mipmap.yfireworks_21,
//                R.mipmap.yfireworks_23, R.mipmap.yfireworks_24, R.mipmap.yfireworks_25
//        };
        int[] maps = {R.mipmap.zfirewick27, R.mipmap.zfirewick26, R.mipmap.zfirewick25,
                R.mipmap.zfirewick24, R.mipmap.zfirewick23, R.mipmap.zfirewick22,
//                R.mipmap.zfirewick21, R.mipmap.zfirewick20, R.mipmap.zfirewick19,
                R.mipmap.zfirewick18, R.mipmap.zfirewick17, R.mipmap.zfirewick16,
                R.mipmap.zfirewick15, R.mipmap.zfirewick14, R.mipmap.zfirewick13,
                R.mipmap.zfirewick12, R.mipmap.zfirewick11, R.mipmap.zfirewick10,
                R.mipmap.zfirewick09, R.mipmap.zfirewick08, R.mipmap.zfirewick07,
                R.mipmap.zfirewick06, R.mipmap.zfirewick05, R.mipmap.zfirewick04,
//                R.mipmap.zfirewick03, R.mipmap.zfirewick02, R.mipmap.zfirewick01,
                R.mipmap.zfirewick00
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
