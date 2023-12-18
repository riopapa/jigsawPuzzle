package biz.riopapa.jigsawpuzzle.images;

import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import biz.riopapa.jigsawpuzzle.R;

public class FireWork {

    public Bitmap[] make(int fireSize) {

        // remember to set biz.riopapa.jigsawpuzzle.DefineTableWalls() nextInt with this value

        int[] maps = {R.mipmap.zfirework27, R.mipmap.zfirework26, R.mipmap.zfirework25,
//                R.mipmap.zfirewick24, R.mipmap.zfirewick23, R.mipmap.zfirewick22,
                R.mipmap.zfirework21, R.mipmap.zfirework20, R.mipmap.zfirework19,
//                R.mipmap.zfirewick18, R.mipmap.zfirewick17, R.mipmap.zfirewick16,
                R.mipmap.zfirework15, R.mipmap.zfirework14, R.mipmap.zfirework13,
//                R.mipmap.zfirewick12, R.mipmap.zfirewick11, R.mipmap.zfirewick10,
                R.mipmap.zfirework09, R.mipmap.zfirework08, R.mipmap.zfirework07,
//                R.mipmap.zfirewick06, R.mipmap.zfirewick05, R.mipmap.zfirewick04,
                R.mipmap.zfirework03, R.mipmap.zfirework02, R.mipmap.zfirework01,
                R.mipmap.zfirework00
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
