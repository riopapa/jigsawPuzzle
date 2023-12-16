package biz.riopapa.jigsawpuzzle.images;

import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import biz.riopapa.jigsawpuzzle.R;

public class Congratulation {

    /*
     * predefine masks with imgOutSize for maskup to crop image
     * output : masks[][]
     */
    public Bitmap[] make(Context cxt, int mapSize) {

        int[] maps = {R.mipmap.z_cong_00a, R.mipmap.z_cong_01a,
                R.mipmap.z_cong_02a, R.mipmap.z_cong_03a,
                R.mipmap.z_cong_04a, R.mipmap.z_cong_05a,
                R.mipmap.z_cong_06a, R.mipmap.z_cong_07a,
                R.mipmap.z_cong_08a, R.mipmap.z_cong_09a,
                R.mipmap.z_cong_10a, R.mipmap.z_cong_11a,
                R.mipmap.z_cong_12a, R.mipmap.z_cong_13a,
                R.mipmap.z_cong_14a, R.mipmap.z_cong_15a,
                R.mipmap.z_cong_16a, R.mipmap.z_cong_17a,
                R.mipmap.z_cong_18a, R.mipmap.z_cong_19a,
                R.mipmap.z_cong_20a
        };
        Bitmap[] bMaps = new Bitmap[maps.length];

        for (int i = 0; i < maps.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource
                    (mContext.getResources(), maps[i], null);
            bMaps[i] = Bitmap.createScaledBitmap(bitmap,
                    mapSize , mapSize, false);
        }
        return bMaps;
    }
}
