package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.mContext;
import static com.riopapa.jigsawpuzzle.MainActivity.rnd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.riopapa.jigsawpuzzle.R;

public class TargetImage {
    int[] images = {R.mipmap.old_castle, R.mipmap.family_at_seashore, R.mipmap.bridge,
            R.mipmap.cafe, R.mipmap.forest_way, R.mipmap.hintersee,
            R.mipmap.seashells, R.mipmap.scenary_two_kids, R.mipmap.boat,
            R.mipmap.kingfisher, R.mipmap.medieval, R.mipmap.tiger,
            R.mipmap.tucan
    };

    public Bitmap get() {
        return BitmapFactory.decodeResource
                (mContext.getResources(), images[rnd.nextInt(images.length-1)], null);
    }

}
