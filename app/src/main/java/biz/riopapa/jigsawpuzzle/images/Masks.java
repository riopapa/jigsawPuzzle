package biz.riopapa.jigsawpuzzle.images;

import android.content.Context;
import android.graphics.Bitmap;

import biz.riopapa.jigsawpuzzle.R;

public class Masks {

    PieceImage pieceImage;
    Context context;
    public Masks(Context context, PieceImage pieceImage) {
        this.context = context;
        this.pieceImage = pieceImage;
    }
    /*
     * predefine masks with imgOutSize for maskup to crop image
     * output : masks[][]
     */
    public Bitmap[][] make(Context cxt, int outerSize) {

        // remember to set DefineTableWalls() nextInt with this value
        // outerSize means puzzle outer size
        Bitmap [][] masks = new Bitmap[4][7];
        Drawable2bitmap dMap = new Drawable2bitmap(cxt, outerSize);
        
        Bitmap part_up = dMap.make(R.drawable.part_up);
        Bitmap part_le = dMap.make(R.drawable.part_le);
        Bitmap part_ri = dMap.make(R.drawable.part_ri);
        Bitmap part_dn = dMap.make(R.drawable.part_dn);

        masks[0][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l0), part_le);
        masks[0][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l1), part_le);
        masks[0][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l2), part_le);
        masks[0][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l3), part_le);
        masks[0][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l4), part_le);
        masks[0][5] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l5), part_le);
        masks[0][6] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l6), part_le);

        masks[1][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r0), part_ri);
        masks[1][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r1), part_ri);
        masks[1][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r2), part_ri);
        masks[1][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r3), part_ri);
        masks[1][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r4), part_ri);
        masks[1][5] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r5), part_ri);
        masks[1][6] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r6), part_ri);

        masks[2][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u0), part_up);
        masks[2][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u1), part_up);
        masks[2][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u2), part_up);
        masks[2][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u3), part_up);
        masks[2][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u4), part_up);
        masks[2][5] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u5), part_up);
        masks[2][6] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u6), part_up);

        masks[3][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d0), part_dn);
        masks[3][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d1), part_dn);
        masks[3][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d2), part_dn);
        masks[3][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d3), part_dn);
        masks[3][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d4), part_dn);
        masks[3][5] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d5), part_dn);
        masks[3][6] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d6), part_dn);

        return masks;
    }

}
