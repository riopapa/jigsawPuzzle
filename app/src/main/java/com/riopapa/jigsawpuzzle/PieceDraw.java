package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigBright;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigOLine;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.jigPic;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;
import static com.riopapa.jigsawpuzzle.ActivityJigsaw.rnd;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_ANCHOR;
import static com.riopapa.jigsawpuzzle.ActivityMain.ANI_TO_PAINT;
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
                if (jigOLine[cc][rr] == null)
                    pieceImage.buildPiece(c+vars.offsetC, r+vars.offsetR);
                if (vars.jigTables[cc][rr].locked) {
                    if (vars.jigTables[cc][rr].count == 0)
                        canvas.drawBitmap(jigPic[cc][rr],
                                vars.baseX + c * vars.picISize, vars.baseY + r * vars.picISize, null);
                    else {
                        vars.jigTables[cc][rr].count--;
                        canvas.drawBitmap((vars.jigTables[cc][rr].count % 2 == 0) ?
                                        jigBright[cc][rr] : jigPic[cc][rr],
                                vars.baseX + c * vars.picISize, vars.baseY + r * vars.picISize, null);
                        if (vars.jigTables[cc][rr].count == 0) {
                            jigBright[cc][rr] = null;
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
                    canvas.drawBitmap(jigOLine[cc][rr],
                            vars.baseX + c * vars.picISize, vars.baseY + r * vars.picISize,
                            pGrayed);
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
                canvas.drawBitmap(jigOLine[c][r], jt.posX, jt.posY, null);
                continue;
            }
            // animate just anchored
            if (fp.count > 0 && fp.mode == ANI_ANCHOR) {
                fp.count--;
                if (jigBright[c][r] == null)
                    jigBright[c][r] = pieceImage.makeBright(jigOLine[c][r]);
                canvas.drawBitmap((fp.count % 2 == 0) ?
                        jigBright[c][r] : jigPic[c][r],
                        vars.jigTables[c][r].posX + rnd.nextInt(10) - 5,
                        vars.jigTables[c][r].posY + rnd.nextInt(10) - 5, null);
                if (fp.count == 0) {
                    fp.mode = 0;
                }
                vars.fps.set(cnt, fp);
                continue;
            }
            // animate recycler to paint
            if (fp.count > 0 && fp.mode == ANI_TO_PAINT) {  // animate from recycle to paintView
                fp.count--;
                if (jigBright[c][r] == null)
                    jigBright[c][r] = pieceImage.makeBright(jigOLine[c][r]);
                canvas.drawBitmap((fp.count % 2 == 0) ?
                        jigBright[c][r] : jigPic[c][r],
                        vars.jigTables[c][r].posX + rnd.nextInt(10) - 5,
                        vars.jigTables[c][r].posY + rnd.nextInt(10) - 5, null);
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
