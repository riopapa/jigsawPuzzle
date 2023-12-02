package biz.riopapa.jigsawpuzzle.func;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class Drawable2bitmap {

    /**
     * converts drawable resources into oLine;
     * @param xySize : piece outer size (one time only)
     * @param drawableId
     * @return bitmap
     **/

    Context context;
    int xySize;
    public Drawable2bitmap (Context context, int size) {
        this.context = context;
        xySize = size;
    }
    public Bitmap make(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(xySize, xySize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, xySize, xySize);
        drawable.draw(canvas);

        return bitmap;
    }

}
