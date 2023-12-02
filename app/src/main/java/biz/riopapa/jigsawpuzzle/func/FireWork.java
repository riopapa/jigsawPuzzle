package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import biz.riopapa.jigsawpuzzle.R;

public class FireWork {

    /*
     * predefine masks with imgOutSize for maskup to crop image
     * output : masks[][]
     */
    public Bitmap[] make(Context cxt, int fireSize) {

        // remember to set biz.riopapa.jigsawpuzzle.SettleJigTableWall() nextInt with this value
        // outerSize means puzzle outer size
//        int[] maps = {R.mipmap.fireworks_a, R.mipmap.fireworks_c, R.mipmap.fireworks_f,
//                R.mipmap.fireworks_h, R.mipmap.fireworks_m, R.mipmap.fireworks_q, R.mipmap.fireworks_x
//        };
        int[] maps = {R.mipmap.zfireworks_f,
                R.mipmap.zfireworks_h, R.mipmap.zfireworks_m, R.mipmap.zfireworks_q, R.mipmap.zfireworks_x
        };
        Bitmap[] fireMaps = new Bitmap[maps.length];

        for (int i = 0; i < maps.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource
                    (mContext.getResources(), maps[i], null);
            fireMaps[i] = Bitmap.createScaledBitmap(bitmap,
                    fireSize , fireSize, false);
        }
        return fireMaps;
    }
}
