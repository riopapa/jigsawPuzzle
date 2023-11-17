package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

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
    public static Bitmap [][] jigPic;
    public static Bitmap [][] jigBright;
    public static Bitmap [][] jigOLine;
    public static int jigRecyclePos; // jigsaw slide x, y count
    public static int nowC, nowR, nowCR;   // fullImage pieceImage array column, row , x*10000+y
    public static int jPosX, jPosY, dragX, dragY; // absolute x,y rightPosition drawing current jigsaw
    public static History history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJigsawBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        paintView = findViewById(R.id.paintview);
        paintView.init(this);

        rnd = new Random(System.currentTimeMillis() & 0xfffff);

        binding.moveLeft.setOnClickListener(v -> {
            vars.offsetC -= vars.showShiftX;
            if (vars.offsetC < 0)
                vars.offsetC = 0;
            copy2RecyclerPieces();
        });
        binding.moveRight.setOnClickListener(v -> {
            vars.offsetC += vars.showShiftX;
            if (vars.offsetC >= vars.jigCOLs - vars.showMaxX)
                vars.offsetC = vars.jigCOLs - vars.showMaxX;
            copy2RecyclerPieces();
        });
        binding.moveUp.setOnClickListener(v -> {
            vars.offsetR -= vars.showShiftY;
            if (vars.offsetR < 0)
                vars.offsetR = 0;
            copy2RecyclerPieces();
        });
        binding.moveDown.setOnClickListener(v -> {
            vars.offsetR += vars.showShiftY;
            if (vars.offsetR >= vars.jigROWs - vars.showMaxY)
                vars.offsetR = vars.jigROWs - vars.showMaxY;
            copy2RecyclerPieces();
        });

        defineImgSize();

        chosenImageMap = Bitmap.createBitmap(chosenImageMap, 0, 0,
                vars.imgInSize * vars.jigCOLs + vars.imgGapSize + vars.imgGapSize,
                vars.imgInSize * vars.jigROWs  + vars.imgGapSize + vars.imgGapSize);
        chosenImageWidth = chosenImageMap.getWidth();
        chosenImageHeight = chosenImageMap.getHeight();
        Log.w("jigsaw Info", " size="+chosenImageWidth+"x"+chosenImageHeight);

        new ClearGlobalValues();

        // decide jigsaw pieces numbers


        pieceImage = new PieceImage(this, vars.imgOutSize, vars.imgInSize);


        vars.jigTables = new JigTable[vars.jigCOLs][vars.jigROWs];
        jigPic = new Bitmap[vars.jigCOLs][vars.jigROWs];
        jigBright = new Bitmap[vars.jigCOLs][vars.jigROWs];
        jigOLine = new Bitmap[vars.jigCOLs][vars.jigROWs];

        new SettleJigTableWall(vars.jigTables);

        srcMaskMaps = new Masks().make(mContext, vars.imgOutSize);
        outMaskMaps = new Masks().makeOut(mContext, vars.imgOutSize);


        new FullRecyclePiece();

        jigRecyclerView = findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = vars.recSize;
        jigRecycleAdapter = new JigsawAdapter();
        jigRecyclerView.setHasFixedSize(true);

        ItemTouchHelper helper = new ItemTouchHelper(
                new JigRecycleCallback(jigRecycleAdapter, binding));

        helper.attachToRecyclerView(jigRecyclerView);

        jigRecyclerView.setAdapter(jigRecycleAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        jigRecyclerView.setLayoutManager(mLinearLayoutManager);

        new AdjustControl(binding, vars.recSize * 3 / 4);
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
        for (int i = 0; i <  vars.histories.size(); i++) {
            if (chosenKey.equals(vars.histories.get(i).key)) {
                history = vars.histories.get(i);
                break;
            }
        }

    }


    private static void defineImgSize() {
        float szW = (float) chosenImageWidth / (float) (vars.jigCOLs+1);
        float szH = (float) chosenImageHeight / (float) (vars.jigROWs+1);
        vars.imgInSize = (szH > szW) ? (int) szW : (int) szH;
        vars.imgGapSize = vars.imgInSize * 5 / 24;
        vars.imgOutSize = vars.imgInSize + vars.imgGapSize + vars.imgGapSize;

    }


    // build recycler from all pieces within in leftC, rightC, topR, bottomR
    public void copy2RecyclerPieces() {
        vars.activeRecyclerJigs = new ArrayList<>();
        for (int i = 0; i < vars.allPossibleJigs.size(); i++) {
            int cr = vars.allPossibleJigs.get(i);
            int c = cr / 10000;
            int r = cr - c * 10000;
            if (!vars.jigTables[c][r].locked && !vars.jigTables[c][r].outRecycle &&
                    c >= vars.offsetC && c < vars.offsetC + vars.showMaxX && r >= vars.offsetR && r < vars.offsetR + vars.showMaxY) {
                vars.activeRecyclerJigs.add(cr);
            }
        }

        jigRecycleAdapter.notifyDataSetChanged();
        new ShowThumbnail(binding);

    }

    @Override
    protected void onPause() {
        Log.w("jigsaw","Activityjigsaw onPause "+vars.gameMode);
        history.time[vars.gameLevel] = System.currentTimeMillis();
        int locked = 1;
        for (int cc = 0; cc < vars.jigCOLs; cc++) {
            for (int rr = 0; rr < vars.jigROWs; rr++) {
                if (vars.jigTables[cc][rr].locked)
                    locked++;
            }
        }
        history.percent[vars.gameLevel] = locked * 100 / (vars.jigCOLs * vars.jigROWs);
        for (int i = 0; i < vars.histories.size(); i++) {
            if (vars.histories.get(i).key.equals(chosenKey)) {
                vars.histories.set(i, history);
                history = null;
            }
        }
        if (history != null)
            vars.histories.add(history);

        if (vars.gameMode != GAME_GOBACK_TO_MAIN)
            vars.gameMode = GAME_PAUSED;
        invalidateTimer.cancel();
        new VarsGetPut().put(vars, this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.w("jigsaw","Activityjigsaw onBackPressed");
        vars.gameMode = GAME_GOBACK_TO_MAIN;
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
//                    int iX = (int) event.getX() - vars.picHSize;
//                    int iY = (int) event.getY() - vars.picHSize;
//
//                    if (iY < screenY - vars.recSize - vars.recSize - vars.picOSize) {
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
