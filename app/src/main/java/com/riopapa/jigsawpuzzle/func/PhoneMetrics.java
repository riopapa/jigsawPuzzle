package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.fPhoneSizeX;
import static com.riopapa.jigsawpuzzle.MainActivity.fPhoneSizeY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.screenX;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
import static com.riopapa.jigsawpuzzle.MainActivity.showMaxX;
import static com.riopapa.jigsawpuzzle.MainActivity.showMaxY;

import android.app.Activity;
import android.util.DisplayMetrics;

public class PhoneMetrics {
    public PhoneMetrics(Activity activity) {

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenX = metrics.widthPixels;
        screenY = metrics.heightPixels;

        // Calculate the physical screen size in inches.
        fPhoneSizeX = screenX / metrics.xdpi;       // 2.9 x 6.22 for note 20
        fPhoneSizeY = screenY / metrics.ydpi;

        recySize =  (int) (screenX / fPhoneSizeX / ((fPhoneSizeX > 3f)? 1.5f:2f));

        recySize = recySize * 9 / 7;   // while testing only

        picOSize = recySize; //  * 11 / 10;
        picISize = picOSize * 14 / (14+5+5);
        picHSize = picOSize / 2;
        picGap = picISize * 5 / 24;

        showMaxX = screenX / picISize - 2;
        showMaxY = (int) (screenX * fPhoneSizeY / fPhoneSizeX / picISize - 2);

    }
}
