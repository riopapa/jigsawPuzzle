package biz.riopapa.jigsawpuzzle.images;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MakeDark {
    public Bitmap make(Bitmap pic) {
        int wSz = pic.getWidth();
        int hSz = pic.getHeight();
        Bitmap outMap = Bitmap.createBitmap(wSz, hSz, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outMap);
        Paint p = new Paint();
        for (int c = 0; c < wSz; c++) {
            for (int r = 0; r < hSz; r++) {
                int pxl = pic.getPixel(c, r);
                if (pxl != 0) {
                    int avr = (Color.red(pxl) + Color.green(pxl) + Color.blue(pxl)) / 3;
                    if (avr > 30) {
                        int color = 0xFF000000 | avr << 16 | avr << 8 | avr;
                        p.setColor(color);
                        canvas.drawPoint(c, r, p);
                    }
                }
            }
        }
        return outMap;
    }

}
