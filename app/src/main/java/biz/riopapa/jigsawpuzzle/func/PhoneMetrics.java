package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchY;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;

import android.app.Activity;
import android.util.DisplayMetrics;

public class PhoneMetrics {
    public PhoneMetrics(Activity activity) {

        /*
        ** determine screen sizes and set related parameters
         */
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenX = metrics.widthPixels;
        screenY = metrics.heightPixels;

        // Calculate the physical screen size in inches.
        fPhoneInchX = screenX / metrics.xdpi;       // 2.9 x 6.22 for note 20
        fPhoneInchY = screenY / metrics.ydpi;
//        Log.w("DPI info ", metrics.xdpi/metrics.ydpi+" x ="+metrics.xdpi+" y="+metrics.ydpi);
    }
}
