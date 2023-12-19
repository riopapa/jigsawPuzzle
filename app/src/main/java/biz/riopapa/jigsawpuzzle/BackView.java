package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activePos;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.doNotUpdate;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.dragX;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.jigRecyclerView;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowC;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.nowR;
import static biz.riopapa.jigsawpuzzle.ActivityMain.GAME_COMPLETED;
import static biz.riopapa.jigsawpuzzle.ActivityMain.INVALIDATE_INTERVAL;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenBottom;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBack;
import static biz.riopapa.jigsawpuzzle.ActivityMain.showBackCount;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import biz.riopapa.jigsawpuzzle.databinding.ActivityJigsawBinding;
import biz.riopapa.jigsawpuzzle.func.AnchorPiece;
import biz.riopapa.jigsawpuzzle.func.NearByFloatPiece;
import biz.riopapa.jigsawpuzzle.func.NearPieceBind;
import biz.riopapa.jigsawpuzzle.func.PiecePosition;
import biz.riopapa.jigsawpuzzle.func.PieceSelection;
import biz.riopapa.jigsawpuzzle.images.PieceImage;
import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class BackView extends View {

    BackDraw backDraw;

    public BackView(Context context) {
        this(context, null);
    }

    public BackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Activity activity, ActivityJigsawBinding binding) {
        backDraw = new BackDraw(binding);
    }
    protected void onDraw(@NonNull Canvas canvas){
        backDraw.draw(canvas);
    }

}