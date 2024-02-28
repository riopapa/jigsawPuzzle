package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.currImageMap;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.history;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.historyIdx;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigGray;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigLock;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigWhite;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGame;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGameLevel;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currLevel;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.histories;
import static biz.riopapa.jigsawpuzzle.ActivityMain.levelNames;
import static biz.riopapa.jigsawpuzzle.ActivityMain.nowVersion;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import biz.riopapa.jigsawpuzzle.databinding.ActivitySelLevelBinding;
import biz.riopapa.jigsawpuzzle.func.CalcImageColor;
import biz.riopapa.jigsawpuzzle.func.ClearGValValues;
import biz.riopapa.jigsawpuzzle.func.DefineColsRows;
import biz.riopapa.jigsawpuzzle.func.DefineFullRecyclePieces;
import biz.riopapa.jigsawpuzzle.func.DefineTableWalls;
import biz.riopapa.jigsawpuzzle.func.GValGetPut;
import biz.riopapa.jigsawpuzzle.func.HistoryGetPut;
import biz.riopapa.jigsawpuzzle.func.SetPicSizes;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.GVal;
import biz.riopapa.jigsawpuzzle.model.History;
import biz.riopapa.jigsawpuzzle.model.JigTable;

public class ActivitySelLevel extends AppCompatActivity {

    ActivitySelLevelBinding binding;
    AlertDialog alertDialog;
    Context context;
    PieceImage pieceImage;
    int imgFullWidth, imgFullHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        if (gameMode == ActivityMain.GMode.PAUSED) {
//            new DefineColsRows(gVal.gameLevel);
            Intent intent = new Intent(this, ActivityJigsaw.class);
            startActivity(intent);
        }

        binding = ActivitySelLevelBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.loading_circle).setVisibility(View.VISIBLE);
        Log.w("s1 SelLevel", "onResume gameMode="+gameMode);
        if (gameMode == ActivityMain.GMode.TO_MAIN) {
            Log.w("SelLevel"," go back to main");
            finish();
            return;
        }
        imgFullWidth = currImageMap.getWidth();
        imgFullHeight = currImageMap.getHeight();

        new CalcImageColor();

        history = null;
        historyIdx = -1;
        if (histories == null || histories.size() == 0) {
            histories = new HistoryGetPut().get(this);
        }
        for (int i = 0; i < histories.size(); i++) {
            if (histories.get(i).game.equals(currGame)) {
                history = histories.get(i);
                historyIdx = i;
            }
        }
        if (historyIdx == -1) {
            history = new History();
            history.game = currGame;
        }

        if (history.latestLvl != -1)
            getGVal(history.latestLvl);
        else
            getGVal(2);

        pieceImage = new PieceImage(this, gVal.imgOutSize, gVal.imgInSize);
        jigPic = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigOLine = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigWhite = new Bitmap[gVal.colNbr][gVal.rowNbr];

        binding.selImage.setImageBitmap(currImageMap);
        binding.selImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        new Thread(this::select_level).start();
    }

    void select_level() {

        int upDown;
        TranslateAnimation animation;
        if (imgFullWidth < imgFullHeight) {
            upDown = imgFullHeight / 12;
            animation = new TranslateAnimation(0.0f, 0.0f, upDown, -upDown-upDown);
        } else {
            upDown = imgFullWidth / 12;
            animation = new TranslateAnimation(-upDown, upDown, 0f, -0f);
        }
        animation.setDuration(5000); // animation duration
        animation.setRepeatCount(-1); // animation repeat count -1 means infinite
        animation.setRepeatMode(Animation.REVERSE); // repeat animation (left to right, right to left)
        animation.setFillAfter(false);
        binding.selImage.startAnimation(animation);//your_view for mine is imageView

        this.runOnUiThread(() -> {

            View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_select_level, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
            builder.setView(dialogView);

            alertDialog = builder.create();
            // show this dialog at the bottom
            Window window = alertDialog.getWindow();
            assert window != null;
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            // give some bottom space on level dialog
            alertDialog.getWindow().getAttributes().height = (int) (screenY * 0.7);
            alertDialog.getWindow().getAttributes().width = screenX;
            alertDialog.setCancelable(false);
            alertDialog.show();

            setDialogInfo(dialogView, 0, R.id.lvl_easy, R.id.lvl_eInfo, R.id.lvl_eNew);
            setDialogInfo(dialogView, 1, R.id.lvl_normal, R.id.lvl_nInfo, R.id.lvl_nNew);
            setDialogInfo(dialogView, 2, R.id.lvl_hard, R.id.lvl_hInfo, R.id.lvl_hNew);
            setDialogInfo(dialogView, 3, R.id.lvl_guru, R.id.lvl_gInfo, R.id.lvl_gNew);
            dialogView.findViewById(R.id.go_back).setOnClickListener(this::go_back);
            findViewById(R.id.loading_circle).setVisibility(View.GONE);
            TextView tv = dialogView.findViewById(R.id.gameInfo);
            tv.setText("GAME : "+currGameLevel);
        });
    }

    private void setDialogInfo(View dialogView, int lvl, int activeId, int infoId, int newId) {
        String s;
        TextView tv;
        final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd", Locale.getDefault());
        int [] colRow = new DefineColsRows().calc(lvl, imgFullWidth, imgFullHeight);
        tv = dialogView.findViewById(infoId);
        s  = levelNames[lvl] + "\n" + colRow[0] +"x"+ colRow[1];
        tv.setText(s);

        tv = dialogView.findViewById(activeId);
        s  = "\n" + history.percent[lvl] + "%" + "\n" + sdf.format(history.time[lvl])+"\n";
        tv.setText(s);
        if (history.time[lvl] != 0) {
            dialogView.findViewById(activeId).setOnClickListener(this::edit_table);
            dialogView.findViewById(activeId).setVisibility(View.VISIBLE);
        } else
            dialogView.findViewById(activeId).setVisibility(View.INVISIBLE);
        dialogView.findViewById(newId).setOnClickListener(this::edit_table);
        if (history.latestLvl == lvl && history.percent[lvl] != 100) {
            TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, -10.0f, 10.0f);
            // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)
            animation.setDuration(200); // animation duration
            animation.setRepeatCount(-1); // animation repeat count -1 means infinite
            animation.setRepeatMode(Animation.REVERSE); // repeat animation (left to right, right to left)
            animation.setFillAfter(false);
            dialogView.findViewById(activeId).startAnimation(animation);//your_view for mine is imageView
        }
    }

    private void edit_table(View view) {
        alertDialog.dismiss();
        
        gameMode = ActivityMain.GMode.STARTED; // target Image, level has been set
        int level = Integer.parseInt(view.getTag().toString());
        // if level > 9 then it means play new game
        getGVal(level);
        pieceImage = null;

        jigPic = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigOLine = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigWhite = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigGray = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigLock = new Bitmap[gVal.colNbr][gVal.rowNbr];

        System.gc();
        Intent intent = new Intent(this, ActivityJigsaw.class);
        startActivity(intent);
    }

    private void go_back(View view) {
        alertDialog.dismiss();
        pieceImage = null;
        jigPic = null;
        jigOLine = null;
        jigWhite = null;
        System.gc();

        finish();
    }

    private void getGVal(int level) {
        currLevel = (level > 9) ? level - 10 : level;
        currGameLevel = currGame + currLevel;
        gVal = new GValGetPut().get(currGameLevel, this);
        if (gVal == null || level > 9 || !gVal.version.equals(nowVersion)) {
            // over 9 means clear and new game
            Log.w("gVal","newly defined "+currGameLevel);
            gVal = new GVal();
            set(gVal);
            new SetPicSizes(screenX * (14 - currLevel) / 14);
            new ClearGValValues();
        }
    }

    void set(GVal gVal) {

        gVal.game = currGame;
        gVal.version = nowVersion;
        gVal.level =  currLevel;
        gVal.imgFullWidth = imgFullWidth;
        gVal.imgFullHeight = imgFullHeight;
        gVal.time = System.currentTimeMillis();
        gVal.fps = new ArrayList<>();
        int [] colRow = new DefineColsRows().calc(currLevel, imgFullWidth, imgFullHeight);
        gVal.colNbr = colRow[0];
        gVal.rowNbr = colRow[1];
        new SetPicSizes(screenX * (16 - currLevel) / 16);

        float szW = (float) gVal.imgFullWidth / (float) (gVal.colNbr + 1);
        float szH = (float) gVal.imgFullHeight / (float) (gVal.rowNbr + 1);
        gVal.imgInSize = (szH > szW) ? (int) szW : (int) szH;
        gVal.imgGapSize = gVal.imgInSize * 5 / 14;
        gVal.imgOutSize = gVal.imgInSize + gVal.imgGapSize + gVal.imgGapSize;
        gVal.jigTables = new JigTable[gVal.colNbr][gVal.rowNbr];
        new DefineTableWalls(gVal.jigTables);
        new ClearGValValues();
        new DefineFullRecyclePieces();
    }

}