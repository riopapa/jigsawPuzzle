package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.fullHeight;
import static com.riopapa.jigsawpuzzle.MainActivity.fullImage;
import static com.riopapa.jigsawpuzzle.MainActivity.fullWidth;
import static com.riopapa.jigsawpuzzle.MainActivity.imageAnswer;
import static com.riopapa.jigsawpuzzle.MainActivity.innerSize;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.MainActivity.jigROWs;
import static com.riopapa.jigsawpuzzle.MainActivity.outerSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.pieceGap;
import static com.riopapa.jigsawpuzzle.MainActivity.recySize;
import static com.riopapa.jigsawpuzzle.MainActivity.screenX;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.riopapa.jigsawpuzzle.Piece;

public class intGlobalValues {
    public intGlobalValues(Context context, Activity activity) {

        float pxVal, dipVal, dipRatio;
        pxVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1000f,
                context.getResources().getDisplayMetrics());
        dipVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1000f,
                context.getResources().getDisplayMetrics());
//        float mmVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1000f,
//                context.getResources().getDisplayMetrics());
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenX = metrics.widthPixels;
        screenY = metrics.heightPixels;
        Log.w("r23 Main","screenXY "+screenX+" x "+screenY);
        recySize = (int) ((float) screenX / 10f * dipVal / 2000f);
        picOSize = recySize * 11 / 10;
        picISize = picOSize * 14 / (14+5+5);
        picHSize = picOSize / 2;

        // note 20 pxVal=1000.0 dipVal=3000.0 innerSize = 468 ScreenX 1080 x 2316
        // Tab 7   pxVal=1000.0 dipVal=2125.0 innerSize = 331 ScreenX 1600 x 2560

//        Log.w("r25 "+Build.MODEL, "pxVal="+pxVal+", dipVal="+dipVal+", mmVal="+mmVal+" recSz="+recySize);

        fullWidth = fullImage.getWidth();
        fullHeight = fullImage.getHeight();
        jPosX = -1; // prevent drawing without preload
        innerSize = fullHeight / (jigROWs +1);
        if (fullWidth / (jigCOLUMNs +1) < innerSize)
            innerSize = fullWidth / (jigCOLUMNs -1);
        pieceGap = innerSize *5/14;

        outerSize = pieceGap + pieceGap + innerSize;
        int margin = 32;

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
//        layoutParams.setMargins(margin, margin, margin, margin);
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.width = screenX - margin - margin;
        layoutParams.height = screenX - margin - margin;
        imageAnswer.setLayoutParams(layoutParams);
        baseX = margin ;
        baseY = (screenY - screenX - recySize)/2;
        Log.w("r21 sizeCheck","image "+ fullWidth +" x "+ fullHeight +", outerSize="+ outerSize +", pieceGap="+ pieceGap +", innerSize="+ innerSize);
        Log.w("r21 sizeCheck","picOSize="+ picOSize +", picISize="+ picISize +
                "base XY ="+baseX+" x "+ baseY);

        piece = new Piece(context, outerSize, pieceGap, innerSize);

    }
}