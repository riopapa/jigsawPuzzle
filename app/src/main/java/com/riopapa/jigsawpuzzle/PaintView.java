package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigC;
import static com.riopapa.jigsawpuzzle.MainActivity.jigR;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.paintView;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.fullHeight;
import static com.riopapa.jigsawpuzzle.MainActivity.fullWidth;
import static com.riopapa.jigsawpuzzle.MainActivity.pieceMax;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;

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

import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 5;
    static JigTable jigNow;
    static Bitmap mapNow, mBitmap;
    public static ArrayList<Integer> inViewC, inViewR;
    public static ArrayList<Bitmap> inViewMap;

    private static boolean dragging;
    Activity activity;
    TextView tvL, tvR;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity, TextView tvLeft, TextView tvRit){
        this.activity = activity;
        this.tvL = tvLeft;
        this.tvR = tvRit;
        inViewC = new ArrayList<>();
        inViewR = new ArrayList<>();
        inViewMap = new ArrayList<>();
        dragging = false;
        mBitmap = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_8888);

    }
//    public void load(JigTable[][] jigTables, int nbrX, int nbrY) {
//        this.idxX = nbrX;
//        this.idxY = nbrY;
//        jPosX = screenX * 8f/10f;
//        jPosY = screenY * 8f/10f;
//        if (jigTables[nbrX][nbrY].src == null)
//            piece.makeAll(nbrX, nbrY);
//        jigNow = jigTables[nbrX][nbrY];
//        mapNow = Bitmap.createScaledBitmap(jigNow.oLine, picOSize, picOSize, true);
//    }

    //  screenXY 1080 x 2316
    //  image 11232 x 7488, outerSize=802, pieceGap=167, innerSize=468
    // picOSize=178, picISize=103base XY =32 x 537
    // left top corner 117 x 629 ( 85 x  92)

    protected void onDraw(Canvas canvas){
        Log.w("p4 paintview","on Draw jPos "+jPosX+" x "+jPosY);
        canvas.save();
//        if (inViewR.size() == 0) {
            for (int c = 0; c < pieceMax; c++) {
                for (int r = 0; r < pieceMax; r++) {
                    if (jigTables[c][r].oLine == null)
                        piece.makeAll(c,r);
                    canvas.drawBitmap(jigTables[c][r].oLine,
                            baseX+c*picISize, baseY+r*picISize, null);
                }
            }
//      }

        for (int cnt = 0; cnt < inViewR.size(); cnt++) {
            int c = inViewC.get(cnt);
            int r = inViewR.get(cnt);
            JigTable jt = jigTables[c][r];
            if (dragging && c == jigC && r == jigR) {
                canvas.drawBitmap(piece.makeBigger(inViewMap.get(cnt)), jt.posX, jt.posY, null);
            } else
                canvas.drawBitmap(inViewMap.get(cnt), jt.posX, jt.posY, null);
        }

        canvas.restore();
        activity.runOnUiThread(() -> tvR.setText("onDraw "+jPosX + " x " + jPosY+"\n c" + jigC+" r"+jigR +" array="+inViewR.size()));

    }
    private void touchDown(float x, float y){

        dragging = true;
        Log.w("Puzzle","touchDown at = "+x+" x "+y);
        jPosX = -999;
        for (int i = 0; i < inViewR.size(); i++) {
            int r = inViewR.get(i);
            int c = inViewC.get(i);
            JigTable jt = jigTables[c][r];
            if (isPieceSelected(jt, x, y)) {
                jigR = r; jigC = c;
                jigNow = jigTables[c][r];
                jPosX = x; jPosY = y;
                break;
            }
        }
        Log.w("x8 paint view", "touchDown x y "+jPosX+" x "+jPosY);
    }

    private boolean isPieceSelected(JigTable jt, float x, float y) {
        if (jt.posX < x  && x < (jt.posX + picOSize) &&
                jt.posY < y  && y < (jt.posY + picOSize))
            return true;
        return false;
    }
    private boolean touchMove(float x, float y){
        if (jPosX == -999)
            return false;
        float dx = Math.abs(x - jPosX);
        float dy = Math.abs(y - jPosY);

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            jPosX = x;
            jPosY = y;
            jigTables[jigC][jigR].posX = (int) jPosX - picHSize;
            jigTables[jigC][jigR].posY = (int) jPosY - picHSize;
            return true;
        }
        return false;
    }
    private void touchUp(){
        dragging = false;
        // todo : check if right position
        //        mapNow = Bitmap.createScaledBitmap(jigNow.src, picOSize, picOSize, true);
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
//            jigNow = jigTables[jigC][jigR];
//            mapNow = Bitmap.createScaledBitmap(jigNow.src, picOSize, picOSize, true);
//            dragging = true;
//            Log.w("p1x "+ jigCR," call by recycler drawing updateting "+jPosX+" x "+jPosY);
            paintView.invalidate();}
    };

}