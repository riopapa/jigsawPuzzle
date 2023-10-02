package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.fullHeight;
import static com.riopapa.jigsawpuzzle.MainActivity.fullWidth;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetC;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetR;
import static com.riopapa.jigsawpuzzle.MainActivity.paintView;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.showMax;
import static com.riopapa.jigsawpuzzle.MainActivity.tvRight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.riopapa.jigsawpuzzle.func.CheckPosition;
import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 20;
    static JigTable nowJig;
    public int nowIdx;
    static Bitmap mBitmap;
    public static ArrayList<FloatPiece> fPs;
    public static int calcC, calcR;
    private static boolean selected;
    public static boolean dragging;
    Activity activity;
    Paint pGrayed = new Paint();
    CheckPosition checkPosition = new CheckPosition();



    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity){
        this.activity = activity;
        fPs = new ArrayList<>();
        dragging = false;
        mBitmap = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_8888);
        pGrayed.setAlpha(40);

    }

    //  screenXY 1080 x 2316
    //  image 11232 x 7488, outerSize=802, pieceGap=167, innerSize=468
    // picOSize=178, picISize=103 base XY =32 x 537
    // left top corner 121 x 624 ( 85 x  92)

    protected void onDraw(Canvas canvas){
        canvas.save();
            for (int c = 0; c < showMax; c++) {
                for (int r = 0; r < showMax; r++) {
                    final int cc = c+offsetC; final int rr = r+offsetR;
                        if (jigTables[cc][rr].oLine == null)
                            piece.makeAll(c+offsetC, r+offsetR);
                    if (jigTables[cc][rr].locked) {
                        if (jigTables[cc][rr].lockedTime == 0)
                            canvas.drawBitmap(jigTables[cc][rr].oLine,
                                baseX + c * picISize, baseY + r * picISize, null);
                        else if (jigTables[cc][rr].lockedTime > System.currentTimeMillis())
                            canvas.drawBitmap(jigTables[cc][rr].picSel,
                                    baseX + c * picISize, baseY + r * picISize, null);
                        else {
                            jigTables[cc][rr].lockedTime = 0;
                        }
                    } else
                        canvas.drawBitmap(jigTables[cc][rr].oLine,
                            baseX + c * picISize, baseY + r * picISize, pGrayed);
                }
            }

        for (int cnt = 0; cnt < fPs.size(); cnt++) {
            FloatPiece fP = fPs.get(cnt);
            int c = fP.C;
            int r = fP.R;
            JigTable jt = jigTables[c][r];
            if (fP.time == 0) { // time == 0 means normal piece
                if (dragging && selected && c == nowC && r == nowR) {
                    canvas.drawBitmap(fP.bigMap, jt.posX, jt.posY, null);
                } else
                    canvas.drawBitmap(fP.oLine, jt.posX, jt.posY, null);
            } else {    // timer active to show bright puzzle
                if (System.currentTimeMillis() < fP.time) {
                    canvas.drawBitmap(fP.brightMap, jt.posX, jt.posY, null);
                } else {
                    canvas.drawBitmap(fP.oLine, jt.posX, jt.posY, null);
                    fPs.remove(cnt);
//                    invalidate();
                }
            }
        }

        canvas.restore();
        String txt = "onD c" + nowC +" r"+ nowR + "\noffCR "+offsetC + " x " + offsetR+"\n calc " + calcC +" x "+ calcR+"\n fPs "+fPs.size();
        activity.runOnUiThread(() -> tvRight.setText(txt));

    }
    private void paintToucnDown(float fX, float fY){

        int iX = (int) fX;
        int iY = (int) fY;
        dragging = true;
        selected = false;
        for (int i = 0; i < fPs.size(); i++) {
            int c = fPs.get(i).C;
            int r = fPs.get(i).R;
            JigTable jt = jigTables[c][r];
            if (isPieceSelected(jt, iX, iY)) {
                nowR = r; nowC = c;
                nowIdx = i;
                nowJig = jigTables[c][r];
                jPosX = iX; jPosY = iY;
                selected = true;
                Log.w("x8 view c="+ nowC +" r="+ nowR, " x y "+jPosX+" x "+jPosY);
                break;
            }
        }
    }

    private boolean isPieceSelected(JigTable jt, int x, int y) {
        return jt.posX < x && x < (jt.posX + picOSize) &&
                jt.posY < y && y < (jt.posY + picOSize);
    }
    private boolean paintTouchMove(float fX, float fY){
        if (!selected)
            return false;
        float dx = Math.abs(fX - jPosX);
        float dy = Math.abs(fY - jPosY);

        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            jPosX = (int) fX;
            jPosY = (int) fY;
            jigTables[nowC][nowR].posX = jPosX - picISize;
            jigTables[nowC][nowR].posY = jPosY - picISize;

            if (checkPosition.isHere(activity) && !jigTables[nowC][nowR].locked) {
                jigTables[nowC][nowR].locked = true;
                jigTables[nowC][nowR].lockedTime = System.currentTimeMillis() + 1000;
            }
            return true;
        }
        return false;
    }

    private void paintTouchUp(){
        dragging = false;
        Log.w("p83"," touchUp");
    }

    long touchTime = 0, tempTime;
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        tempTime = System.currentTimeMillis() + 200;
        if (touchTime < tempTime)
            return true;
        touchTime = tempTime;
        Log.w("px on TouchEvent", "time="+touchTime);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                paintToucnDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                paintTouchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                paintTouchUp();
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