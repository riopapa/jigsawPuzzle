package com.riopapa.zigsawpuzzle;

import static com.riopapa.zigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.zigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.zigsawpuzzle.MainActivity.jigPos;
import static com.riopapa.zigsawpuzzle.MainActivity.jigX;
import static com.riopapa.zigsawpuzzle.MainActivity.jigY;
import static com.riopapa.zigsawpuzzle.MainActivity.nw;
import static com.riopapa.zigsawpuzzle.MainActivity.paintView;
import static com.riopapa.zigsawpuzzle.MainActivity.piece;
import static com.riopapa.zigsawpuzzle.MainActivity.pieceBitmap;
import static com.riopapa.zigsawpuzzle.MainActivity.puzzleHeight;
import static com.riopapa.zigsawpuzzle.MainActivity.puzzleWidth;
import static com.riopapa.zigsawpuzzle.MainActivity.pw;
import static com.riopapa.zigsawpuzzle.MainActivity.screenX;
import static com.riopapa.zigsawpuzzle.MainActivity.screenY;
import static com.riopapa.zigsawpuzzle.MainActivity.x5;
import static com.riopapa.zigsawpuzzle.MainActivity.zigInfo;
import static com.riopapa.zigsawpuzzle.MainActivity.zw;

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

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 10;
    int idxX, idxY, zwOff;
    static ZigInfo zNow;
    static Bitmap zigMap, mBitmap;

    Activity activity;
    TextView tv;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity, TextView tv){
        this.zwOff = nw*2/3;
        this.activity = activity;
        this.tv = tv;

        mBitmap = Bitmap.createBitmap(puzzleWidth, puzzleHeight, Bitmap.Config.ARGB_8888);

    }
    public void load(ZigInfo[][] zigInfos, int nbrX, int nbrY) {
        this.idxX = nbrX;
        this.idxY = nbrY;
        jPosX = screenX * 8f/10f;
        jPosY = screenY * 8f/10f;
        if (zigInfos[nbrX][nbrY].src == null)
            pieceBitmap.make(nbrX, nbrY);
        zNow = zigInfos[nbrX][nbrY];
        zigMap = Bitmap.createScaledBitmap(zNow.src, nw, nw, true);
    }

    protected void onDraw(Canvas canvas){
        if (jPosX != -1) {
            canvas.save();
            canvas.drawBitmap(zigMap, jPosX - zwOff, jPosY - zwOff, null);
            canvas.restore();
            activity.runOnUiThread(() -> tv.setText(jPosX + " x " + jPosY));
        }
    }
    private void touchDown(float x, float y){
        jPosX = x;
        jPosY = y;
        zigMap = Bitmap.createScaledBitmap(piece.makeBig(zNow.oLine2), nw, nw, true);
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

    public final static Handler updateViewHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            zNow = zigInfo[jigX][jigY];
            zigMap = Bitmap.createScaledBitmap(zNow.src, nw, nw, true);

            Log.w("PaintView_"+jigPos," updateting "+jPosX+" x "+jPosY);
            paintView.invalidate();}
    };

}