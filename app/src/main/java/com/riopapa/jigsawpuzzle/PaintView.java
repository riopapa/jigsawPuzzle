package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.baseX;
import static com.riopapa.jigsawpuzzle.MainActivity.baseY;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.nowC;
import static com.riopapa.jigsawpuzzle.MainActivity.nowR;
import static com.riopapa.jigsawpuzzle.MainActivity.picGap;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.paintView;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.fullHeight;
import static com.riopapa.jigsawpuzzle.MainActivity.fullWidth;
import static com.riopapa.jigsawpuzzle.MainActivity.jigTables;
import static com.riopapa.jigsawpuzzle.MainActivity.showMax;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetC;
import static com.riopapa.jigsawpuzzle.MainActivity.offsetR;

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
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PaintView extends View {

    private static final float TOUCH_TOLERANCE = 5;
    static JigTable nowJig;
    public int nowIdx;
    static Bitmap mBitmap;
    public static ArrayList<FloatPiece> fPs;
    public static int calcC, calcR;
    private static boolean selected, dragging;
    Activity activity;
    TextView tvL, tvR;
    Paint pGrayed = new Paint();


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
                        if (jigTables[c][r].oLine == null)
                            piece.makeAll(c, r);
                    if (jigTables[c][r].locked)
                        canvas.drawBitmap(jigTables[c][r].oLine,
                                baseX + c * picISize, baseY + r * picISize, null);
                        else
                        canvas.drawBitmap(jigTables[c][r].oLine,
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
                    canvas.drawBitmap(fP.bitmap, jt.posX, jt.posY, null);
            } else {    // timer active to show bright puzzle
                if (System.currentTimeMillis() < fP.time) {
                    canvas.drawBitmap(fP.brightMap, jt.posX, jt.posY, null);
                } else {
                    canvas.drawBitmap(fP.bitmap, jt.posX, jt.posY, null);
                    fPs.remove(cnt);
                    invalidate();
                }
            }
        }

        canvas.restore();
        activity.runOnUiThread(() -> tvR.setText("onDraw "+jPosX + " x " + jPosY+"\n c" + nowC +" r"+ nowR));

    }
    private void touchDown(float fX, float fY){

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
                Log.w("x8 pGrayed view c="+ nowC +" r="+ nowR, " x y "+jPosX+" x "+jPosY);
                break;
            }
        }
    }

    private boolean isPieceSelected(JigTable jt, int x, int y) {
        return jt.posX < x && x < (jt.posX + picOSize) &&
                jt.posY < y && y < (jt.posY + picOSize);
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

            if (checkIfPositioned() && !jigTables[nowC][nowR].locked) {
                jigTables[nowC][nowR].locked = true;
                FloatPiece fp = fPs.get(nowIdx);
                fp.brightMap = piece.makeBright(fp.bitmap);
                fp.time = System.currentTimeMillis() + 500;
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        invalidate();
                    }
                }, 600);
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        invalidate();
                    }
                }, 900);
            }
            return true;
        }
        return false;
    }

    private boolean checkIfPositioned() {
        // todo : check if right position

        calcC = (jPosX - baseX - picHSize) / picISize + offsetC;
        calcR = (jPosY - baseY - picHSize) / picISize + offsetR;
        int xR = baseX + calcC * picISize + picISize;
        int xL = xR - picGap;
        int yB = baseY + calcR * picISize + picISize;
        int yT = yB - picGap;
//        String txt = " jx=" + jPosX + " jy=" + jPosY + "\n calc " + xL + " vs " + xR +
//                "\n R " + yT + " vs " + yB;
//        activity.runOnUiThread(() -> tvL.setText(txt));
        if (calcC != nowC || calcR != nowR)
            return false;
        if (jPosX < xL || jPosX > xR)
            return false;
        if (jPosY < yT || jPosY > yB)
            return false;
//        int x = jPosX % picISize; jPosX = jPosX - x * picISize;
//        int y = jPosY % picISize; jPosY = jPosX - y * picISize;
        return true;
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