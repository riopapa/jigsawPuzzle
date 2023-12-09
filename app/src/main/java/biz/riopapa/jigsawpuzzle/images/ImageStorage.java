package biz.riopapa.jigsawpuzzle.images;

import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import biz.riopapa.jigsawpuzzle.R;

public class ImageStorage {

    int[] images = {
            R.mipmap.a00_alberta,
            R.mipmap.a01_bears,
            R.mipmap.a02_boat,
            R.mipmap.a03_birds,
            R.mipmap.a09_cafe,
            R.mipmap.b01_builging,
            R.mipmap.a10_cat_dog,
            R.mipmap.b15_city3,
            R.mipmap.b26_tajimahal,
            R.mipmap.b27_apple_basket,
            R.mipmap.b28_apple_boots,
            R.mipmap.b29_flower_pole,
            R.mipmap.b30_flower_ampola,
            R.mipmap.b33_family_at_seashore,
            R.mipmap.b40_flower2,
            R.mipmap.b45_forest_way,
            R.mipmap.c10_golf_yard,
            R.mipmap.c15_horse,
            R.mipmap.c20_house2,
            R.mipmap.c25_house_mountain

    };
//    R.mipmap.c35_korean_party1,
//    R.mipmap.c40_kwangwha2,
//    R.mipmap.c45_lake_side,
//    R.mipmap.c50_medieval,
//    R.mipmap.c55_mountain3,
//    R.mipmap.c60_mountain4,
//    R.mipmap.c65_mountains2,
//    R.mipmap.c70_old_castle,
//    R.mipmap.d10_river,
//    R.mipmap.d15_road,
//    R.mipmap.d20_scenary_two_kids,
//    R.mipmap.d25_seagul,
//    R.mipmap.d30_seashells,
//    R.mipmap.d35_seoul1,
//    R.mipmap.d40_seoul4,
//    R.mipmap.d50_siblings,
//    R.mipmap.d55_sunrise,
//    R.mipmap.d60_tiger,
//    R.mipmap.d65_trees,
//    R.mipmap.d70_tucan,
//    R.mipmap.d75_unicon

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
