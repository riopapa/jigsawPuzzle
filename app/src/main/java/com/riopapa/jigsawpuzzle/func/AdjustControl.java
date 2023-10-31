package com.riopapa.jigsawpuzzle.func;

import com.riopapa.jigsawpuzzle.databinding.ActivityMainBinding;

public class AdjustControl {
    public AdjustControl(ActivityMainBinding binding, int sizeLong) {
        int sizeShort = sizeLong / 4;
        binding.moveRight.getLayoutParams().width = sizeShort;
        binding.moveRight.getLayoutParams().height = sizeLong;
        binding.moveLeft.getLayoutParams().width = sizeShort;
        binding.moveLeft.getLayoutParams().height = sizeLong;
        binding.moveUp.getLayoutParams().width = sizeLong;
        binding.moveUp.getLayoutParams().height = sizeShort;
        binding.moveDown.getLayoutParams().width = sizeLong;
        binding.moveDown.getLayoutParams().height = sizeShort;
        binding.thumbnail.getLayoutParams().width = sizeLong;
        binding.thumbnail.getLayoutParams().height = sizeLong;
        binding.moveThumbnail.getLayoutParams().width = sizeLong * 120/80;
        binding.moveThumbnail.getLayoutParams().height = sizeLong * 120/80;
    }

}
