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
                final int ac = c + gVal.offsetC;
                final int ar = r + gVal.offsetR;
                if (gVal.jigTables[ac][ar].locked) {
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
            final int ac = fp.C;
            final int ar = fp.R;
            if (gVal.jigTables[ac][ar].locked)
                sb.append("\nDump fps "+i+" "+ ac+"x"+ar+" is locked");
            else
                sb.append("\nDump fps "+i+" "+ac+"x"+ar+" OK");
        }
        for (int i = 0; i < activeJigs.size(); i++) {
            final int tmp = activeJigs.get(i) - 10000;
            final int ac = tmp / 100;
            final int ar = tmp - ac * 100;
            if (gVal.jigTables[ac][ar].locked)
                sb.append("\n **** "+ac + "x" + ar + " is locked");
        }

        if (locked + activeJigs.size() + gVal.fps.size() != (gVal.showMaxX*gVal.showMaxY)) {
            sb.append("\nDiff Size Locked=" + locked+ " activeSZ="+activeJigs.size()+" fpSZ="+ gVal.fps.size()+" show="+(gVal.showMaxX*gVal.showMaxY));
            Log.e("Dump", sb.toString());
        }
    }
}
