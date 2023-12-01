package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;

public class AdjustControl {
    public AdjustControl(ActivityJigsawBinding binding) {

        int sizeHeight = gVal.picISize * 20 / 10;
        if (sizeHeight > gVal.baseY * 6 / 10)
            sizeHeight = gVal.baseY * 6 / 10;
        int sizeWidth = sizeHeight * chosenImageWidth / chosenImageHeight;

        int gap = (sizeHeight > sizeWidth) ? sizeWidth /4: sizeHeight / 4;

        binding.moveRight.getLayoutParams().width = gap;
        binding.moveRight.getLayoutParams().height = sizeHeight;
        binding.moveLeft.getLayoutParams().width = gap;
        binding.moveLeft.getLayoutParams().height = sizeHeight;
        binding.moveUp.getLayoutParams().width = sizeWidth;
        binding.moveUp.getLayoutParams().height = gap;
        binding.moveDown.getLayoutParams().width = sizeWidth;
        binding.moveDown.getLayoutParams().height = gap;
        binding.thumbnail.getLayoutParams().width = sizeWidth;
        binding.thumbnail.getLayoutParams().height = sizeHeight;
        binding.moveThumbnail.getLayoutParams().width = (sizeWidth + gap + gap);
        binding.moveThumbnail.getLayoutParams().height = (sizeHeight + gap + gap);
    }

}
