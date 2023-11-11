package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.riopapa.jigsawpuzzle.R;

public class ImageStorage {

    int[] images = {
            R.mipmap.a00_alberta,
            R.mipmap.a01_bears,
            R.mipmap.a02_boat,
            R.mipmap.a03_birds,
            R.mipmap.a04_bridge,
            R.mipmap.a09_cafe,
            R.mipmap.a10_cat_dog,
            R.mipmap.butterfly_flower,
            R.mipmap.chunggye,
            R.mipmap.city3,
            R.mipmap.city_barsava,
            R.mipmap.city_old1,
            R.mipmap.ducks,
            R.mipmap.family_at_seashore,
            R.mipmap.field,
            R.mipmap.fishs1,
            R.mipmap.fishs2,
            R.mipmap.flower2,
            R.mipmap.flower_field,
            R.mipmap.forest_way,
            R.mipmap.giraffe,
            R.mipmap.golf_yard,
            R.mipmap.hintersee,
            R.mipmap.hollywood,
            R.mipmap.horse,
            R.mipmap.house2,
            R.mipmap.house_mountain,
            R.mipmap.kingfisher,
            R.mipmap.korean_house,
            R.mipmap.korean_party1,
            R.mipmap.kwangwha,
            R.mipmap.kwangwha2,
            R.mipmap.lake_side,
            R.mipmap.leaves,
            R.mipmap.lotter_tower,
            R.mipmap.medieval,
            R.mipmap.meercat,
            R.mipmap.mountain3,
            R.mipmap.mountain4,
            R.mipmap.mountains2,
            R.mipmap.mountain_landscape,
            R.mipmap.neom,
            R.mipmap.old_castle,
            R.mipmap.river,
            R.mipmap.road,
            R.mipmap.scenary_two_kids,
            R.mipmap.seagul,
            R.mipmap.seashells,
            R.mipmap.seoul1,
            R.mipmap.seoul4,
            R.mipmap.seoul5,
            R.mipmap.ship_sea,
            R.mipmap.siblings,
            R.mipmap.sunrise,
            R.mipmap.terras,
            R.mipmap.tiger,
            R.mipmap.tiger2,
            R.mipmap.trees,
            R.mipmap.tucan,
            R.mipmap.unicon,
            R.mipmap.winter_scean

    };

    public int count() {
        return images.length;
    }

    public Bitmap getMap(int i) {
        return BitmapFactory.decodeResource
                (mContext.getResources(), images[i], null);
    }
    public String getStr(int i) {
        String []ids = mContext.getString(images[i]).split("/");
        String s = ids[ids.length-1];
        s = s.substring(0, s.length()-4);
        return  s;
    }

}
