package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.historyIdx;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.history;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigBright;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigWhite;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_STARTED;
import static com.riopapa.jigsawpuzzle.ActivityMain.appVersion;
import static com.riopapa.jigsawpuzzle.ActivityMain.congrats;
import static com.riopapa.jigsawpuzzle.ActivityMain.currGame;
import static com.riopapa.jigsawpuzzle.ActivityMain.currGameLevel;
import static com.riopapa.jigsawpuzzle.ActivityMain.currLevel;
import static com.riopapa.jigsawpuzzle.ActivityMain.fireWorks;
import static com.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static com.riopapa.jigsawpuzzle.ActivityMain.histories;
import static com.riopapa.jigsawpuzzle.ActivityMain.levelNames;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.outMaskMaps;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static com.riopapa.jigsawpuzzle.ActivityMain.srcMaskMaps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.riopapa.jigsawpuzzle.databinding.ActivitySelLevelBinding;
import com.riopapa.jigsawpuzzle.func.ClearGValValues;
import com.riopapa.jigsawpuzzle.func.Congrat;
import com.riopapa.jigsawpuzzle.func.DefineColsRows;
import com.riopapa.jigsawpuzzle.func.FireWork;
import com.riopapa.jigsawpuzzle.func.GValGetPut;
import com.riopapa.jigsawpuzzle.func.Masks;
import com.riopapa.jigsawpuzzle.func.SetPicSizes;
import com.riopapa.jigsawpuzzle.model.GVal;
import com.riopapa.jigsawpuzzle.model.History;

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
        fireWorks = new FireWork().make(mContext, gVal.picOSize + gVal.picGap + gVal.picGap);
        congrats = new Congrat().make(mContext, gVal.picOSize + gVal.picGap + gVal.picGap);

        if (history.latest != -1) {
            int xSize = screenX;
            int ySize = xSize * chosenImageHeight / chosenImageWidth;

            Bitmap bitmap = Bitmap.createBitmap(xSize, ySize, Bitmap.Config.ARGB_8888,true);
            Canvas canvas = new Canvas(bitmap);
            float szI = xSize / (gVal.colNbr+1);
            float szO = szI * (14+5+5) / 14;
            for (int cc = 0; cc < gVal.colNbr; cc++) {
                for (int rr = 0; rr < gVal.rowNbr; rr++) {
                    Bitmap picMap = pieceImage.buildPic(cc, rr);
                    Bitmap oLMap = pieceImage.buildOline(picMap, cc, rr);
                    Bitmap lockMap = Bitmap.createScaledBitmap(
                            (gVal.jigTables[cc][rr].locked) ? oLMap : picMap, (int) szO, (int) szO, true);
                    canvas.drawBitmap(lockMap, cc * szI, rr * szI, null);
                }
            }
            binding.selImage.setImageBitmap(bitmap);

        } else
            binding.selImage.setImageBitmap(chosenImageMap);

        select_level();

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
        doNotUpdate = true;
        // give some bottom space on level dialog
        alertDialog.getWindow().getAttributes().height = (int) (screenY * 0.7);
        alertDialog.getWindow().getAttributes().width = screenX;
        alertDialog.setCancelable(false);
        alertDialog.show();

        TextView tv;
        String s;

        s = levelNames[0]; defineColsRows.calc(0);
        s += "\n" + defineColsRows.col +" x "+ defineColsRows.row;
        s += "\n" + ((history.percent[0] > 99) ? "Done" : history.percent[0] + "%");
        tv = dialogView.findViewById(R.id.lvl_easy); tv.setText(s);
        if (history.latest == 0)
            tv.setBackgroundColor(0xFFCCDDEE);
        else
            tv.setAlpha((history.time[0] == 0)? 0.4f: 1f);

        dialogView.findViewById(R.id.lvl_easy).setOnClickListener(this::edit_table);

        s = levelNames[1]; defineColsRows.calc(1);
        s += "\n" + defineColsRows.col +" x "+ defineColsRows.row;
        s += "\n" + ((history.percent[1] > 99) ? "Done" : history.percent[1] + "%");
        tv = dialogView.findViewById(R.id.lvl_normal); tv.setText(s);
        if (history.latest == 1)
            tv.setBackgroundColor(0xFFCCDDEE);
        else
            tv.setAlpha((history.time[1] == 0)? 0.4f: 1f);
        dialogView.findViewById(R.id.lvl_normal).setOnClickListener(this::edit_table);

        s = levelNames[2]; defineColsRows.calc(2);
        s += "\n" + defineColsRows.col +" x "+ defineColsRows.row;
        s += "\n" + ((history.percent[2] > 99) ? "Done" : history.percent[2] + "%");
        tv = dialogView.findViewById(R.id.lvl_hard); tv.setText(s);
        if (history.latest == 2)
            tv.setBackgroundColor(0xFFCCDDEE);
        else
            tv.setAlpha((history.time[2] == 0)? 0.4f: 1f);
        dialogView.findViewById(R.id.lvl_hard).setOnClickListener(this::edit_table);

        s = levelNames[3]; defineColsRows.calc(3);
        s += "\n" + defineColsRows.col +" x "+ defineColsRows.row;
        s += "\n" + ((history.percent[3] > 99) ? "Done" : history.percent[3] + "%");
        tv = dialogView.findViewById(R.id.lvl_expert); tv.setText(s);
        if (history.latest == 3)
            tv.setBackgroundColor(0xFFCCDDEE);
        else
            tv.setAlpha((history.time[3] == 0)? 0.4f: 1f);
        dialogView.findViewById(R.id.lvl_expert).setOnClickListener(this::edit_table);


        s = levelNames[0] + "\nNew"; defineColsRows.calc(0);
        s += "\n" + defineColsRows.col +" x "+ defineColsRows.row;
        tv = dialogView.findViewById(R.id.lvl_easyn); tv.setText(s);
        tv.setAlpha((history.time[0] == 0)? 1f: 0.5f);
        dialogView.findViewById(R.id.lvl_easyn).setOnClickListener(this::edit_table);

        s = levelNames[1] + "\nNew"; defineColsRows.calc(1);
        s += "\n" + defineColsRows.col +" x "+ defineColsRows.row;
        tv = dialogView.findViewById(R.id.lvl_normaln); tv.setText(s);
        tv.setAlpha((history.time[1] == 0)? 1f: 0.5f);
        dialogView.findViewById(R.id.lvl_normaln).setOnClickListener(this::edit_table);

        s = levelNames[2] + "\nNew"; defineColsRows.calc(2);
        s += "\n" + defineColsRows.col +" x "+ defineColsRows.row;
        tv = dialogView.findViewById(R.id.lvl_hardn); tv.setText(s);
        tv.setAlpha((history.time[2] == 0)? 1f: 0.5f);
        dialogView.findViewById(R.id.lvl_hardn).setOnClickListener(this::edit_table);

        s = levelNames[3] + "\nNew"; defineColsRows.calc(3);
        s += "\n" + defineColsRows.col +" x "+ defineColsRows.row;
        tv = dialogView.findViewById(R.id.lvl_expertn); tv.setText(s);
        tv.setAlpha((history.time[3] == 0)? 1f: 0.5f);
        dialogView.findViewById(R.id.lvl_expertn).setOnClickListener(this::edit_table);

        dialogView.findViewById(R.id.go_back).setOnClickListener(this::go_back);

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
        gVal = new GValGetPut().get(currGameLevel, mContext);
        if (gVal == null || level > 9 || gVal.version < appVersion) {    // over 9 means clear and new game
            Log.w("gVal","new "+currGameLevel);
            gVal = new GVal();
            defineColsRows.calc(currLevel);
            new GValGetPut().set(gVal, defineColsRows.col, defineColsRows.row);
            int sz = screenX * (10 - currLevel) / 10;
            new SetPicSizes(sz);
            new ClearGValValues();
        }
    }

}