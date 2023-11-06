package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.riopapa.jigsawpuzzle.R;

public class TargetImage {

    int[] images = {
            R.mipmap.alberta,
            R.mipmap.boat,
            R.mipmap.bridge,
            R.mipmap.butterfly_flower,
            R.mipmap.cafe,
            R.mipmap.family_at_seashore,
            R.mipmap.field,
            R.mipmap.flower1,
            R.mipmap.flower2,
            R.mipmap.flower_butterfly,
            R.mipmap.flower_field,
            R.mipmap.forest_way,
            R.mipmap.golf_yard,
            R.mipmap.hintersee,
            R.mipmap.horse,
            R.mipmap.kingfisher,
            R.mipmap.kwang_myung,
            R.mipmap.lake_side,
            R.mipmap.leaves,
            R.mipmap.medieval,
            R.mipmap.mountain3,
            R.mipmap.mountain4,
            R.mipmap.mountains2,
            R.mipmap.mountain_landscape,
            R.mipmap.neom,
            R.mipmap.old_castle,
            R.mipmap.plum_blossom,
            R.mipmap.river,
            R.mipmap.road,
            R.mipmap.scenary_two_kids,
            R.mipmap.seashells,
            R.mipmap.ship_sea,
            R.mipmap.siblings,
            R.mipmap.sunrisejpg,
            R.mipmap.terras,
            R.mipmap.tiger,
            R.mipmap.tucan,
            R.mipmap.winter_scean

    };

    public int count() {
        return images.length;
    }

    public Bitmap get(int i) {
        return BitmapFactory.decodeResource
                (mContext.getResources(), images[i], null);
    }

}
