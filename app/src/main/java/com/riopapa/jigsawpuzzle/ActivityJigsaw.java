package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;
import static com.riopapa.jigsawpuzzle.Vars.activeRecyclerJigs;
import static com.riopapa.jigsawpuzzle.Vars.allPossibleJigs;
import static com.riopapa.jigsawpuzzle.Vars.difficulties;
import static com.riopapa.jigsawpuzzle.Vars.difficulty;
import static com.riopapa.jigsawpuzzle.Vars.jigCOLUMNs;
import static com.riopapa.jigsawpuzzle.Vars.jigGapSize;
import static com.riopapa.jigsawpuzzle.Vars.jigInnerSize;
import static com.riopapa.jigsawpuzzle.Vars.jigOuterSize;
import static com.riopapa.jigsawpuzzle.Vars.jigROWs;
import static com.riopapa.jigsawpuzzle.Vars.jigTables;
import static com.riopapa.jigsawpuzzle.Vars.maskMaps;
import static com.riopapa.jigsawpuzzle.Vars.offsetC;
import static com.riopapa.jigsawpuzzle.Vars.offsetR;
import static com.riopapa.jigsawpuzzle.Vars.outMaps;
import static com.riopapa.jigsawpuzzle.Vars.recySize;
import static com.riopapa.jigsawpuzzle.Vars.selectedHeight;
import static com.riopapa.jigsawpuzzle.Vars.selectedImage;
import static com.riopapa.jigsawpuzzle.Vars.selectedWidth;
import static com.riopapa.jigsawpuzzle.Vars.showMaxX;
import static com.riopapa.jigsawpuzzle.Vars.showMaxY;
import static com.riopapa.jigsawpuzzle.Vars.showShiftX;
import static com.riopapa.jigsawpuzzle.Vars.showShiftY;

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
            offsetC -= showShiftX;
            if (offsetC < 0)
                offsetC = 0;
            copy2RecyclerPieces();
        });
        binding.moveRight.setOnClickListener(v -> {
            offsetC += showShiftX;
            if (offsetC >= jigCOLUMNs - showMaxX)
                offsetC = jigCOLUMNs - showMaxX;
            copy2RecyclerPieces();
        });
        binding.moveUp.setOnClickListener(v -> {
            offsetR -= showShiftY;
            if (offsetR < 0)
                offsetR = 0;
            copy2RecyclerPieces();
        });
        binding.moveDown.setOnClickListener(v -> {
            offsetR += showShiftY;
            if (offsetR >= jigROWs - showMaxY)
                offsetR = jigROWs - showMaxY;
            copy2RecyclerPieces();
        });


// Hide the status bar.

        String s = "Show "+showMaxX+" x "+showMaxY;
        for (int i = 0; i < 4; i++) {
            new CalcColRowSize(i);
            s += "Level "+difficulties[i]+" CR "+jigCOLUMNs+"x"+jigROWs + " \n";
        }
        difficulty = rnd.nextInt(4);
        new CalcColRowSize(difficulty);
        s += "Level "+difficulties[difficulty]+" CR "+jigCOLUMNs+"x"+jigROWs + " \n";
        tvLeft.setText(s);
        selectDificulty();

        selectedImage = Bitmap.createBitmap(selectedImage, 0, 0,
                jigInnerSize * jigCOLUMNs + jigGapSize + jigGapSize,
                jigInnerSize * jigROWs  + jigGapSize + jigGapSize);
        selectedWidth = selectedImage.getWidth();
        selectedHeight = selectedImage.getHeight();

        new intGlobalValues();

        // decide jigsaw pieces numbers


        pieceImage = new PieceImage(this, jigOuterSize, jigInnerSize);


        jigTables = new JigTable[jigCOLUMNs][jigROWs];
        new initJigTable(jigTables, jigCOLUMNs, jigROWs);

        maskMaps = new Masks().make(mContext, jigOuterSize);
        outMaps = new Masks().makeOut(mContext, jigOuterSize);


        new FullRecyclePiece();

        jigRecyclerView = findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = recySize;
        jigRecycleAdapter = new RecycleJigListener();
//        ItemTouchHelper.Callback mainCallback = new PaintViewTouchCallback(jigRecycleAdapter, mContext);
//        ItemTouchHelper mainItemTouchHelper = new ItemTouchHelper(mainCallback);
//        jigRecycleAdapter.setTouchHelper(mainItemTouchHelper);
        ItemTouchHelper helper = new ItemTouchHelper(new PaintViewTouchCallback(jigRecycleAdapter, mContext));;
//        jigRecycleAdapter.setTouchHelper(mainItemTouchHelper);

        helper.attachToRecyclerView(jigRecyclerView);
        jigRecyclerView.setAdapter(jigRecycleAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        jigRecyclerView.setLayoutManager(mLinearLayoutManager);

        new AdjustControl(binding, recySize * 3 / 4);
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


        int szW = selectedWidth / (jigCOLUMNs+1);
        int szH = selectedHeight / (jigROWs+1);
        jigInnerSize = Math.min(szW, szH);
        jigOuterSize = jigInnerSize * (14+5+5) / 14;
        jigGapSize = jigInnerSize *5/14;
        Log.w("imageInfo", selectedWidth + "x"+selectedHeight+ "CR="+jigCOLUMNs+"x"+jigROWs);
        Log.w("main jig Size"," outerSize="+ jigOuterSize +", gapSize="+ jigGapSize +", innerSize="+ jigInnerSize);
    }



    // build recycler from all pieces within in leftC, rightC, topR, bottomR
    public void copy2RecyclerPieces() {
        activeRecyclerJigs = new ArrayList<>();
        for (int i = 0; i < allPossibleJigs.size(); i++) {
            int cr = allPossibleJigs.get(i);
            int c = cr / 10000;
            int r = cr - c * 10000;
            if (!jigTables[c][r].locked && !jigTables[c][r].outRecycle &&
                    c >= offsetC && c < offsetC + showMaxX && r >= offsetR && r < offsetR + showMaxY) {
                activeRecyclerJigs.add(cr);
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