package biz.riopapa.jigsawpuzzle.images;

import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import biz.riopapa.jigsawpuzzle.R;

public class Congrat {

    /*
     * predefine masks with imgOutSize for maskup to crop image
     * output : masks[][]
     */
    public Bitmap[] make(Context cxt, int fireSize) {

        // remember to set biz.riopapa.jigsawpuzzle.DefineTableWalls() nextInt with this value
        // outerSize means puzzle outer size
//        int[] maps = {R.mipmap.fireworks_a, R.mipmap.fireworks_c, R.mipmap.fireworks_f,
//                R.mipmap.fireworks_h, R.mipmap.fireworks_m, R.mipmap.fireworks_q, R.mipmap.fireworks_x
//        };
        int[] maps = {R.mipmap.zcongrat_d, R.mipmap.zcongrat_f, R.mipmap.zcongrat_l,
                R.mipmap.zcongrat_n, R.mipmap.zcongrat_q, R.mipmap.zcongrat_y
        };
        Bitmap[] congrats = new Bitmap[maps.length];

        for (int i = 0; i < maps.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource
                    (mContext.getResources(), maps[i], null);
            congrats[i] = Bitmap.createScaledBitmap(bitmap,
                    fireSize , fireSize, false);
        }
        return congrats;
    }
}
