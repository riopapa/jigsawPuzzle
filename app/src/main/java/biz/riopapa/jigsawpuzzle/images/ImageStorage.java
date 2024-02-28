package biz.riopapa.jigsawpuzzle.images;

import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import biz.riopapa.jigsawpuzzle.R;

public class ImageStorage {

    int[] images = {
            R.mipmap._00_apple_boots,
            R.mipmap._01_seoul,
            R.mipmap._02_builging,
            R.mipmap._03_aeria,
            R.mipmap._04_cat_dog,
            R.mipmap._05_city3,
            R.mipmap._06_apple_basket,
            R.mipmap._07_lemurs,
            R.mipmap._08_alberta,
            R.mipmap._09_paul_gaugui,
            R.mipmap._10_tajimahal,
            R.mipmap._11_cafe,
            R.mipmap._12_lpaul_cezanne
    };

    public int count() {
        return images.length;
    }

    public Bitmap getFullMap(int i) {
        return BitmapFactory.decodeResource
                (mContext.getResources(), images[i], null);
    }
    public Bitmap getThumbnail(int i) {
        Bitmap bitmap = BitmapFactory.decodeResource
                (mContext.getResources(), images[i], null);
        return Bitmap.createScaledBitmap(bitmap,
                bitmap.getWidth()/4, bitmap.getHeight()/4, false);
    }
    public String getGame(int i) {
        String []ids = mContext.getString(images[i]).split("/");
        String s = ids[ids.length-1];
        s = s.substring(0, 3);
        return  s;
    }

}
