package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;

import android.graphics.Color;
import android.util.Log;

public class ImageChosen {
    public ImageChosen() {
        chosenImageWidth = chosenImageMap.getWidth();
        chosenImageHeight = chosenImageMap.getHeight();
        long r = 0, g = 0, b = 0;

        for (int x = 0; x < chosenImageMap.getWidth(); x++) {
            for (int y = 0; y < chosenImageMap.getHeight(); y++) {
                int pixel = chosenImageMap.getPixel(x,y);
                r += Color.red(pixel);
                g += Color.green(pixel);
                b += Color.blue(pixel);
            }
        }
        Log.w("ImageChosen", "r="+r+" g="+g+" b="+b);
        long mx = Math.max(r, Math.max(g, b));
        r = 255 - 255 * r / mx;
        g = 255 - 255 * g / mx;
        b = 255 - 255 * b / mx;
        chosenImageColor = Color.rgb (r, g, b);
        Log.w("ImageChosen", "r="+r+" g="+g+" b="+b);

    }
}
