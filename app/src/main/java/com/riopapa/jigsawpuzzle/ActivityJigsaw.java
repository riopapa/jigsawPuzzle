package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static com.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import com.riopapa.jigsawpuzzle.func.AdjustControl;
import com.riopapa.jigsawpuzzle.func.FullRecyclePiece;
import com.riopapa.jigsawpuzzle.func.ShowThumbnail;
import com.riopapa.jigsawpuzzle.func.VarsGetPut;
import com.riopapa.jigsawpuzzle.func.initJigTable;
import com.riopapa.jigsawpuzzle.func.ClearGlobalValues;
import com.riopapa.jigsawpuzzle.model.JigTable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityJigsaw extends Activity {

    ActivityJigsawBinding binding;
    public static TextView tvLeft, tvRight;

    public static ImageView imageAnswer, thumbNail, moveL, moveR, moveU, moveD;


    public static PieceImage pieceImage;

    public static RecyclerView jigRecyclerView;


    public static PaintView paintView;

    public static RecycleJigListener jigRecycleAdapter;

    public static boolean doNotUpdate; // wait while one action completed

    public static Random rnd;
    public static Timer invalidateTimer;

    public static Bitmap selectedImage;
    public static int selectedWidth, selectedHeight; // puzzle photo size (in dpi)

    public static Bitmap [][] jigPic;
    public static Bitmap [][] jigBright;
    public static Bitmap [][] jigOLine;
    public static int jigRecyclePos; // jigsaw slide x, y count
    public static int nowC, nowR, jigCR;   // fullImage pieceImage array column, row , x*10000+y
    public static int jPosX, jPosY; // absolute x,y rightPosition drawing current jigsaw


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJigsawBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        paintView = findViewById(R.id.paintview);
        paintView.init(this);

        tvLeft = findViewById(R.id.debug_left);
        tvRight = findViewById(R.id.debug_right);

        imageAnswer = findViewById(R.id.image_answer);
        rnd = new Random(System.currentTimeMillis() & 0xfffff);

        thumbNail = findViewById(R.id.thumbnail);
        moveL = findViewById(R.id.move_left);
        moveR = findViewById(R.id.move_right);
        moveU = findViewById(R.id.move_up);
        moveD = findViewById(R.id.move_down);

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

        selectedImage = Bitmap.createBitmap(selectedImage, 0, 0,
                vars.imgInSize * vars.jigCOLs + vars.imgGapSize + vars.imgGapSize,
                vars.imgInSize * vars.jigROWs  + vars.imgGapSize + vars.imgGapSize);
        selectedWidth = selectedImage.getWidth();
        selectedHeight = selectedImage.getHeight();

        new ClearGlobalValues();

        // decide jigsaw pieces numbers


        pieceImage = new PieceImage(this, vars.imgOutSize, vars.imgInSize);


        vars.jigTables = new JigTable[vars.jigCOLs][vars.jigROWs];
        jigPic = new Bitmap[vars.jigCOLs][vars.jigROWs];
        jigBright = new Bitmap[vars.jigCOLs][vars.jigROWs];
        jigOLine = new Bitmap[vars.jigCOLs][vars.jigROWs];

        new initJigTable(vars.jigTables);

        srcMaskMaps = new Masks().make(mContext, vars.imgOutSize);
        outMaskMaps = new Masks().makeOut(mContext, vars.imgOutSize);


        new FullRecyclePiece();

        jigRecyclerView = findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = vars.recSize;
        jigRecycleAdapter = new RecycleJigListener();
//        ItemTouchHelper.Callback mainCallback = new PaintViewTouchCallback(vars.jigRecycleAdapter, mContext);
//        ItemTouchHelper mainItemTouchHelper = new ItemTouchHelper(mainCallback);
//        vars.jigRecycleAdapter.setTouchHelper(mainItemTouchHelper);
        ItemTouchHelper helper = new ItemTouchHelper(new PaintViewTouchCallback(jigRecycleAdapter, mContext));
//        vars.jigRecycleAdapter.setTouchHelper(mainItemTouchHelper);

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

    }

    private static void defineImgSize() {
        float szW = (float) selectedWidth / (float) (vars.jigCOLs+1);
        float szH = (float) selectedHeight / (float) (vars.jigROWs+1);
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
        super.onPause();
        vars.gameMode = GAME_PAUSED;
        invalidateTimer.cancel();
        new VarsGetPut().put(vars, this);
    }
}