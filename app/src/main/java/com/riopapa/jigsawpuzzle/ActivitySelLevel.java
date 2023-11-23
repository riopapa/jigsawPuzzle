package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_STARTED;
import static com.riopapa.jigsawpuzzle.ActivityMain.currGame;
import static com.riopapa.jigsawpuzzle.ActivityMain.currGameLevel;
import static com.riopapa.jigsawpuzzle.ActivityMain.currLevel;
import static com.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static com.riopapa.jigsawpuzzle.ActivityMain.levelNames;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.riopapa.jigsawpuzzle.func.DefineColsRows;
import com.riopapa.jigsawpuzzle.func.GValGetPut;
import com.riopapa.jigsawpuzzle.func.SetPicSizes;

public class ActivitySelLevel extends AppCompatActivity {

    ActivitySelLevelBinding binding;
    AlertDialog alertDialog;
    Context context;
    DefineColsRows jigColumnRow;
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
        jigColumnRow = new DefineColsRows();
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
        binding.selImage.setImageBitmap(chosenImageMap);

        select_level();

    }

    void select_level() {
        View dialogView = this.getLayoutInflater().inflate(R.layout.select_level, null);

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
        alertDialog.setCancelable(false);
        alertDialog.show();

        TextView tv;
        String s;
        
        
        s = levelNames[0]; jigColumnRow.calc(0);
        s += "\n" + jigColumnRow.col +" x "+ jigColumnRow.row;
        tv = dialogView.findViewById(R.id.lvl_easy); tv.setText(s);
        dialogView.findViewById(R.id.lvl_easy).setOnClickListener(this::edit_table);

        s = levelNames[1]; jigColumnRow.calc(1);
        s += "\n" + jigColumnRow.col +" x "+ jigColumnRow.row;
        tv = dialogView.findViewById(R.id.lvl_normal); tv.setText(s);
        dialogView.findViewById(R.id.lvl_normal).setOnClickListener(this::edit_table);

        s = levelNames[2]; jigColumnRow.calc(2);
        s += "\n" + jigColumnRow.col +" x "+ jigColumnRow.row;
        tv = dialogView.findViewById(R.id.lvl_hard); tv.setText(s);
        dialogView.findViewById(R.id.lvl_hard).setOnClickListener(this::edit_table);

        s = levelNames[3]; jigColumnRow.calc(3);
        s += "\n" + jigColumnRow.col +" x "+ jigColumnRow.row;
        tv = dialogView.findViewById(R.id.lvl_expert); tv.setText(s);
        dialogView.findViewById(R.id.lvl_expert).setOnClickListener(this::edit_table);


        s = levelNames[0] + "\nNew"; jigColumnRow.calc(0);
        s += "\n" + jigColumnRow.col +" x "+ jigColumnRow.row;
        tv = dialogView.findViewById(R.id.lvl_easyn); tv.setText(s);
        dialogView.findViewById(R.id.lvl_easyn).setOnClickListener(this::edit_table);

        s = levelNames[1] + "\nNew"; jigColumnRow.calc(1);
        s += "\n" + jigColumnRow.col +" x "+ jigColumnRow.row;
        tv = dialogView.findViewById(R.id.lvl_normaln); tv.setText(s);
        dialogView.findViewById(R.id.lvl_normaln).setOnClickListener(this::edit_table);

        s = levelNames[2] + "\nNew"; jigColumnRow.calc(2);
        s += "\n" + jigColumnRow.col +" x "+ jigColumnRow.row;
        tv = dialogView.findViewById(R.id.lvl_hardn); tv.setText(s);
        dialogView.findViewById(R.id.lvl_hardn).setOnClickListener(this::edit_table);

        s = levelNames[3] + "\nNew"; jigColumnRow.calc(3);
        s += "\n" + jigColumnRow.col +" x "+ jigColumnRow.row;
        tv = dialogView.findViewById(R.id.lvl_expertn); tv.setText(s);
        dialogView.findViewById(R.id.lvl_expertn).setOnClickListener(this::edit_table);

    }

    private void edit_table(View view) {
        alertDialog.dismiss();
        gameMode = GAME_STARTED; // target Image, level has been set
        int level = Integer.parseInt(view.getTag().toString());
        // if level > 9 then it means play new game
        currLevel = (level > 9) ? level - 10 : level;
        currGameLevel = currGame + currLevel;
        gVal = new GValGetPut().get(currGameLevel, this);
        if (gVal == null || level > 9) {
            Log.w("gVal","new "+currGameLevel);
            gVal = new GVal();
            jigColumnRow.calc(currLevel);
            new GValGetPut().set(gVal, jigColumnRow.col, jigColumnRow.row);
            int sz = screenX * (10 - currLevel) / 10;
            new SetPicSizes(sz);
            new ClearGValValues();
//            if (gVal.picISize * gVal.rowNbr < screenBottom * 2/3) {
//                new SetPicSizes(sz * 4 / 3);
//                new ClearGValValues();
//            }
        }

        Log.w("sel level", "GVal Level="+ currLevel);
        Intent intent = new Intent(this, ActivityJigsaw.class);
        startActivity(intent);
    }

}