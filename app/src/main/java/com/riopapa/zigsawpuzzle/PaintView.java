package com.riopapa.zigsawpuzzle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 6;
    private float mX, mY;
    int gHeight, gWidth;
    int idxX, idxY;
    int zw, zwhf, x5, pw, nw;
    ZigInfo zNow;
    Bitmap zigMap, mBitmap;

    Activity activity;
    TextView tv;

    Piece piece;
    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(int gWidth, int gHeight, int zw, int x5, int pw, int nw, Activity activity, TextView tv){
        this.gWidth = gWidth; this.gHeight = gHeight;
        this.zw = zw; this.zwhf = nw/2; this.x5 = x5; this.pw = pw; this.nw = nw;
        this.activity = activity;
        this.tv = tv;

        piece = new Piece(zw, x5, pw);
        mBitmap = Bitmap.createBitmap(gWidth, gHeight, Bitmap.Config.ARGB_8888);

    }
    public void load(ZigInfo[][] zigInfos, int nbrX, int nbrY) {
        this.idxX = nbrX;
        this.idxY = nbrY;
        zNow = zigInfos[nbrX][nbrY];
        mX = gWidth * 9/10;
        mY = gHeight * 9/10;
        zigMap = Bitmap.createScaledBitmap(zNow.src, nw, nw, true);
    }

    protected void onDraw(Canvas canvas){
        canvas.save();
        canvas.drawBitmap(zigMap, mX- zwhf, mY- zwhf, null);
        canvas.restore();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(mX+" x "+mY);
            }
        });
    }
    private void touchDown(float x, float y){
        mX = x;
        mY = y;
        zigMap = Bitmap.createScaledBitmap(piece.makeBig(zNow.oLine2), nw, nw, true);
    }
    private boolean touchMove(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            mX = x;
            mY = y;
            return true;
        }
        return false;
    }
    private void touchUp(){
        zigMap = Bitmap.createScaledBitmap(zNow.src, nw, nw, true);
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
}