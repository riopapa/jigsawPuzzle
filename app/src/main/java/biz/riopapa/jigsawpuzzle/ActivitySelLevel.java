package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.history;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.historyIdx;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigBright;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigWhite;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static biz.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static biz.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static biz.riopapa.jigsawpuzzle.ActivityMain.GAME_STARTED;
import static biz.riopapa.jigsawpuzzle.ActivityMain.appVersion;
import static biz.riopapa.jigsawpuzzle.ActivityMain.congrats;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGame;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGameLevel;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currLevel;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fireWorks;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.histories;
import static biz.riopapa.jigsawpuzzle.ActivityMain.levelNames;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static biz.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

import biz.riopapa.jigsawpuzzle.databinding.ActivitySelLevelBinding;
import biz.riopapa.jigsawpuzzle.func.ClearGValValues;
import biz.riopapa.jigsawpuzzle.func.Congrat;
import biz.riopapa.jigsawpuzzle.func.DefineColsRows;
import biz.riopapa.jigsawpuzzle.func.FireWork;
import biz.riopapa.jigsawpuzzle.func.GValGetPut;
import biz.riopapa.jigsawpuzzle.func.HistoryGetPut;
import biz.riopapa.jigsawpuzzle.func.Masks;
import biz.riopapa.jigsawpuzzle.func.SetPicSizes;
import biz.riopapa.jigsawpuzzle.func.calcImageColor;
import biz.riopapa.jigsawpuzzle.model.GVal;
import biz.riopapa.jigsawpuzzle.model.History;

public class ActivitySelLevel extends AppCompatActivity {

    ActivitySelLevelBinding binding;
    AlertDialog alertDialog;
    Context context;
    DefineColsRows defineColsRows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        if (gameMode == GAME_PAUSED) {
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
        Log.w("s1 SelLevel", "onResume gameMode="+gameMode);
        if (gameMode == GAME_GOBACK_TO_MAIN) {
            Log.w("SelLevel"," go back to main");
            finish();
            return;
        }
        defineColsRows = new DefineColsRows();

        new calcImageColor();

//        chosenImageMap = new ImageStorage().getMap(GVal.selectedImageNbr);
//        new calcImageColor();
//        int width = screenX * 8 / 10;
//        int height = width * chosenImageHeight / chosenImageWidth;
//        if (height > screenY * 7 /10)
//            height = screenY * 7 / 10;
//        Bitmap selected = Bitmap.createScaledBitmap(chosenImageMap,
//                chosenImageWidth /2, chosenImageHeight /2, true);

//        binding.selImage.getLayoutParams().width = width;
//        binding.selImage.getLayoutParams().height = height;

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

        if (history.latest != -1)
            getGVal(history.latest, defineColsRows);
        else
            getGVal(2, defineColsRows);

        pieceImage = new PieceImage(this, gVal.imgOutSize, gVal.imgInSize);
        jigPic = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigOLine = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigBright = new Bitmap[gVal.colNbr][gVal.rowNbr];
        jigWhite = new Bitmap[gVal.colNbr][gVal.rowNbr];

        srcMaskMaps = new Masks().make(mContext, gVal.imgOutSize);
        outMaskMaps = new Masks().makeOut(mContext, gVal.imgOutSize);

        if (history.latest != -1) {
            int xSize = screenX;
            int ySize = xSize * chosenImageHeight / chosenImageWidth;

            Bitmap bitmap = Bitmap.createBitmap(xSize, ySize, Bitmap.Config.ARGB_8888,true);
            showLocked(binding, xSize, bitmap);
            binding.selImage.setImageBitmap(bitmap);

        } else
            binding.selImage.setImageBitmap(chosenImageMap);

        select_level();
    }

    private static void showLocked(ActivitySelLevelBinding binding, int xSize, Bitmap bitmap) {

        new Thread(() -> {
            Canvas canvas = new Canvas(bitmap);
            float szI = xSize / (gVal.colNbr+1);
            float szO = szI * 24 / 14;
            for (int rr = 0; rr < gVal.rowNbr; rr++) {
                for (int cc = 0; cc < gVal.colNbr; cc++) {
                    Bitmap picMap = pieceImage.makePic(cc, rr);
                    Bitmap oLMap = pieceImage.makeOline(picMap, cc, rr);
                    Bitmap sMap = Bitmap.createScaledBitmap(
                            ((gVal.jigTables[cc][rr].locked) ? oLMap : picMap),
                            (int) szO, (int) szO, false);
                    canvas.drawBitmap(sMap, cc * szI, rr * szI, null);
                    binding.selImage.setImageBitmap(bitmap);
                    binding.selImage.invalidate();
                }
            }
        }).start();

    }

    void select_level() {
        View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_select_level, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);

        alertDialog = builder.create();
        // show this dialog at the bottom
        Window window = alertDialog.getWindow();
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

    }

    private void setDialogInfo(View dialogView, int lvl, int activeId, int infoId, int newId) {
        String s;
        TextView tv;
        final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        defineColsRows.calc(lvl);
        tv = dialogView.findViewById(infoId);
        s  = levelNames[lvl] + "\n" + defineColsRows.col +"x"+ defineColsRows.row;
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
    }

    private void edit_table(View view) {
        alertDialog.dismiss();
        gameMode = GAME_STARTED; // target Image, level has been set
        int level = Integer.parseInt(view.getTag().toString());
        // if level > 9 then it means play new game
        getGVal(level, defineColsRows);

        Log.w("sel level", "GVal Level="+ currLevel);
        Intent intent = new Intent(this, ActivityJigsaw.class);
        startActivity(intent);
    }
    private void go_back(View view) {
        finish();
    }

    private void getGVal(int level, DefineColsRows defineColsRows) {
        currLevel = (level > 9) ? level - 10 : level;
        currGameLevel = currGame + currLevel;
        gVal = new GValGetPut().get(currGameLevel, this);
        if (gVal == null || level > 9 || !gVal.version.equals(appVersion)) {    // over 9 means clear and new game
            Log.w("gVal","newly defined "+currGameLevel);
            gVal = new GVal();
            defineColsRows.calc(currLevel);
            new GValGetPut().set(gVal, defineColsRows.col, defineColsRows.row);
            int sz = screenX * (10 - currLevel) / 10;
            new SetPicSizes(sz);
            new ClearGValValues();
        }
    }

}