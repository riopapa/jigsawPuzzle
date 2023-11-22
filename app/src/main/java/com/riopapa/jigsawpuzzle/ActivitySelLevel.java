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
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.riopapa.jigsawpuzzle.databinding.ActivitySelLevelBinding;
import com.riopapa.jigsawpuzzle.func.CalcCOLUMN_ROW;
import com.riopapa.jigsawpuzzle.func.GValGetPut;
import com.riopapa.jigsawpuzzle.func.SetPicSizes;

public class ActivitySelLevel extends AppCompatActivity {

    ActivitySelLevelBinding binding;
    AlertDialog alertDialog;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        if (gameMode == GAME_PAUSED) {
//            new CalcCOLUMN_ROW(gVal.gameLevel);
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
//        chosenImageMap = new ImageStorage().getMap(GVal.selectedImageNbr);
//        new ImageChosen();
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
        s = levelNames[0]; new CalcCOLUMN_ROW(0); s += "\n" + gVal.jigCOLs +" x "+ gVal.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_easy); tv.setText(s);
        dialogView.findViewById(R.id.lvl_easy).setOnClickListener(this::edit_table);

        s = levelNames[1]; new CalcCOLUMN_ROW(1); s += "\n" + gVal.jigCOLs +" x "+ gVal.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_normal); tv.setText(s);
        dialogView.findViewById(R.id.lvl_normal).setOnClickListener(this::edit_table);

        s = levelNames[2]; new CalcCOLUMN_ROW(2); s += "\n" + gVal.jigCOLs +" x "+ gVal.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_hard); tv.setText(s);
        dialogView.findViewById(R.id.lvl_hard).setOnClickListener(this::edit_table);

        s = levelNames[3]; new CalcCOLUMN_ROW(3); s += "\n" + gVal.jigCOLs +" x "+ gVal.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_expert); tv.setText(s);
        dialogView.findViewById(R.id.lvl_expert).setOnClickListener(this::edit_table);
    }

    private void edit_table(View view) {
        alertDialog.dismiss();
        gameMode = GAME_STARTED; // target Image, level has been set
        currLevel = Integer.parseInt(view.getTag().toString());
        currGameLevel = currGame + currLevel;
        gVal = new GValGetPut().get(currGameLevel, this);
        if (gVal == null) {
            Log.w("gVal","new "+currGameLevel);
            gVal = new GVal();
            new GValGetPut().set(gVal);
        }

        Log.w("sel level", "GVal Level="+ currLevel);
        Intent intent = new Intent(this, ActivityJigsaw.class);
        startActivity(intent);
    }

}