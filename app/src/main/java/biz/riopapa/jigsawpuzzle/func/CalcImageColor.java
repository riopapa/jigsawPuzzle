package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorLocked;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.colorOutline;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.currImageMap;

import android.graphics.Color;
import android.util.Log;

public class CalcImageColor {
    public CalcImageColor() {
        long r = 0, g = 0, b = 0;
        if (currImageMap == null)
            Log.e("No", "chosenImageMap is null");

        for (int x = 0; x < currImageMap.getWidth(); x++) {
            for (int y = 0; y < currImageMap.getHeight(); y++) {
                int pixel = currImageMap.getPixel(x,y);
                r += Color.red(pixel);
                g += Color.green(pixel);
                b += Color.blue(pixel);
            }
        }
        long mx = Math.max(r, Math.max(g, b));
        r = 255 - 255 * r / mx;
        g = 255 - 255 * g / mx;
        b = 255 - 255 * b / mx;
        colorOutline = Color.rgb (r, g, b);
        colorLocked = 0xcF000000 | (0x00FFFFFF & ~colorLocked);

    }
}
