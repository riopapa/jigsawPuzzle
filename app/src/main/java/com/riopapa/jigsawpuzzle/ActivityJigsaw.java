package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static com.riopapa.jigsawpuzzle.ActivityMain.congrats;
import static com.riopapa.jigsawpuzzle.ActivityMain.currGame;
import static com.riopapa.jigsawpuzzle.ActivityMain.currGameLevel;
import static com.riopapa.jigsawpuzzle.ActivityMain.currLevel;
import static com.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static com.riopapa.jigsawpuzzle.ActivityMain.fireWorks;
import static com.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static com.riopapa.jigsawpuzzle.ActivityMain.histories;
import static com.riopapa.jigsawpuzzle.ActivityMain.levelNames;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.showBack;
import static com.riopapa.jigsawpuzzle.ActivityMain.sound;
import static com.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static com.riopapa.jigsawpuzzle.ActivityMain.vibrate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import com.riopapa.jigsawpuzzle.func.AdjustControl;
import com.riopapa.jigsawpuzzle.func.Congrat;
import com.riopapa.jigsawpuzzle.func.FireWork;
import com.riopapa.jigsawpuzzle.func.HistoryGetPut;
import com.riopapa.jigsawpuzzle.func.Masks;
import com.riopapa.jigsawpuzzle.func.ShowThumbnail;
import com.riopapa.jigsawpuzzle.func.GValGetPut;
import com.riopapa.jigsawpuzzle.model.History;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityJigsaw extends Activity {

    ActivityJigsawBinding binding;

    public static PieceImage pieceImage;

    public static RecyclerView jigRecyclerView;


    public static PaintView paintView;

    public static JigsawAdapter activeAdapter;

    public static boolean doNotUpdate; // wait while one action completed
;
    public static Timer invalidateTimer;

    public static Bitmap chosenImageMap;
    public static int chosenImageWidth, chosenImageHeight, chosenImageColor; // puzzle photo size (in dpi)
    public static Bitmap [][] jigPic, jigBright, jigWhite, jigOLine;
    public static int activePos; // jigsaw slide x, y count
    public static int nowC, nowR, nowCR;   // fullImage pieceImage array column, row , x*10000+y
    public static int dragX, dragY; // absolute x,y rightPosition drawing current jigsaw
    public static History history;
    public static int historyIdx;

    ShowThumbnail showThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJigsawBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Log.w("jSaw","onCreate gameMode="+gameMode);
        screenBottom = screenY - gVal.recSize - gVal.recSize + gVal.picGap;
        if (fPhoneInchX > 3f)
            screenBottom += gVal.picHSize;

        pieceImage = new PieceImage(this, gVal.imgOutSize, gVal.imgInSize);
        jigPic = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigOLine = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigBright = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigWhite = new Bitmap[gVal.colNbr][gVal.rowNbr];

        srcMaskMaps = new Masks().make(mContext, gVal.imgOutSize);
        outMaskMaps = new Masks().makeOut(mContext, gVal.imgOutSize);
        fireWorks = new FireWork().make(mContext, gVal.picOSize + gVal.picGap + gVal.picGap);
        congrats = new Congrat().make(mContext, gVal.picOSize + gVal.picGap + gVal.picGap);

        paintView = findViewById(R.id.paintview);
        paintView.init(this);
        binding.paintview.getLayoutParams().height = screenBottom;
        showThumbnail = new ShowThumbnail();



        binding.moveLeft.setOnClickListener(v -> {
            gVal.offsetC -= gVal.showShiftX;
            if (gVal.offsetC < 0)
                gVal.offsetC = 0;
            copy2RecyclerPieces();
        });
        binding.moveRight.setOnClickListener(v -> {
            gVal.offsetC += gVal.showShiftX;
            if (gVal.offsetC >= gVal.colNbr - gVal.showMaxX)
                gVal.offsetC = gVal.colNbr - gVal.showMaxX;
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
            if (gVal.offsetR >= gVal.rowNbr - gVal.showMaxY)
                gVal.offsetR = gVal.rowNbr - gVal.showMaxY;
            copy2RecyclerPieces();
        });

        binding.showBack.setImageResource((showBack)? R.drawable.z_eye_opened : R.drawable.z_eye_closed);
        binding.showBack.setOnClickListener(v -> { showBack = !showBack;
            binding.showBack.setImageResource((showBack)? R.drawable.z_eye_opened : R.drawable.z_eye_closed);
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

//        pieceImage = new PieceImage(this, gVal.imgOutSize, gVal.imgInSize);

        jigRecyclerView = findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = gVal.recSize;
        activeAdapter = new JigsawAdapter();
        jigRecyclerView.setHasFixedSize(true);

        ItemTouchHelper helper = new ItemTouchHelper(
                new JigRecycleCallback(activeAdapter, binding));

        helper.attachToRecyclerView(jigRecyclerView);

        jigRecyclerView.setAdapter(activeAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        jigRecyclerView.setLayoutManager(mLinearLayoutManager);

        new AdjustControl(binding);
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
        String info = currGame+"\n"+levelNames[currLevel] + "\n"+gVal.colNbr+"x"+gVal.rowNbr;

        binding.pieceInfo.setText(info);
        doNotUpdate = false;

        readyPieces();
    }

    private void readyPieces() {
        new Thread(() -> {
            for (int cc = 0; cc < gVal.colNbr; cc++) {
                for (int rr = 0; rr < gVal.rowNbr; rr++) {
                    if (jigPic[cc][rr] == null)
                        jigPic[cc][rr] = pieceImage.buildPic(cc, rr);
                    if (jigOLine[cc][rr] == null)
                        jigOLine[cc][rr] = pieceImage.buildOline(jigPic[cc][rr], cc, rr);
                }
            }
            Log.w("r readyPieces"," completed");
        }).start();

    }


    // build recycler from all pieces within in leftC, rightC, topR, bottomR
    public void copy2RecyclerPieces() {
        binding.layoutJigsaw.setAlpha(0.5f);
        binding.thumbnail.setImageResource(R.drawable.z_transparent);
        binding.thumbnail.invalidate();
        doNotUpdate = true;
        Log.w("copy2RecyclerPieces", "activeAdapter");
        new Thread(() -> {
            this.runOnUiThread(() -> {
                Log.w("copy2RecyclerPieces", "thumbnail");
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
                Log.w("copy2RecyclerPieces", "activeJigs");
                jigRecyclerView.setAdapter(activeAdapter);
                Bitmap thumb = showThumbnail.make();
                Log.w("copy2RecyclerPieces", "thumb map");
                binding.thumbnail.setImageBitmap(thumb);
                binding.layoutJigsaw.setAlpha(1f);
                doNotUpdate = false;
                Log.w("copy2RecyclerPieces", "thumb showImage");
            });
        }).start();
    }

    @Override
    protected void onPause() {
        Log.w("jigsaw","jigsaw onPause "+ gameMode);
        invalidateTimer.cancel();
        if (gameMode != GAME_GOBACK_TO_MAIN)
            gameMode = GAME_PAUSED;
        new GValGetPut().put(currGameLevel, gVal, this);

        history.time[gVal.gameLevel] = System.currentTimeMillis();
        int locked = 0;
        for (int cc = 0; cc < gVal.colNbr; cc++) {
            for (int rr = 0; rr < gVal.rowNbr; rr++) {
                if (gVal.jigTables[cc][rr].locked)
                    locked++;
            }
        }
        history.locked[gVal.gameLevel] = locked;
        history.percent[gVal.gameLevel] = locked * 100 / (gVal.colNbr * gVal.rowNbr);
        history.latest = gVal.gameLevel;

        if (historyIdx != -1) {
                histories.set(historyIdx, history);
        } else
            histories.add(history);
        new HistoryGetPut().put(histories, this);
        super.onPause();
    }

    private void save_params() {
        SharedPreferences sharedPref = getSharedPreferences("params", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedEditor = sharedPref.edit();
        sharedEditor.putBoolean("showBack", showBack);
        sharedEditor.putBoolean("vibrate", vibrate);
        sharedEditor.putBoolean("sound", sound);
        sharedEditor.apply();
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
