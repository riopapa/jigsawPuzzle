package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.app.Activity;
import android.util.DisplayMetrics;

public class PhoneMetrics {
    public PhoneMetrics(Activity activity) {

        /*
        ** determine screen sizes and set related parameters
         */
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        vars.screenX = metrics.widthPixels;
        vars.screenY = metrics.heightPixels;

        // Calculate the physical screen size in inches.
        vars.fPhoneInchX = vars.screenX / metrics.xdpi;       // 2.9 x 6.22 for note 20
        vars.fPhoneInchY = vars.screenY / metrics.ydpi;

    }
}
