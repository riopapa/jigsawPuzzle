package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static com.riopapa.jigsawpuzzle.ActivityMain.currGame;
import static com.riopapa.jigsawpuzzle.ActivityMain.currGameLevel;
import static com.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static com.riopapa.jigsawpuzzle.ActivityMain.histories;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import com.riopapa.jigsawpuzzle.func.AdjustControl;
import com.riopapa.jigsawpuzzle.func.ClearGlobalValues;
import com.riopapa.jigsawpuzzle.func.FullRecyclePiece;
import com.riopapa.jigsawpuzzle.func.HistoryGetPut;
import com.riopapa.jigsawpuzzle.func.SettleJigTableWall;
import com.riopapa.jigsawpuzzle.func.ShowThumbnail;
import com.riopapa.jigsawpuzzle.func.GValGetPut;
import com.riopapa.jigsawpuzzle.model.History;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityJigsaw extends Activity {

    ActivityJigsawBinding binding;

    public static PieceImage pieceImage;

    public static RecyclerView jigRecyclerView;


    public static PaintView paintView;

    public static JigsawAdapter jigRecycleAdapter;

    public static boolean doNotUpdate; // wait while one action completed
;
    public static Timer invalidateTimer;

    public static Bitmap chosenImageMap;
    public static int chosenImageWidth, chosenImageHeight, chosenImageColor; // puzzle photo size (in dpi)
    public static Bitmap [][] jigPic, jigBright, jigWhite, jigOLine;
    public static int jigRecyclePos; // jigsaw slide x, y count
    public static int nowC, nowR, nowCR;   // fullImage pieceImage array column, row , x*10000+y
    public static int dragX, dragY; // absolute x,y rightPosition drawing current jigsaw
    public static History history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJigsawBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Log.w("jSaw","onCreate gameMode="+gameMode);

        paintView = findViewById(R.id.paintview);
        paintView.init(this);
        binding.paintview.getLayoutParams().height = screenBottom;

        binding.moveLeft.setOnClickListener(v -> {
            gVal.offsetC -= gVal.showShiftX;
            if (gVal.offsetC < 0)
                gVal.offsetC = 0;
            copy2RecyclerPieces();
        });
        binding.moveRight.setOnClickListener(v -> {
            gVal.offsetC += gVal.showShiftX;
            if (gVal.offsetC >= gVal.jigCOLs - gVal.showMaxX)
                gVal.offsetC = gVal.jigCOLs - gVal.showMaxX;
            copy2RecyclerPieces();
        });
        binding.moveUp.setOnClickListener(v -> {
            gVal.offsetR -= gVal.showShiftY;
            if (gVal.offsetR < 0)
                gVal.offsetR = 0;
            copy2RecyclerPieces();
        });
        binding.moveDown.setOnClickListener(v -> {
            gVal.offsetR += gVal.showShiftY;
            if (gVal.offsetR >= gVal.jigROWs - gVal.showMaxY)
                gVal.offsetR = gVal.jigROWs - gVal.showMaxY;
            copy2RecyclerPieces();
        });

        pieceImage = new PieceImage(this, gVal.imgOutSize, gVal.imgInSize);


        jigPic = new Bitmap[gVal.jigCOLs][gVal.jigROWs];
        jigOLine = new Bitmap[gVal.jigCOLs][gVal.jigROWs];
        jigBright = new Bitmap[gVal.jigCOLs][gVal.jigROWs];
        jigWhite = new Bitmap[gVal.jigCOLs][gVal.jigROWs];


        srcMaskMaps = new Masks().make(mContext, gVal.imgOutSize);
        outMaskMaps = new Masks().makeOut(mContext, gVal.imgOutSize);

        new FullRecyclePiece();

        jigRecyclerView = findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = gVal.recSize;
        jigRecycleAdapter = new JigsawAdapter();
        jigRecyclerView.setHasFixedSize(true);

        ItemTouchHelper helper = new ItemTouchHelper(
                new JigRecycleCallback(jigRecycleAdapter, binding));

        helper.attachToRecyclerView(jigRecyclerView);

        jigRecyclerView.setAdapter(jigRecycleAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        jigRecyclerView.setLayoutManager(mLinearLayoutManager);

        new AdjustControl(binding, gVal.recSize * 3 / 4);
        copy2RecyclerPieces();

        if (paintView != null) {
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    paintView.invalidate();
                }
            };
            invalidateTimer = new Timer();
            invalidateTimer.schedule(tt, 100, 50);
        }

        history = new History();
        history.gameLevel = currGameLevel;
        for (int i = 0; i <  histories.size(); i++) {
            if (currGameLevel.equals(histories.get(i).gameLevel)) {
                history = histories.get(i);
                break;
            }
        }

    }



    // build recycler from all pieces within in leftC, rightC, topR, bottomR
    public void copy2RecyclerPieces() {
        gVal.activeRecyclerJigs = new ArrayList<>();
        for (int i = 0; i < gVal.allPossibleJigs.size(); i++) {
            int cr = gVal.allPossibleJigs.get(i);
            int cc = cr / 10000;
            int rr = cr - cc * 10000;
            if (!gVal.jigTables[cc][rr].locked && !gVal.jigTables[cc][rr].outRecycle &&
                    cc >= gVal.offsetC && cc < gVal.offsetC + gVal.showMaxX && rr >= gVal.offsetR && rr < gVal.offsetR + gVal.showMaxY) {
                gVal.activeRecyclerJigs.add(cr);
            }
        }

        jigRecycleAdapter.notifyDataSetChanged();
        new ShowThumbnail(binding);

    }

    @Override
    protected void onPause() {
        Log.w("jigsaw","jigsaw onPause "+ gameMode);
        history.time[gVal.gameLevel] = System.currentTimeMillis();
        int locked = 1;
        for (int cc = 0; cc < gVal.jigCOLs; cc++) {
            for (int rr = 0; rr < gVal.jigROWs; rr++) {
                if (gVal.jigTables[cc][rr].locked)
                    locked++;
            }
        }
        history.percent[gVal.gameLevel] = locked * 100 / (gVal.jigCOLs * gVal.jigROWs);
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).gameLevel.equals(currGameLevel)) {
                histories.set(i, history);
                history = null;
            }
        }
        if (history != null)
            histories.add(history);

        if (gameMode != GAME_GOBACK_TO_MAIN)
            gameMode = GAME_PAUSED;
        invalidateTimer.cancel();
        new GValGetPut().put(currGameLevel, gVal, this);
        new HistoryGetPut().put(histories, this);
        super.onPause();
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
