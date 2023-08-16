package com.riopapa.zigsawpuzzle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 6;
    private float mX, mY;
    private Bitmap mBitmap;
    int gHeight, gWidth;
    int idxX, idxY;
    int zw, zwh, x5, pw;
    ZigInfo zNow;
    Bitmap zigMap;

    Activity activity;
    TextView tv;

    Piece piece;
    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(DisplayMetrics metrics, int zw, int x5, int pw, Activity activity, TextView tv){
        gHeight = metrics.heightPixels;
        gWidth = metrics.widthPixels;
        this.zw = zw; this.zwh = zw/2; this.x5 = x5; this.pw = pw;
        this.activity = activity;
        this.tv = tv;

        piece = new Piece(zw, x5, pw);
        mBitmap = Bitmap.createBitmap(gWidth, gHeight, Bitmap.Config.ARGB_8888);

    }
    public void load(ZigInfo[][] zigInfos, int nbrX, int nbrY) {
        this.idxX = nbrX;
        this.idxY = nbrY;
        zNow = zigInfos[nbrX][nbrY];
        zigMap = zNow.src;
    }

    protected void onDraw(Canvas canvas){
        canvas.save();
        canvas.drawBitmap(zigMap, mX-zwh, mY-zwh, null);
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
        zigMap = piece.makeBig(zNow.oLine2);
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
        zigMap = zNow.oLine;
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