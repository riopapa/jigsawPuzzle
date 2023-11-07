package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;

public class PieceDraw {
    Paint pGrayed;
    public PieceDraw() {
        pGrayed = new Paint();
        pGrayed.setAlpha(100);
    }

    public void draw(Canvas canvas){
        canvas.save();

        // draw locked pieces first with .pic
        for (int c = 0; c < vars.showMaxX; c++) {
            for (int r = 0; r < vars.showMaxY; r++) {
                final int cc = c+vars.offsetC; final int rr = r+vars.offsetR;
                if (vars.jigTables[cc][rr].oLine == null)
                    pieceImage.makeAll(c+vars.offsetC, r+vars.offsetR);
                if (vars.jigTables[cc][rr].locked) {
                    if (vars.jigTables[cc][rr].count == 0)
                        canvas.drawBitmap(vars.jigTables[cc][rr].pic,
                                vars.baseX + c * vars.picISize, vars.baseY + r * vars.picISize, null);
                    else {
                        vars.jigTables[cc][rr].count--;
                        canvas.drawBitmap((vars.jigTables[cc][rr].count % 2 == 0) ?
                                        vars.jigTables[cc][rr].picBright : vars.jigTables[cc][rr].pic,
                                vars.baseX + c * vars.picISize, vars.baseY + r * vars.picISize, null);
                        if (vars.jigTables[cc][rr].count == 0) {
                            vars.jigTables[cc][rr].picBright = null;
                        }
                    }
                }
            }
        }
        // then empty pieces with .oline
        for (int c = 0; c < vars.showMaxX; c++) {
            for (int r = 0; r < vars.showMaxY; r++) {
                final int cc = c+vars.offsetC; final int rr = r+vars.offsetR;
                if (!vars.jigTables[cc][rr].locked) {
                    canvas.drawBitmap(vars.jigTables[cc][rr].oLine,
                            vars.baseX + c * vars.picISize, vars.baseY + r * vars.picISize, pGrayed);
                }
            }
        }

        // drawing floating pieces
        for (int cnt = 0; cnt < vars.fps.size(); cnt++) {
            FloatPiece fp = vars.fps.get(cnt);
            int c = fp.C;
            int r = fp.R;
            JigTable jt = vars.jigTables[c][r];
            if (fp.count == 0) { // normal pieceImage
                canvas.drawBitmap(fp.oLine, jt.posX, jt.posY, null);
                continue;
            }
            // animate just anchored
            if (fp.count > 0 && fp.mode == vars.aniANCHOR) {
                fp.count--;
                canvas.drawBitmap((fp.count % 2 == 0) ?
                                vars.jigTables[c][r].picBright : vars.jigTables[c][r].pic,
                        vars.jigTables[c][r].posX, vars.jigTables[c][r].posY, null);
                if (fp.count == 0) {
                    fp.mode = 0;
                }
                vars.fps.set(cnt, fp);
                continue;
            }
            // animate recycler to paint
            if (fp.count > 0 && fp.mode == vars.aniTO_PAINT) {  // animate from recycle to paintView
                fp.count--;
                canvas.drawBitmap((fp.count % 2 == 0) ?
                                vars.jigTables[c][r].picBright : vars.jigTables[c][r].pic,
                        vars.jigTables[c][r].posX, vars.jigTables[c][r].posY, null);
                vars.jigTables[c][r].posY -= vars.picISize / 4;
                if (fp.count == 0) {
                    fp.mode = 0;
                }
                vars.fps.set(cnt, fp);
            }
        }
        canvas.restore();
//        String txt = "onD c" + nowC +" r"+ nowR + "\noffCR "+vars.offsetC + " x " + vars.offsetR+"\n calc " + calcC +" x "+ calcR+"\n vars.fps "+vars.fps.size();
//        mActivity.runOnUiThread(() -> tvRight.setText(txt));

    }

}
