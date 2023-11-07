package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import com.riopapa.jigsawpuzzle.func.AdjustControl;
import com.riopapa.jigsawpuzzle.func.CalcColRowSize;
import com.riopapa.jigsawpuzzle.func.FullRecyclePiece;
import com.riopapa.jigsawpuzzle.func.ShowThumbnail;
import com.riopapa.jigsawpuzzle.func.VarsGetPut;
import com.riopapa.jigsawpuzzle.func.initJigTable;
import com.riopapa.jigsawpuzzle.func.intGlobalValues;
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


    public static Random rnd;
    public static Timer invalidateTimer;

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
            if (vars.offsetC >= vars.jigCOLUMNs - vars.showMaxX)
                vars.offsetC = vars.jigCOLUMNs - vars.showMaxX;
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


// Hide the status bar.

        String s = "Show "+vars.showMaxX+" x "+vars.showMaxY;
        for (int i = 0; i < 4; i++) {
            new CalcColRowSize(i);
            s += "Level "+vars.difficulties[i]+" CR "+vars.jigCOLUMNs+"x"+vars.jigROWs + " \n";
        }
        vars.difficulty = rnd.nextInt(4);
        new CalcColRowSize(vars.difficulty);
        s += "Level "+vars.difficulties[vars.difficulty]+" CR "+vars.jigCOLUMNs+"x"+vars.jigROWs + " \n";
        tvLeft.setText(s);
        selectDificulty();

        vars.selectedImage = Bitmap.createBitmap(vars.selectedImage, 0, 0,
                vars.jigInnerSize * vars.jigCOLUMNs + vars.jigGapSize + vars.jigGapSize,
                vars.jigInnerSize * vars.jigROWs  + vars.jigGapSize + vars.jigGapSize);
        vars.selectedWidth = vars.selectedImage.getWidth();
        vars.selectedHeight = vars.selectedImage.getHeight();

        new intGlobalValues();

        // decide jigsaw pieces numbers


        pieceImage = new PieceImage(this, vars.jigOuterSize, vars.jigInnerSize);


        vars.jigTables = new JigTable[vars.jigCOLUMNs][vars.jigROWs];
        new initJigTable(vars.jigTables, vars.jigCOLUMNs, vars.jigROWs);

        vars.maskMaps = new Masks().make(mContext, vars.jigOuterSize);
        vars.outMaps = new Masks().makeOut(mContext, vars.jigOuterSize);


        new FullRecyclePiece();

        jigRecyclerView = findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = vars.recySize;
        jigRecycleAdapter = new RecycleJigListener();
//        ItemTouchHelper.Callback mainCallback = new PaintViewTouchCallback(vars.jigRecycleAdapter, mContext);
//        ItemTouchHelper mainItemTouchHelper = new ItemTouchHelper(mainCallback);
//        vars.jigRecycleAdapter.setTouchHelper(mainItemTouchHelper);
        ItemTouchHelper helper = new ItemTouchHelper(new PaintViewTouchCallback(jigRecycleAdapter, mContext));;
//        vars.jigRecycleAdapter.setTouchHelper(mainItemTouchHelper);

        helper.attachToRecyclerView(jigRecyclerView);
        jigRecyclerView.setAdapter(jigRecycleAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        jigRecyclerView.setLayoutManager(mLinearLayoutManager);

        new AdjustControl(binding, vars.recySize * 3 / 4);
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

    private static void selectDificulty() {


        int szW = vars.selectedWidth / (vars.jigCOLUMNs+1);
        int szH = vars.selectedHeight / (vars.jigROWs+1);
        vars.jigInnerSize = Math.min(szW, szH);
        vars.jigOuterSize = vars.jigInnerSize * (14+5+5) / 14;
        vars.jigGapSize = vars.jigInnerSize *5/14;
        Log.w("imageInfo", vars.selectedWidth + "x"+vars.selectedHeight+ "CR="+vars.jigCOLUMNs+"x"+vars.jigROWs);
        Log.w("main jig Size"," outerSize="+ vars.jigOuterSize +", gapSize="+ vars.jigGapSize +", innerSize="+ vars.jigInnerSize);
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
        invalidateTimer.cancel();
        new VarsGetPut().put(vars, this);
    }
}