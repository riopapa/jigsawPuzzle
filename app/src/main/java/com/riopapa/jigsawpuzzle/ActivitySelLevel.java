package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_GOBACK_TO_MAIN;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_PAUSED;
import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_STARTED;
import static com.riopapa.jigsawpuzzle.ActivityMain.levelNames;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenY;
import static com.riopapa.jigsawpuzzle.ActivityMain.GVal;

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
import com.riopapa.jigsawpuzzle.func.CalcCOLUMN_ROW;
import com.riopapa.jigsawpuzzle.func.SetPicSizes;

public class ActivitySelLevel extends AppCompatActivity {

    ActivitySelLevelBinding binding;
    AlertDialog alertDialog;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        if (GVal.gameMode == GAME_PAUSED) {
            GVal.gameMode = GAME_STARTED; // target Image, level has been set
            new CalcCOLUMN_ROW(GVal.gameLevel);
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
        if (GVal.gameMode == GAME_GOBACK_TO_MAIN) {
            Log.w("SelLevel"," go back to main");
            finish();
            return;
        }
        Log.w("sl1 selectLevel","onResume");
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
        s = levelNames[0]; new CalcCOLUMN_ROW(0); s += "\n" + GVal.jigCOLs +" x "+ GVal.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_easy); tv.setText(s);
        dialogView.findViewById(R.id.lvl_easy).setOnClickListener(this::edit_table);

        s = levelNames[1]; new CalcCOLUMN_ROW(1); s += "\n" + GVal.jigCOLs +" x "+ GVal.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_normal); tv.setText(s);
        dialogView.findViewById(R.id.lvl_normal).setOnClickListener(this::edit_table);

        s = levelNames[2]; new CalcCOLUMN_ROW(2); s += "\n" + GVal.jigCOLs +" x "+ GVal.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_hard); tv.setText(s);
        dialogView.findViewById(R.id.lvl_hard).setOnClickListener(this::edit_table);

        s = levelNames[3]; new CalcCOLUMN_ROW(3); s += "\n" + GVal.jigCOLs +" x "+ GVal.jigROWs;
        tv = dialogView.findViewById(R.id.lvl_expert); tv.setText(s);
        dialogView.findViewById(R.id.lvl_expert).setOnClickListener(this::edit_table);
    }

    private void edit_table(View view) {
        alertDialog.dismiss();
        GVal.gameLevel = Integer.parseInt(view.getTag().toString());
        new CalcCOLUMN_ROW(GVal.gameLevel);

        GVal.gameMode = GAME_STARTED; // target Image, level has been set
        new SetPicSizes(screenX * (12 - GVal.gameLevel) / 12);
        Log.w("sel level", "GVal Level="+ GVal.gameLevel);
        Intent intent = new Intent(this, ActivityJigsaw.class);
        startActivity(intent);
    }

}