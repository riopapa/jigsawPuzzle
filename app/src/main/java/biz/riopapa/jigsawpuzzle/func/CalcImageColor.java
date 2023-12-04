package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageColor;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;

import android.graphics.Color;
import android.util.Log;

public class CalcImageColor {
    public CalcImageColor() {
//        chosenImageWidth = chosenImageMap.getWidth();
//        chosenImageHeight = chosenImageMap.getHeight();
        long r = 0, g = 0, b = 0;
        if (chosenImageMap == null)
            Log.e("No", "chosenImageMap is null");

        for (int x = 0; x < chosenImageMap.getWidth(); x++) {
            for (int y = 0; y < chosenImageMap.getHeight(); y++) {
                int pixel = chosenImageMap.getPixel(x,y);
                r += Color.red(pixel);
                g += Color.green(pixel);
                b += Color.blue(pixel);
            }
        }
        long mx = Math.max(r, Math.max(g, b));
        r = 255 - 255 * r / mx;
        g = 255 - 255 * g / mx;
        b = 255 - 255 * b / mx;
//        r = 255 * r / mx;
//        g = 255 * g / mx;
//        b = 255 * b / mx;
        chosenImageColor = Color.rgb (r, g, b);

    }
}
