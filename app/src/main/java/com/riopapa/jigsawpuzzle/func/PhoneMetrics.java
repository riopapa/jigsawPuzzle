package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.app.Activity;
import android.util.DisplayMetrics;

public class PhoneMetrics {
    public PhoneMetrics(Activity activity) {

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        vars.screenX = metrics.widthPixels;
        vars.screenY = metrics.heightPixels;

        // Calculate the physical screen size in inches.
        vars.fPhoneSizeX = vars.screenX / metrics.xdpi;       // 2.9 x 6.22 for note 20
        vars.fPhoneSizeY = vars.screenY / metrics.ydpi;

        vars.recySize =  (int) (vars.screenX / vars.fPhoneSizeX / ((vars.fPhoneSizeX > 3f)? 1.5f:2f));

        vars.recySize = vars.recySize * 9 / 7;   // while testing only

        vars.picOSize = vars.recySize; //  * 11 / 10;
        vars.picISize = vars.picOSize * 14 / (14+5+5);
        vars.picHSize = vars.picOSize / 2;
        vars.picGap = vars.picISize * 5 / 24;

        vars.showMaxX = vars.screenX / vars.picISize - 2;
        vars.showMaxY = (int) (vars.screenX * vars.fPhoneSizeY / vars.fPhoneSizeX / vars.picISize - 2);

    }
}
