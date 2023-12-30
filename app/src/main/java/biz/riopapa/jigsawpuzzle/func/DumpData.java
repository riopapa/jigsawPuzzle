package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.activeJigs;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gVal;

import android.util.Log;

import biz.riopapa.jigsawpuzzle.model.FloatPiece;

public class DumpData {
    public DumpData() {
        StringBuilder sb = new StringBuilder();

        sb.append("Dump Lock Status sz="+ (gVal.showMaxX*gVal.showMaxY)+"\n");
        int locked = 0;
        for (int r = 0; r < gVal.showMaxY; r++) {
            sb.append("\n(").append(r);
            for (int c = 0; c < gVal.showMaxX; c++) {
                final int cc = c + gVal.offsetC;
                final int rr = r + gVal.offsetR;
                if (gVal.jigTables[cc][rr].locked) {
                    sb.append(" L");
                    locked++;
                }
                else
                    sb.append(" -");
            }
        }

        sb.append("\nDump FP Status");
        for (int i = 0; i < gVal.fps.size(); i++) {
            FloatPiece fp = gVal.fps.get(i);
            final int cc = fp.C;
            final int rr = fp.R;
            if (gVal.jigTables[cc][rr].locked)
                sb.append("\nDump fps "+i+" "+ cc+"x"+rr+" is locked");
            else
                sb.append("\nDump fps "+i+" "+cc+"x"+rr+" OK");
        }
        for (int i = 0; i < activeJigs.size(); i++) {
            final int cr = activeJigs.get(i) - 10000;
            final int cc = cr / 100;
            final int rr = cr - cc * 100;
            if (gVal.jigTables[cc][rr].locked)
                sb.append("\n **** "+cc + "x" + rr + " is locked");
        }

        if (locked + activeJigs.size() + gVal.fps.size() != (gVal.showMaxX*gVal.showMaxY)) {
            sb.append("\nDiff Size Locked=" + locked+ " activeSZ="+activeJigs.size()+" fpSZ="+ gVal.fps.size()+" show="+(gVal.showMaxX*gVal.showMaxY));
            Log.e("Dump", sb.toString());
        }
    }
}
