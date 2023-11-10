package com.riopapa.jigsawpuzzle;

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
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.riopapa.jigsawpuzzle.databinding.ActivitySelLevelBinding;
import com.riopapa.jigsawpuzzle.func.CalcCOLUMN_ROW;

public class ActivitySelLevel extends AppCompatActivity {

    ActivitySelLevelBinding binding;
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
        int width = screenX * 8 / 10;
        int height = width * vars.selectedHeight / vars.selectedWidth;
        if (height > screenY * 7 /10)
            height = screenY * 7 / 10;
        Bitmap selected = Bitmap.createScaledBitmap(vars.selectedImage,
                vars.selectedWidth/2, vars.selectedHeight/2, true);

        binding.selImage.getLayoutParams().width = width;
        binding.selImage.getLayoutParams().height = height;
        binding.selImage.setImageBitmap(selected);

        select_level();
//        selected = null;
    }

    void select_level() {
        View dialogView = this.getLayoutInflater().inflate(R.layout.select_level, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
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
        dialogView.findViewById(R.id.lvl_easy).setOnClickListener(this::edit_table);
    }

    private void edit_table(View view) {
        vars.gameLevel = Integer.parseInt(view.getTag().toString());
        vars.gameMode = GAME_STARTED; // target Image, level has been set
        new CalcCOLUMN_ROW(vars.gameLevel);
        Intent intent = new Intent(this, ActivityJigsaw.class);
        startActivity(intent);
    }

}