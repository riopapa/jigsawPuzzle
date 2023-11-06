package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.imageAnswer;
import static com.riopapa.jigsawpuzzle.Vars.picGap;
import static com.riopapa.jigsawpuzzle.Vars.picISize;
import static com.riopapa.jigsawpuzzle.Vars.showMaxX;
import static com.riopapa.jigsawpuzzle.Vars.showMaxY;

import androidx.constraintlayout.widget.ConstraintLayout;

public class AdjustThumbNail {

    public AdjustThumbNail() {
        // imageAnswer (will be removed later
        ConstraintLayout.LayoutParams layoutImage = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutImage.setMargins(picGap, picGap, picGap, picGap);
        layoutImage.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutImage.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutImage.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutImage.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutImage.width = showMaxX * picISize * 2;
        layoutImage.height = showMaxY * picISize * 2;
        imageAnswer.setLayoutParams(layoutImage);

//        int thumbSize = screenX/5;
//        if (thumbSize > 0)
//            return;
//        int thumbHeight = thumbSize * fullImage.getHeight() / fullWidth;
//        int moveWidth = thumbSize / 5;
//
//        // thumbnail
//        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//        lp.width = thumbSize;
//        lp.height = thumbHeight;
//        lp.rightToLeft = moveR.getId();
//        lp.leftToRight = moveL.getId();
//        lp.topToBottom = moveU.getId();
//        lp.bottomToTop = moveD.getId();
//        thumbNail.setLayoutParams(lp);
//
//        // move right
//        ConstraintLayout.LayoutParams lpR = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//        lpR.width = moveWidth;
//        lpR.height = thumbSize;
//        lpR.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
//        lpR.leftToRight = thumbNail.getId();
//        lpR.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
//        lpR.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
//        moveR.setLayoutParams(lpR);
//
//        // move left
//        ConstraintLayout.LayoutParams lpL = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//        lpL.width = moveWidth;
//        lpL.height = thumbSize;
//        lpL.rightToLeft = thumbNail.getId();
//        lpL.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
//        lpL.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
//        moveL.setLayoutParams(lpL);
//
//        // move up
//        ConstraintLayout.LayoutParams lpU = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//        lpU.width = thumbSize;
//        lpU.height = moveWidth;
//        lpU.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
//        lpU.bottomToTop = thumbNail.getId();
//        lpU.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
//        lpU.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
//        moveU.setLayoutParams(lpU);
//
//        // move down
//        ConstraintLayout.LayoutParams lpD = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//        lpD.width = thumbSize;
//        lpD.height = moveWidth;
//        lpD.topToBottom = thumbNail.getId();
//        lpU.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
//        lpU.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
//        moveR.setLayoutParams(lpD);

    }

}
