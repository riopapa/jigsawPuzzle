package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.jPosX;
import static com.riopapa.jigsawpuzzle.MainActivity.jPosY;
import static com.riopapa.jigsawpuzzle.MainActivity.jigC00R;
import static com.riopapa.jigsawpuzzle.MainActivity.jigC;
import static com.riopapa.jigsawpuzzle.MainActivity.jigR;
import static com.riopapa.jigsawpuzzle.MainActivity.picHSize;
import static com.riopapa.jigsawpuzzle.MainActivity.picISize;
import static com.riopapa.jigsawpuzzle.MainActivity.picOSize;
import static com.riopapa.jigsawpuzzle.MainActivity.paintView;
import static com.riopapa.jigsawpuzzle.MainActivity.piece;
import static com.riopapa.jigsawpuzzle.MainActivity.fullHeight;
import static com.riopapa.jigsawpuzzle.MainActivity.fullWidth;
import static com.riopapa.jigsawpuzzle.MainActivity.screenX;
import static com.riopapa.jigsawpuzzle.MainActivity.screenY;
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

    private static final float TOUCH_TOLERANCE = 10;
    int idxX, idxY, zwOff;
    static JigTable zNow;
    static Bitmap jigMap, mBitmap;
    public static ArrayList<Integer> inViewC, inViewR;
    Activity activity;
    TextView tvL, tvR;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity, TextView tvLeft, TextView tvRit){
        this.zwOff = picOSize - picISize;
        this.activity = activity;
        this.tvL = tvLeft;
        this.tvR = tvRit;
        inViewC = new ArrayList<>();
        inViewR = new ArrayList<>();

        mBitmap = Bitmap.createBitmap(fullWidth, fullHeight, Bitmap.Config.ARGB_8888);

    }
    public void load(JigTable[][] jigTables, int nbrX, int nbrY) {
        this.idxX = nbrX;
        this.idxY = nbrY;
        jPosX = screenX * 8f/10f;
        jPosY = screenY * 8f/10f;
        if (jigTables[nbrX][nbrY].src == null)
            piece.makeAll(nbrX, nbrY);
        zNow = jigTables[nbrX][nbrY];
        jigMap = Bitmap.createScaledBitmap(zNow.oLine, picOSize, picOSize, true);
    }

    protected void onDraw(Canvas canvas){
        Log.w("x4 paintview","on Draw jPos "+jPosX+" x "+jPosY);
        canvas.save();
        if (inViewR.size() > 0) {
            for (int cnt = 0; cnt < inViewR.size(); cnt++) {
                JigTable jt = jigTables[inViewC.get(cnt)][inViewR.get(cnt)];
                Bitmap bm = Bitmap.createScaledBitmap(jt.oLine, picOSize, picOSize, true);
                    canvas.drawBitmap(bm, jt.posX, jt.posY, null);
            }
        }
        canvas.restore();
        activity.runOnUiThread(() -> tvR.setText("onDraw "+jPosX + " x " + jPosY+"\n c" + jigC+" r"+jigR +" array="+inViewR.size()));

    }
    private void touchDown(float x, float y){
        jPosX = x;
        jPosY = y;
        jigMap = Bitmap.createScaledBitmap(zNow.oLine, picOSize, picOSize, true);
        Log.w("x8 paint view", "touchDown x y "+jPosX+" x "+jPosY);
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
        jigMap = Bitmap.createScaledBitmap(zNow.oLine, picOSize, picOSize, true);
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
            zNow = jigTables[jigC][jigR];
            jigMap = Bitmap.createScaledBitmap(zNow.src, picOSize, picOSize, true);

            Log.w("x1x PaintView_"+ jigC00R," drawing updateting "+jPosX+" x "+jPosY);
            paintView.invalidate();}
    };

}