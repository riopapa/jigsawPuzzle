package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityMain.INVALIDATE_INTERVAL;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_appVersion;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_backColor;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGameLevel;
import static biz.riopapa.jigsawpuzzle.ActivityMain.debugMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.histories;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_showBack;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_sound;
import static biz.riopapa.jigsawpuzzle.ActivityMain.share_vibrate;
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
import biz.riopapa.jigsawpuzzle.func.DumpData;
import biz.riopapa.jigsawpuzzle.func.GValGetPut;
import biz.riopapa.jigsawpuzzle.func.HistoryGetPut;
import biz.riopapa.jigsawpuzzle.func.SharedParam;
import biz.riopapa.jigsawpuzzle.images.Congrat;
import biz.riopapa.jigsawpuzzle.images.FireWork;
import biz.riopapa.jigsawpuzzle.images.JigDone;
import biz.riopapa.jigsawpuzzle.images.Masks;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.images.ShowThumbnail;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;
import biz.riopapa.jigsawpuzzle.model.History;

public class ActivityJigsaw extends Activity {

    ActivityJigsawBinding binding;

    public static PieceImage pieceImage;

    public static RecyclerView jigRecyclerView;

    public static ForeView foreView;

    BackView backView;
    public static JigsawAdapter activeAdapter;
    public static ArrayList<Integer> activeJigs;

    public static Bitmap currImageMap;
    public static int colorOutline, colorLocked; // puzzle photo size (in dpi)
    public static Bitmap [][] jigPic, jigOLine, jigWhite, jigGray, jigLock;
    public static Bitmap[][] srcMaskMaps; // , outMaskMaps;
    public static Bitmap[] fireWorks, congrats, jigFinishes;

    public static int itemPos; // jigsaw slide x, y count
    public static int itemC, itemR, itemCR;   // fullImage pieceImage array column, row , x*10000+y
    public static int itemX, itemY; // absolute x,y while recycler piece dragging
    public static History history;
    public static int historyIdx;
    public static int allLockedMode = 0;    // 10: just all locked, 20: after all locked 99: completed
    public static int congCount = 0;

    public static int moveFrom, moveTo;

    public static boolean moving, reDrawOLine;

    int [] eyes = {R.drawable.z_eye_open, R.drawable.z_eye_half, R.drawable.z_eye_closed};
    ShowThumbnail showThumbnail;
    public static Timer loopTimer;
    int chkC, chkR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJigsawBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Log.w("jSaw", "onCreate gameMode=" + gameMode);
        screenBottom = screenY - gVal.recSize - gVal.recSize - gVal.picGap;
        if (fPhoneInchX > 3f)
            screenBottom += gVal.picHSize;

        pieceImage = new PieceImage(this, gVal.imgOutSize, gVal.imgInSize);

        srcMaskMaps = new Masks(this, pieceImage).make(mContext, gVal.imgOutSize);
//        outMaskMaps = new Masks(this, pieceImage).makeOut(mContext, gVal.imgOutSize);
        fireWorks = new FireWork().make(gVal.picOSize + gVal.picGap + gVal.picGap);
        congrats = new Congrat().make(screenX * 13 / 20);
        jigFinishes = new JigDone().make(screenX * 13 / 20);

        foreView = findViewById(R.id.paint_view);
        binding.paintView.getLayoutParams().height = screenBottom;

        backView = findViewById(R.id.back_view);
        binding.backView.getLayoutParams().height = screenBottom;
        backView.init(binding);
        foreView.init(binding);
        backBlink = true;
        foreBlink = true;
        moveFrom = -1; moveTo = -1;

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

        binding.showBack.setImageResource(eyes[share_showBack]);
        binding.showBack.setOnClickListener(v -> {
            share_showBack = (share_showBack + 1) % 3;
            binding.showBack.setImageResource(eyes[share_showBack]);
            save_params();
        });

        binding.vibrate.setImageResource((share_vibrate) ? R.drawable.z_vibrate_on : R.drawable.z_vibrate_off);
        binding.vibrate.setOnClickListener(v -> {
            share_vibrate = !share_vibrate;
            binding.vibrate.setImageResource((share_vibrate) ? R.drawable.z_vibrate_on : R.drawable.z_vibrate_off);
            save_params();
        });

//        binding.sound.setImageResource((share_sound) ? R.drawable.z_sound_on : R.drawable.z_sound_off);
//        binding.sound.setOnClickListener(v -> {
//            share_sound = !share_sound;
//            binding.sound.setImageResource((share_sound) ? R.drawable.z_sound_on : R.drawable.z_sound_off);
//            save_params();
//        });

//        binding.debugRight.setOnClickListener(v -> new DumpData());

        int[] backColors = {ContextCompat.getColor(mContext, R.color.backColor0),
                ContextCompat.getColor(mContext, R.color.backColor1),
                ContextCompat.getColor(mContext, R.color.backColor2)};

        binding.backcolor.setOnClickListener(v -> {
            share_backColor = (share_backColor + 1) % 3;
            binding.layoutJigsaw.setBackgroundColor(backColors[share_backColor]);
            save_params();
        });

        jigRecyclerView = findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = gVal.recSize + gVal.picGap;
        activeAdapter = new JigsawAdapter();
        jigRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        jigRecyclerView.setLayoutManager(mLinearLayoutManager);

        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(activeAdapter, foreView.pieceLock,
                        pieceImage, foreView.pieceBind);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(jigRecyclerView);

        jigRecyclerView.setAdapter(activeAdapter);

        new DefineControlButton(binding);
        copy2RecyclerPieces();

        if (debugMode) {
            // followings are to test congrats
            for (int ac = 0; ac < gVal.colNbr; ac++) {
                for (int ar = 0; ar < gVal.rowNbr; ar++) {
                    if (new Random().nextInt(8) > 1) {
                        gVal.jigTables[ac][ar].locked = true;
                        jigOLine[ac][ar] = pieceImage.makeOline(jigPic[ac][ar], ac, ar);
                    }
                }
            }
        }
        chkC = 0;
        chkR = 0;
        loopTimer = new Timer();
        reDrawOLine = true;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (gameMode == ActivityMain.GMode.ALL_DONE) {
                    loopTimer.cancel();
                    loopTimer = null;
                    finish();
                }
                chkC++;
                if (chkC > gVal.colNbr - 1) {
                    chkC = 0;
                    chkR++;
                    if (chkR > gVal.rowNbr - 1) {
                        chkR = 0;
                    }
                }
                if (backBlink)
                    backView.invalidate();
                if (foreBlink)
                    foreView.invalidate();

            }
        };
        loopTimer.schedule(timerTask, INVALIDATE_INTERVAL, INVALIDATE_INTERVAL);

    }
    // build recycler from all pieces within in leftC, rightC, topR, bottomR
    void copy2RecyclerPieces() {

        binding.moveLeft.setVisibility ((gVal.offsetC == 0) ? View.INVISIBLE: View.VISIBLE);
        binding.moveRight.setVisibility ((gVal.offsetC == gVal.colNbr-gVal.showMaxX) ? View.INVISIBLE: View.VISIBLE);
        binding.moveUp.setVisibility((gVal.offsetR == 0)? View.INVISIBLE: View.VISIBLE);
        binding.moveDown.setVisibility((gVal.offsetR == gVal.rowNbr-gVal.showMaxY)? View.INVISIBLE: View.VISIBLE);

        binding.thumbnail.setImageResource(R.drawable.z_transparent);
        binding.thumbnail.invalidate();
        activeJigs = new ArrayList<>();

        jigPic = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigOLine = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigGray = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigWhite = new Bitmap[gVal.colNbr][gVal.rowNbr];

        for (int i = 0; i < gVal.allPossibleJigs.size(); i++) {
            int tmp = gVal.allPossibleJigs.get(i) -  10000;
            int ac = tmp / 100;
            int ar = tmp - ac * 100;
            if (!gVal.jigTables[ac][ar].locked &&
                    ac >= gVal.offsetC && ac < gVal.offsetC + gVal.showMaxX && ar >= gVal.offsetR && ar < gVal.offsetR + gVal.showMaxY) {
                if (isNotInFp(ac,ar))
                    activeJigs.add(tmp + 10000);
            }
        }

        jigRecyclerView.setAdapter(activeAdapter);
        binding.thumbnail.setImageBitmap(showThumbnail.make(currImageMap));
        binding.layoutJigsaw.setAlpha(1f);
        binding.layoutJigsaw.invalidate();
        allLockedMode = 0;
        congCount = 0;
        backBlink = true;
    }

    boolean isNotInFp(int ac, int ar) {
        for (int i = 0; i < gVal.fps.size(); i++) {
            FloatPiece fp = gVal.fps.get(i);
            if (fp.C == ac && fp.R == ar)
                return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        if (loopTimer != null) {
            loopTimer.cancel();
            loopTimer = null;
        }
        Log.w("jigsaw","jigsaw onPause "+ gameMode);
        releaseAll();
        if (gameMode != ActivityMain.GMode.TO_MAIN)
            gameMode = ActivityMain.GMode.PAUSED;

        new GValGetPut().put(currGameLevel, gVal, this);
        save_History();

        System.gc();
        if (gameMode == ActivityMain.GMode.PAUSED)
            finish();
        super.onPause();
    }

    public static void save_History() {
        history.time[gVal.level] = System.currentTimeMillis();
        int locked = 0;
        for (int ac = 0; ac < gVal.colNbr; ac++) {
            for (int ar = 0; ar < gVal.rowNbr; ar++) {
                if (gVal.jigTables[ac][ar].locked)
                    locked++;
            }
        }
        history.locked[gVal.level] = locked;
        history.percent[gVal.level] = locked * 100 / (gVal.colNbr * gVal.rowNbr);
        history.latestLvl = gVal.level;

        if (historyIdx != -1) {
                histories.set(historyIdx, history);
        } else
            histories.add(history);
        new HistoryGetPut().put(histories, mContext);
    }

    private void releaseAll() {
        jigRecyclerView = null;
        foreView = null;
        backView = null;
        activeAdapter = null;
        jigPic = null;
        jigOLine = null;
        jigWhite = null;
        jigGray = null;
        srcMaskMaps = null;
         // outMaskMaps = null;
        fireWorks = null;
        congrats = null;
        jigFinishes = null;
    }

    private void save_params() {
        new SharedParam().put(this);
        backView.invalidate();
        foreView.invalidate();
    }

    @Override
    public void onBackPressed() {
        finish();
        if (loopTimer != null) {
            loopTimer.cancel();
            loopTimer = null;
        }
        Log.w("jigsaw","jigsaw onBackPressed");
        new SharedParam().put(this);
        new GValGetPut().put(currGameLevel, gVal, this);
        releaseAll();
        gameMode = ActivityMain.GMode.TO_MAIN;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Log.w("onDestroy","release Memory");
        releaseAll();
        super.onDestroy();
    }
}
