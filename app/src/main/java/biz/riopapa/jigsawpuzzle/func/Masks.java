package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.pieceImage;

import android.content.Context;
import android.graphics.Bitmap;

import biz.riopapa.jigsawpuzzle.R;
import biz.riopapa.jigsawpuzzle.func.Drawable2bitmap;

public class Masks {

    /*
     * predefine masks with imgOutSize for maskup to crop image
     * output : masks[][]
     */
    public Bitmap[][] make(Context cxt, int outerSize) {

        // remember to set biz.riopapa.jigsawpuzzle.DefineTableWalls() nextInt with this value
        // outerSize means puzzle outer size
        Bitmap [][] masks = new Bitmap[4][5];
        Drawable2bitmap dMap = new Drawable2bitmap(cxt, outerSize);
        
        Bitmap part_up = dMap.make(R.drawable.part_up);
        Bitmap part_le = dMap.make(R.drawable.part_le);
        Bitmap part_ri = dMap.make(R.drawable.part_ri);
        Bitmap part_dn = dMap.make(R.drawable.part_dn);

        masks[0][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.part0_le), part_le);
        masks[0][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l1), part_le);
        masks[0][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l2), part_le);
        masks[0][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l3), part_le);
        masks[0][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l4), part_le);

        masks[1][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.part0_ri), part_ri);
        masks[1][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r1), part_ri);
        masks[1][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r2), part_ri);
        masks[1][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r3), part_ri);
        masks[1][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r4), part_ri);

        masks[2][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.part0_up), part_up);
        masks[2][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u1), part_up);
        masks[2][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u2), part_up);
        masks[2][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u3), part_up);
        masks[2][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u4), part_up);

        masks[3][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.part0_dn), part_dn);
        masks[3][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d1), part_dn);
        masks[3][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d2), part_dn);
        masks[3][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d3), part_dn);
        masks[3][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d4), part_dn);
        return masks;
    }
    /*
     * predefine masks with imgOutSize for maskup to crop image with outline
     * output : masks[][]
     */

    public Bitmap[][] makeOut(Context cxt, int outerSize) {

        // remember to set biz.riopapa.jigsawpuzzle.DefineTableWalls() nextInt with this value
        Bitmap [][] masks = new Bitmap[4][5];
        Drawable2bitmap dMap = new Drawable2bitmap(cxt, outerSize);

        Bitmap part_up = dMap.make(R.drawable.part_up);
        Bitmap part_le = dMap.make(R.drawable.part_le);
        Bitmap part_ri = dMap.make(R.drawable.part_ri);
        Bitmap part_dn = dMap.make(R.drawable.part_dn);

        masks[0][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.part0_leo), part_le);
        masks[0][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l1o), part_le);
        masks[0][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l2o), part_le);
        masks[0][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l3o), part_le);
        masks[0][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_l4o), part_le);

        masks[1][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.part0_rio), part_ri);
        masks[1][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r1o), part_ri);
        masks[1][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r2o), part_ri);
        masks[1][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r3o), part_ri);
        masks[1][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_r4o), part_ri);

        masks[2][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.part0_upo), part_up);
        masks[2][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u1o), part_up);
        masks[2][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u2o), part_up);
        masks[2][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u3o), part_up);
        masks[2][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_u4o), part_up);

        masks[3][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.part0_dno), part_dn);
        masks[3][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d1o), part_dn);
        masks[3][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d2o), part_dn);
        masks[3][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d3o), part_dn);
        masks[3][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_d4o), part_dn);
        return masks;
    }
}
