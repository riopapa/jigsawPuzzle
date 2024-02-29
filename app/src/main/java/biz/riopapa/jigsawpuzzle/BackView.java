package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ForeView.backBlink;
import static biz.riopapa.jigsawpuzzle.ForeView.reFresh;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import biz.riopapa.jigsawpuzzle.images.PieceImage;

public class BackView extends View {

    BackDraw backDraw;

    public BackView(Context context) {
        this(context, null);
    }

    public BackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(ActivityJigsawBinding binding) {
        backDraw = new BackDraw(binding);
    }
    protected void onDraw(@NonNull Canvas canvas){
        if (reFresh) {
            backBlink = false;
            backDraw.draw(canvas);
        }
    }

}