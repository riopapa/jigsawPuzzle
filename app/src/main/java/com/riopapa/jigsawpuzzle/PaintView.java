package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
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
import java.util.Timer;
import java.util.TimerTask;

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 5;
    static JigTable nowJig;
    public int nowIdx;
    static Bitmap mBitmap;
    public static ArrayList<Integer> inViewC, inViewR;
    public static ArrayList<Bitmap> inViewMap;
    public static int offsetC, offsetR, calcC, calcR;

    private static boolean dragging, selected;
    Activity activity;
    TextView tvL, tvR;
    long positionedTime;

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
        offsetC = 0; offsetR = 0;


    }

    //  screenXY 1080 x 2316
    //  image 11232 x 7488, outerSize=802, pieceGap=167, innerSize=468
    // picOSize=178, picISize=103 base XY =32 x 537
    // left top corner 121 x 624 ( 85 x  92)

    protected void onDraw(Canvas canvas){
        canvas.save();
//        if (inViewR.size() == 0) {
            for (int c = 0; c < pieceMax; c++) {
                for (int r = 0; r < pieceMax; r++) {
                    if (jigTables[c][r].locked) {
                        if (jigTables[c][r].oLine == null)
                            piece.makeAll(c, r);

                    canvas.drawBitmap(jigTables[c][r].oLine,
                            baseX+c*picISize, baseY+r*picISize, null);
//                        canvas.drawBitmap(piece.makeBright(jigTables[c][r].oLine),
//                                baseX + c * picISize, baseY + r * picISize, null);
                    }
                }
            }
//      }

        for (int cnt = 0; cnt < inViewR.size(); cnt++) {
            int c = inViewC.get(cnt);
            int r = inViewR.get(cnt);
            JigTable jt = jigTables[c][r];
            if (nowC == c && nowR == r && jt.locked && System.currentTimeMillis() < positionedTime) {
                canvas.drawBitmap(piece.makeBright(inViewMap.get(cnt)), jt.posX, jt.posY, null);

            } else if (dragging && selected && c == nowC && r == nowR) {
                canvas.drawBitmap(piece.makeBigger(inViewMap.get(cnt)), jt.posX, jt.posY, null);
            } else
                canvas.drawBitmap(inViewMap.get(cnt), jt.posX, jt.posY, null);
        }

        canvas.restore();
        activity.runOnUiThread(() -> tvR.setText("onDraw "+jPosX + " x " + jPosY+"\n c" + nowC +" r"+ nowR +" array="+inViewR.size()));

    }
    private void touchDown(float fX, float fY){

        int x = (int) fX;
        int y = (int) fY;
        dragging = true;
        selected = false;
        for (int i = 0; i < inViewR.size(); i++) {
            int r = inViewR.get(i);
            int c = inViewC.get(i);
            JigTable jt = jigTables[c][r];
            if (isPieceSelected(jt, x, y)) {
                nowR = r; nowC = c;
                nowIdx = i;
                nowJig = jigTables[c][r];
                jPosX = x; jPosY = y;
                selected = true;
                Log.w("x8 paint view c="+ nowC +" r="+ nowR, " x y "+jPosX+" x "+jPosY);
                break;
            }
        }
    }

    private boolean isPieceSelected(JigTable jt, int x, int y) {
        if (jt.posX < x && x < (jt.posX + picOSize) &&
                jt.posY < y && y < (jt.posY + picOSize))
            return true;
        return false;
    }
    private boolean touchMove(float fX, float fY){
        if (!selected)
            return false;
        float dx = Math.abs(fX - jPosX);
        float dy = Math.abs(fY - jPosY);

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            jPosX = (int) fX;
            jPosY = (int) fY;
            jigTables[nowC][nowR].posX = (int) jPosX - picHSize;
            jigTables[nowC][nowR].posY = (int) jPosY - picHSize;
            String txt;
            if (checkIfPositioned() && !jigTables[nowC][nowR].locked) {
                jigTables[nowC][nowR].locked = true;
                inViewC.remove(nowIdx); inViewR.remove(nowIdx);
                inViewMap.remove(nowIdx);
                positionedTime = System.currentTimeMillis() + 1000;
                 txt = "Positioned " + jPosX + " x " + jPosY + "\n c" + nowC + " r" + nowR;
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        positionedTime = System.currentTimeMillis() - 500;
                        paintView.invalidate();
                    }
                }, 400);
            } else
                txt = "table c " + calcC +" r "+ calcR + "\nnow c" + nowC +" r"+ nowR;
            activity.runOnUiThread(() -> tvL.setText(txt));
            return true;
        }
        return false;
    }

    private boolean checkIfPositioned() {
        // todo : check if right position

        calcC = (jPosX - baseX - picHSize) / picISize + offsetC;
        calcR = (jPosY - baseY - picHSize) / picISize + offsetR;
        if (calcC == nowC && calcR == nowR) {
            return true;
        }
        return false;
    }
    private void touchUp(){
        dragging = false;
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
//            nowJig = jigTables[nowC][nowR];
//            mapNow = Bitmap.createScaledBitmap(nowJig.src, picOSize, picOSize, true);
//            dragging = true;
//            Log.w("p1x "+ jigCR," call by recycler drawing updateting "+jPosX+" x "+jPosY);
            paintView.invalidate();}
    };

}