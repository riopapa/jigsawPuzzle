package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.mContext;
import static com.riopapa.jigsawpuzzle.MainActivity.rnd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.riopapa.jigsawpuzzle.R;

public class TargetImage {
    int[] images = {
            R.mipmap.boat,
            R.mipmap.bridge,
            R.mipmap.butterfly_flower,
            R.mipmap.cafe,
            R.mipmap.family_at_seashore,
            R.mipmap.flower1,
            R.mipmap.flower_butterfly,
            R.mipmap.forest_way,
            R.mipmap.golf_yard,
            R.mipmap.hintersee,
            R.mipmap.horse,
            R.mipmap.kingfisher,
            R.mipmap.kwang_myung,
            R.mipmap.medieval,
            R.mipmap.mesh_face,
            R.mipmap.neom,
            R.mipmap.old_castle,
            R.mipmap.plum_blossom,
            R.mipmap.scenary_two_kids,
            R.mipmap.seashells,
            R.mipmap.terras,
            R.mipmap.tiger,
            R.mipmap.tucan,
            R.mipmap.winter_sceneray

    };

    public Bitmap get() {
        return BitmapFactory.decodeResource
                (mContext.getResources(), images[rnd.nextInt(images.length-1)], null);
    }

}
