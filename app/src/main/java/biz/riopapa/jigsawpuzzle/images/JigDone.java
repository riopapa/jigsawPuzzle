package biz.riopapa.jigsawpuzzle.images;

import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import biz.riopapa.jigsawpuzzle.R;

public class JigDone {

    public Bitmap[] make(int mapSize) {

        int[] maps = {R.mipmap.zjig_done_i, R.mipmap.zjig_done_j,
                R.mipmap.zjig_done_k, R.mipmap.zjig_done_l, R.mipmap.zjig_done_m,
                R.mipmap.zjig_done_n, R.mipmap.zjig_done_o, R.mipmap.zjig_done_p,
                R.mipmap.zjig_done_q, R.mipmap.zjig_done_r, R.mipmap.zjig_done_s,
                R.mipmap.zjig_done_t, R.mipmap.zjig_done_u, R.mipmap.zjig_done_v,
                R.mipmap.zjig_done_w, R.mipmap.zjig_done_x, R.mipmap.zjig_done_y,
                R.mipmap.zjig_done_z, R.mipmap.zjig_done_j, R.mipmap.zjig_done_j
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
