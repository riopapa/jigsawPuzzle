package com.riopapa.zigsawpuzzle;

import static com.riopapa.zigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.zigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.zigsawpuzzle.MainActivity.jigX00Y;
import static com.riopapa.zigsawpuzzle.MainActivity.jigX;
import static com.riopapa.zigsawpuzzle.MainActivity.jigY;
import static com.riopapa.zigsawpuzzle.MainActivity.picSize;
import static com.riopapa.zigsawpuzzle.MainActivity.paintView;
import static com.riopapa.zigsawpuzzle.MainActivity.piece;
import static com.riopapa.zigsawpuzzle.MainActivity.fullHeight;
import static com.riopapa.zigsawpuzzle.MainActivity.fullWidth;
import static com.riopapa.zigsawpuzzle.MainActivity.screenX;
import static com.riopapa.zigsawpuzzle.MainActivity.screenY;
import static com.riopapa.zigsawpuzzle.MainActivity.jigTables;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.riopapa.zigsawpuzzle.model.JigTable;

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 10;
    int idxX, idxY, zwOff;
    static JigTable zNow;
    static Bitmap jigMap, mBitmap;

    Activity activity;
    TextView tv;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity, TextView tv){
        this.zwOff = picSize * 2/3;
        this.activity = activity;
        this.tv = tv;

        mBitmap = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_8888);

    }
    public void load(JigTable[][] jigTables, int nbrX, int nbrY) {
        this.idxX = nbrX;
        this.idxY = nbrY;
        jPosX = screenX * 8f/10f;
        jPosY = screenY * 8f/10f;
        if (jigTables[nbrX][nbrY].src == null)
            piece.make(nbrX, nbrY);
        zNow = jigTables[nbrX][nbrY];
        jigMap = Bitmap.createScaledBitmap(zNow.oLine, picSize, picSize, true);
    }

    protected void onDraw(Canvas canvas){
        if (jPosX != -1) {
            canvas.save();
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    JigTable jt = jigTables[x+2][y+2];
                    if (jt.oLine == null) {
                        piece.make(x+2, y+2);
                        jt = jigTables[x+2][y+2];
                    }
                    Bitmap bm = Bitmap.createScaledBitmap(jt.oLine, picSize, picSize, true);
                    canvas.drawBitmap(bm, x*(picSize+10), y*(picSize+10), null);

                }
            }
            Log.w("onDraw", " x "+jigTables.length);
            canvas.drawBitmap(jigMap, jPosX - zwOff, jPosY - zwOff, null);
            canvas.restore();
            activity.runOnUiThread(() -> tv.setText(jPosX + " x " + jPosY+" "+ jigX00Y));
        }

    }
    private void touchDown(float x, float y){
        jPosX = x;
        jPosY = y;
        jigMap = Bitmap.createScaledBitmap(zNow.oLine, picSize, picSize, true);
    }
    private boolean touchMove(float x, float y){
        float dx = Math.abs(x - jPosX);
        float dy = Math.abs(y - jPosY);

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            jPosX = x;
            jPosY = y;
            return true;
        }
        return false;
    }
    private void touchUp(){
        jigMap = Bitmap.createScaledBitmap(zNow.oLine, picSize, picSize, true);
    }
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchDown(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchMove(x, y))
                    invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }

        return true;
    }

    public final static Handler updateViewHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            zNow = jigTables[jigX][jigY];
            jigMap = Bitmap.createScaledBitmap(zNow.src, picSize, picSize, true);

            Log.w("PaintView_"+ jigX00Y," updateting "+jPosX+" x "+jPosY);
            paintView.invalidate();}
    };

}