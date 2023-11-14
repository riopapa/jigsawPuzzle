package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_STARTED;
import static com.riopapa.jigsawpuzzle.ActivityMain.gameLevels;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.riopapa.jigsawpuzzle.func.ImageChosen;
import com.riopapa.jigsawpuzzle.func.ImageStorage;
import com.riopapa.jigsawpuzzle.func.SetPicSizes;

public class ActivitySelLevel extends AppCompatActivity {

    ActivitySelLevelBinding binding;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (vars.gameMode == GAME_PAUSED) {
            vars.gameMode = GAME_STARTED; // target Image, level has been set
            new CalcCOLUMN_ROW(vars.gameLevel);
            Intent intent = new Intent(this, ActivityJigsaw.class);
            startActivity(intent);
        }
//        setContentView(R.layout.activity_sel_level);

        binding = ActivitySelLevelBinding.inflate(this.getLayoutInflater());
        setContentView(binding.getRoot());

//        selected = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vars.gameMode == GAME_GOBACK_TO_MAIN) {
            Log.w("SelLevel"," go back to main");
            finish();
            return;
        }
        Log.w("sl1 selectLevel","onResume");
        chosenImageMap = new ImageStorage().getMap(vars.selectedImageNbr);
        new ImageChosen();
        int width = screenX * 8 / 10;
        int height = width * chosenImageHeight / chosenImageWidth;
        if (height > screenY * 7 /10)
            height = screenY * 7 / 10;
        Bitmap selected = Bitmap.createScaledBitmap(chosenImageMap,
                chosenImageWidth /2, chosenImageHeight /2, true);

        binding.selImage.getLayoutParams().width = width;
        binding.selImage.getLayoutParams().height = height;
        binding.selImage.setImageBitmap(selected);

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
        alertDialog.show();

        TextView tv;
        String s;
        s = gameLevels[0]; new CalcCOLUMN_ROW(0); s += "\n" + vars.jigCOLs +" x "+vars.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_easy); tv.setText(s);
        dialogView.findViewById(R.id.lvl_easy).setOnClickListener(this::edit_table);

        s = gameLevels[1]; new CalcCOLUMN_ROW(1); s += "\n" + vars.jigCOLs +" x "+vars.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_normal); tv.setText(s);
        dialogView.findViewById(R.id.lvl_normal).setOnClickListener(this::edit_table);

        s = gameLevels[2]; new CalcCOLUMN_ROW(2); s += "\n" + vars.jigCOLs +" x "+vars.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_hard); tv.setText(s);
        dialogView.findViewById(R.id.lvl_hard).setOnClickListener(this::edit_table);

        s = gameLevels[3]; new CalcCOLUMN_ROW(3); s += "\n" + vars.jigCOLs +" x "+vars.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_expert); tv.setText(s);
        dialogView.findViewById(R.id.lvl_expert).setOnClickListener(this::edit_table);
    }

    private void edit_table(View view) {
        alertDialog.dismiss();
        vars.gameLevel = Integer.parseInt(view.getTag().toString());
        vars.gameMode = GAME_STARTED; // target Image, level has been set
        new CalcCOLUMN_ROW(vars.gameLevel);
        new SetPicSizes(screenX * (12 - vars.gameLevel) / 12);
        Log.w("sel level", "Game Level="+vars.gameLevel);
        Intent intent = new Intent(this, ActivityJigsaw.class);
        startActivity(intent);
    }

}