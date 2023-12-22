package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static biz.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static biz.riopapa.jigsawpuzzle.ActivityMain.INVALIDATE_INTERVAL;
import static biz.riopapa.jigsawpuzzle.ActivityMain.appVersion;
import static biz.riopapa.jigsawpuzzle.ActivityMain.backColor;
import static biz.riopapa.jigsawpuzzle.ActivityMain.congrats;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGame;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGameLevel;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currLevel;
import static biz.riopapa.jigsawpuzzle.ActivityMain.debugMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.histories;
import static biz.riopapa.jigsawpuzzle.ActivityMain.jigDones;
import static biz.riopapa.jigsawpuzzle.ActivityMain.levelNames;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBack;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBackCount;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBackLoop;
import static biz.riopapa.jigsawpuzzle.ActivityMain.sound;
import static biz.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;
import static biz.riopapa.jigsawpuzzle.ActivityMain.vibrate;
import static biz.riopapa.jigsawpuzzle.ForeView.backBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.foreBlink;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import biz.riopapa.jigsawpuzzle.adaptors.JigsawAdapter;
import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import biz.riopapa.jigsawpuzzle.func.DefineControlButton;
import biz.riopapa.jigsawpuzzle.images.Congrat;
import biz.riopapa.jigsawpuzzle.images.FireWork;
import biz.riopapa.jigsawpuzzle.func.GValGetPut;
import biz.riopapa.jigsawpuzzle.func.HistoryGetPut;
import biz.riopapa.jigsawpuzzle.images.JigDone;
import biz.riopapa.jigsawpuzzle.images.Masks;
import biz.riopapa.jigsawpuzzle.images.ShowThumbnail;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.History;

public class ActivityJigsaw extends Activity {

    ActivityJigsawBinding binding;

    PieceImage pieceImage;

    public static RecyclerView jigRecyclerView;

    public static ForeView foreView;

    public static BackView backView;

    public static JigsawAdapter activeAdapter;

    public static boolean doNotUpdate; // wait while one action completed

    public static Bitmap chosenImageMap, areaMap;
    public static int chosenImageWidth, chosenImageHeight, chosenImageColor; // puzzle photo size (in dpi)
    public static Bitmap [][] jigPic, jigOLine, jigWhite, jigGray;
    public static int activePos; // jigsaw slide x, y count
    public static int nowC, nowR, nowCR;   // fullImage pieceImage array column, row , x*10000+y
    public static int dragX, dragY; // absolute x,y rightPosition drawing current jigsaw
    public static History history;
    public static int historyIdx;
    public static int allLockedMode = 0;    // 10: just all locked, 20: after all locked 99: completed
    public static int congCount = 0;

    int [] eyes = {R.drawable.z_eye_open, R.drawable.z_eye_half, R.drawable.z_eye_closed};
    ShowThumbnail showThumbnail;
    public static Timer loopTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJigsawBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Log.w("jSaw","onCreate gameMode="+gameMode);
        screenBottom = screenY - gVal.recSize - gVal.recSize;
        if (fPhoneInchX > 3f)
            screenBottom += gVal.picHSize;

        jigPic = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigOLine = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigWhite = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigGray = new Bitmap[gVal.colNbr][gVal.rowNbr];

        srcMaskMaps = new Masks(this).make(mContext, gVal.imgOutSize);
        outMaskMaps = new Masks(this).makeOut(mContext, gVal.imgOutSize);
        fireWorks = new FireWork().make(gVal.picOSize + gVal.picGap + gVal.picGap);
        congrats = new Congrat().make(screenX * 7/10);
        jigDones = new JigDone().make(screenX * 7/10);

        foreView = findViewById(R.id.paint_view);
        binding.paintView.getLayoutParams().height = screenBottom;

        backView = findViewById(R.id.back_view);
        binding.backView.getLayoutParams().height = screenBottom;

        showThumbnail = new ShowThumbnail();

        binding.moveLeft.setOnClickListener(v -> {
            gVal.offsetC -= gVal.showShiftX;
            if (gVal.offsetC < 0 || gVal.offsetC == 1)
                gVal.offsetC = 0;
            copy2RecyclerPieces();
        });
        binding.moveRight.setOnClickListener(v -> {
            gVal.offsetC += gVal.showShiftX;
            if (gVal.offsetC >= gVal.colNbr - gVal.showMaxX)
                gVal.offsetC = gVal.colNbr - gVal.showMaxX;
            if (gVal.offsetC + gVal.showMaxX == gVal.colNbr - 1)
                gVal.offsetC = gVal.colNbr - gVal.showMaxX;
            copy2RecyclerPieces();
        });
        binding.moveUp.setOnClickListener(v -> {
            gVal.offsetR -= gVal.showShiftY;
            if (gVal.offsetR < 0 || gVal.offsetR == 1)
                gVal.offsetR = 0;
            copy2RecyclerPieces();
        });
        binding.moveDown.setOnClickListener(v -> {
            gVal.offsetR += gVal.showShiftY;
            if (gVal.offsetR >= gVal.rowNbr - gVal.showMaxY)
                gVal.offsetR = gVal.rowNbr - gVal.showMaxY;
            if (gVal.offsetR + gVal.showMaxY == gVal.rowNbr - 1)
                gVal.offsetR = gVal.rowNbr - gVal.showMaxY;
            copy2RecyclerPieces();
        });

        binding.showBack.setImageResource(eyes[showBack]);
        if (showBack == 0) {
            showBackCount = showBackLoop;
            backBlink = true;
        }
        binding.showBack.setOnClickListener(v -> {
            showBack = (showBack + 1) % 3;
            binding.showBack.setImageResource(eyes[showBack]);
            if (showBack == 0)
                showBackCount = showBackLoop;
            save_params();
        });

        binding.vibrate.setImageResource((vibrate)? R.drawable.z_vibrate_on : R.drawable.z_vibrate_off);
        binding.vibrate.setOnClickListener(v -> { vibrate = !vibrate;
            binding.vibrate.setImageResource((vibrate)? R.drawable.z_vibrate_on : R.drawable.z_vibrate_off);
            save_params();
        });

        binding.sound.setImageResource((sound)? R.drawable.z_sound_on : R.drawable.z_sound_off);
        binding.sound.setOnClickListener(v -> { sound = !sound;
            binding.sound.setImageResource((sound)? R.drawable.z_sound_on : R.drawable.z_sound_off);
            save_params();
        });

        int [] backColors = {ContextCompat.getColor(mContext, R.color.backColor0),
                            ContextCompat.getColor(mContext, R.color.backColor1),
                            ContextCompat.getColor(mContext, R.color.backColor2)};

        binding.backcolor.setOnClickListener(v -> {
            backColor = (backColor + 1) % 3;
            binding.layoutJigsaw.setBackgroundColor(backColors[backColor]);
            save_params();
        });

        jigRecyclerView = findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = gVal.recSize;
        activeAdapter = new JigsawAdapter();
        jigRecyclerView.setHasFixedSize(true);

        ItemTouchHelper helper = new ItemTouchHelper(
                new JigRecycleCallback(activeAdapter));

        helper.attachToRecyclerView(jigRecyclerView);

        jigRecyclerView.setAdapter(activeAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        jigRecyclerView.setLayoutManager(mLinearLayoutManager);

        new DefineControlButton(binding);
        copy2RecyclerPieces();

        String info = currGame+"\n"+levelNames[currLevel] + "\n"+gVal.colNbr+"x"+gVal.rowNbr;

        binding.pieceInfo.setText(info);
        doNotUpdate = false;

        if (debugMode) {
            // followings are to test congrats
            for (int cc = 0; cc < gVal.colNbr; cc++) {
                for (int rr = 0; rr < gVal.rowNbr; rr++) {
                    if (new Random().nextInt(19) > 1)
                        gVal.jigTables[cc][rr].locked = true;
                }
            }
        }
        pieceImage = new PieceImage(this, gVal.imgOutSize, gVal.imgInSize);
        backView.init(this, binding, pieceImage);
        backView.invalidate();
        foreView.init(this, binding, pieceImage);
        foreView.invalidate();

        loopTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (foreBlink)
                    foreView.invalidate();
                if (backBlink)
                    backView.invalidate();
            }
        };
        loopTimer.schedule(timerTask, INVALIDATE_INTERVAL, INVALIDATE_INTERVAL);
    }

    // build recycler from all pieces within in leftC, rightC, topR, bottomR
    public void copy2RecyclerPieces() {

        binding.moveLeft.setVisibility ((gVal.offsetC == 0) ? View.INVISIBLE: View.VISIBLE);
        binding.moveRight.setVisibility ((gVal.offsetC == gVal.colNbr-gVal.showMaxX) ? View.INVISIBLE: View.VISIBLE);
        binding.moveUp.setVisibility((gVal.offsetR == 0)? View.INVISIBLE: View.VISIBLE);
        binding.moveDown.setVisibility((gVal.offsetR == gVal.rowNbr-gVal.showMaxY)? View.INVISIBLE: View.VISIBLE);

        binding.layoutJigsaw.setAlpha(0.5f);
        binding.thumbnail.setImageResource(R.drawable.z_transparent);
        binding.thumbnail.invalidate();
        doNotUpdate = true;
        gVal.activeJigs = new ArrayList<>();
        for (int i = 0; i < gVal.allPossibleJigs.size(); i++) {
            int cr = gVal.allPossibleJigs.get(i);
            int cc = cr / 10000;
            int rr = cr - cc * 10000;
            if (!gVal.jigTables[cc][rr].locked && !gVal.jigTables[cc][rr].outRecycle &&
                    cc >= gVal.offsetC && cc < gVal.offsetC + gVal.showMaxX && rr >= gVal.offsetR && rr < gVal.offsetR + gVal.showMaxY) {
                gVal.activeJigs.add(cr);
            }
        }
        areaMap = Bitmap.createBitmap(chosenImageMap,
                gVal.offsetC * gVal.imgInSize + gVal.imgGapSize,
                gVal.offsetR * gVal.imgInSize + gVal.imgGapSize,
                gVal.showMaxX * gVal.imgInSize,
                gVal.showMaxY * gVal.imgInSize, null, false);

        areaMap = Bitmap.createScaledBitmap(areaMap,
                gVal.showMaxX * gVal.picISize, gVal.showMaxY * gVal.picISize,
                false);
        jigRecyclerView.setAdapter(activeAdapter);
        Bitmap thumb = showThumbnail.make();
        binding.thumbnail.setImageBitmap(thumb);
        binding.layoutJigsaw.setAlpha(1f);
        binding.layoutJigsaw.invalidate();
        doNotUpdate = false;
        allLockedMode = 0;
        congCount = 0;
        backBlink = true;
        if (showBack == 0)
            showBackCount = showBackLoop;
    }

    @Override
    protected void onPause() {
        Log.w("jigsaw","jigsaw onPause "+ gameMode);

//        invalidateTimer.cancel();
        if (gameMode != GAME_GOBACK_TO_MAIN)
            gameMode = GAME_PAUSED;
        new GValGetPut().put(currGameLevel, gVal, this);

        history.time[gVal.level] = System.currentTimeMillis();
        int locked = 0;
        for (int cc = 0; cc < gVal.colNbr; cc++) {
            for (int rr = 0; rr < gVal.rowNbr; rr++) {
                if (gVal.jigTables[cc][rr].locked)
                    locked++;
            }
        }
        history.locked[gVal.level] = locked;
        history.percent[gVal.level] = locked * 100 / (gVal.colNbr * gVal.rowNbr);
        history.latest = gVal.level;

        if (historyIdx != -1) {
                histories.set(historyIdx, history);
        } else
            histories.add(history);
        new HistoryGetPut().put(histories, this);

        jigRecyclerView = null;
        foreView = null;
        backView = null;
        activeAdapter = null;
        jigPic = null;
        jigOLine = null;
        jigWhite = null;
        jigGray = null;
        System.gc();
        if (gameMode == GAME_PAUSED)
            finish();
        super.onPause();
    }

    private void save_params() {
        SharedPreferences sharedPref = getSharedPreferences("params", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        sharedEditor.putInt("showBack", showBack);
        sharedEditor.putBoolean("vibrate", vibrate);
        sharedEditor.putBoolean("sound", sound);
        sharedEditor.putInt("backColor", backColor);
        sharedEditor.putString("appVersion", appVersion);
        sharedEditor.apply();
        backView.invalidate();
        foreView.invalidate();
    }

    @Override
    public void onBackPressed() {
        Log.w("jigsaw","jigsaw onBackPressed");
        gameMode = GAME_GOBACK_TO_MAIN;
        super.onBackPressed();
    }
}

//        binding.layoutJigsaw.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.w("paintview onTouch "+event.getAction(), "sel "+v.getX()+"x"+v.getY()
//                        + " xy= "+event.getX()+"x"+event.getY());
//                return binding.layoutJigsaw.performClick();
//            }
//        });
//        binding.layoutJigsaw.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                if (event.getAction() == DragEvent.ACTION_DROP) {
//                    int iX = (int) event.getX() - GVal.picHSize;
//                    int iY = (int) event.getY() - GVal.picHSize;
//
//                    if (iY < screenY - GVal.recSize - GVal.recSize - GVal.picOSize) {
//                        jPosX = iX;
//                        jPosY = iY;
//                        doNotUpdate = true;
//                        removeFrmRecycle.sendEmptyMessage(0);
//                        add2FloatingPiece();
//                    }
//                }
//                return true;
//            }
//        });
