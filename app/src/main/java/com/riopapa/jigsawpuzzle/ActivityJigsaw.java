package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

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
import com.riopapa.jigsawpuzzle.func.SettleJigTableWall;
import com.riopapa.jigsawpuzzle.func.ShowThumbnail;
import com.riopapa.jigsawpuzzle.func.VarsGetPut;
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

    public static Random rnd;
    public static Timer invalidateTimer;

    public static Bitmap chosenImageMap;
    public static int chosenImageWidth, chosenImageHeight, chosenImageColor; // puzzle photo size (in dpi)
    public static String chosenKey;
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

        paintView = findViewById(R.id.paintview);
        paintView.init(this);
        binding.paintview.getLayoutParams().height = screenBottom;

        rnd = new Random(System.currentTimeMillis() & 0xfffff);

        binding.moveLeft.setOnClickListener(v -> {
            GVal.offsetC -= GVal.showShiftX;
            if (GVal.offsetC < 0)
                GVal.offsetC = 0;
            copy2RecyclerPieces();
        });
        binding.moveRight.setOnClickListener(v -> {
            GVal.offsetC += GVal.showShiftX;
            if (GVal.offsetC >= GVal.jigCOLs - GVal.showMaxX)
                GVal.offsetC = GVal.jigCOLs - GVal.showMaxX;
            copy2RecyclerPieces();
        });
        binding.moveUp.setOnClickListener(v -> {
            GVal.offsetR -= GVal.showShiftY;
            if (GVal.offsetR < 0)
                GVal.offsetR = 0;
            copy2RecyclerPieces();
        });
        binding.moveDown.setOnClickListener(v -> {
            GVal.offsetR += GVal.showShiftY;
            if (GVal.offsetR >= GVal.jigROWs - GVal.showMaxY)
                GVal.offsetR = GVal.jigROWs - GVal.showMaxY;
            copy2RecyclerPieces();
        });

        defineImgSize();

        chosenImageMap = Bitmap.createBitmap(chosenImageMap, 0, 0,
                GVal.imgInSize * GVal.jigCOLs + GVal.imgGapSize + GVal.imgGapSize,
                GVal.imgInSize * GVal.jigROWs  + GVal.imgGapSize + GVal.imgGapSize);
        chosenImageWidth = chosenImageMap.getWidth();
        chosenImageHeight = chosenImageMap.getHeight();
        Log.w("jigsaw Info", " size="+chosenImageWidth+"x"+chosenImageHeight);

        new ClearGlobalValues();

        // decide jigsaw pieces numbers


        pieceImage = new PieceImage(this, GVal.imgOutSize, GVal.imgInSize);


        jigPic = new Bitmap[GVal.jigCOLs][GVal.jigROWs];
        jigOLine = new Bitmap[GVal.jigCOLs][GVal.jigROWs];
        jigBright = new Bitmap[GVal.jigCOLs][GVal.jigROWs];
        jigWhite = new Bitmap[GVal.jigCOLs][GVal.jigROWs];

        GVal.jigTables = new JigTable[GVal.jigCOLs][GVal.jigROWs];
        new SettleJigTableWall(GVal.jigTables);

        srcMaskMaps = new Masks().make(mContext, GVal.imgOutSize);
        outMaskMaps = new Masks().makeOut(mContext, GVal.imgOutSize);

        new FullRecyclePiece();

        jigRecyclerView = findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = GVal.recSize;
        jigRecycleAdapter = new JigsawAdapter();
        jigRecyclerView.setHasFixedSize(true);

        ItemTouchHelper helper = new ItemTouchHelper(
                new JigRecycleCallback(jigRecycleAdapter, binding));

        helper.attachToRecyclerView(jigRecyclerView);

        jigRecyclerView.setAdapter(jigRecycleAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        jigRecyclerView.setLayoutManager(mLinearLayoutManager);

        new AdjustControl(binding, GVal.recSize * 3 / 4);
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
        history.key = chosenKey;
        for (int i = 0; i <  GVal.histories.size(); i++) {
            if (chosenKey.equals(GVal.histories.get(i).key)) {
                history = GVal.histories.get(i);
                break;
            }
        }

    }


    private static void defineImgSize() {
        float szW = (float) chosenImageWidth / (float) (GVal.jigCOLs+1);
        float szH = (float) chosenImageHeight / (float) (GVal.jigROWs+1);
        GVal.imgInSize = (szH > szW) ? (int) szW : (int) szH;
        GVal.imgGapSize = GVal.imgInSize * 5 / 24;
        GVal.imgOutSize = GVal.imgInSize + GVal.imgGapSize + GVal.imgGapSize;

    }


    // build recycler from all pieces within in leftC, rightC, topR, bottomR
    public void copy2RecyclerPieces() {
        GVal.activeRecyclerJigs = new ArrayList<>();
        for (int i = 0; i < GVal.allPossibleJigs.size(); i++) {
            int cr = GVal.allPossibleJigs.get(i);
            int cc = cr / 10000;
            int rr = cr - cc * 10000;
            if (!GVal.jigTables[cc][rr].locked && !GVal.jigTables[cc][rr].outRecycle &&
                    cc >= GVal.offsetC && cc < GVal.offsetC + GVal.showMaxX && rr >= GVal.offsetR && rr < GVal.offsetR + GVal.showMaxY) {
                GVal.activeRecyclerJigs.add(cr);
            }
        }

        jigRecycleAdapter.notifyDataSetChanged();
        new ShowThumbnail(binding);

    }

    @Override
    protected void onPause() {
        Log.w("jigsaw","Activityjigsaw onPause "+ GVal.gameMode);
        history.time[GVal.gameLevel] = System.currentTimeMillis();
        int locked = 1;
        for (int cc = 0; cc < GVal.jigCOLs; cc++) {
            for (int rr = 0; rr < GVal.jigROWs; rr++) {
                if (GVal.jigTables[cc][rr].locked)
                    locked++;
            }
        }
        history.percent[GVal.gameLevel] = locked * 100 / (GVal.jigCOLs * GVal.jigROWs);
        for (int i = 0; i < GVal.histories.size(); i++) {
            if (GVal.histories.get(i).key.equals(chosenKey)) {
                GVal.histories.set(i, history);
                history = null;
            }
        }
        if (history != null)
            GVal.histories.add(history);

        if (GVal.gameMode != GAME_GOBACK_TO_MAIN)
            GVal.gameMode = GAME_PAUSED;
        invalidateTimer.cancel();
        new VarsGetPut().put(GVal, this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.w("jigsaw","Activityjigsaw onBackPressed");
        GVal.gameMode = GAME_GOBACK_TO_MAIN;
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
