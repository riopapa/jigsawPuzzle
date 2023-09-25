package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.MainActivity.piece;

import android.content.Context;
import android.graphics.Bitmap;

public class Masks {
    public Bitmap[][] make(Context cxt, int outerSize) {

        // remember to set com.riopapa.jigsawpuzzle.SetBoundaryVal() nextInt with this value
        // outerSize means puzzle outer size
        Bitmap [][] masks = new Bitmap[4][5];
        Drawable2bitmap dMap = new Drawable2bitmap(cxt, outerSize);
        
        Bitmap part_up = dMap.make(R.drawable.part_up);
        Bitmap part_le = dMap.make(R.drawable.part_le);
        Bitmap part_ri = dMap.make(R.drawable.part_ri);
        Bitmap part_dn = dMap.make(R.drawable.part_dn);

        masks[0][0] = dMap.make(R.drawable.part0_le);
        masks[0][1] = piece.maskOut(dMap.make(R.drawable.case_l1), part_le);
        masks[0][2] = piece.maskOut(dMap.make(R.drawable.case_l2), part_le);
        masks[0][3] = piece.maskOut(dMap.make(R.drawable.case_l3), part_le);
        masks[0][4] = piece.maskOut(dMap.make(R.drawable.case_l4), part_le);

        masks[1][0] = dMap.make(R.drawable.part0_ri);
        masks[1][1] = piece.maskOut(dMap.make(R.drawable.case_r1), part_ri);
        masks[1][2] = piece.maskOut(dMap.make(R.drawable.case_r2), part_ri);
        masks[1][3] = piece.maskOut(dMap.make(R.drawable.case_r3), part_ri);
        masks[1][4] = piece.maskOut(dMap.make(R.drawable.case_r4), part_ri);

        masks[2][0] = dMap.make(R.drawable.part0_up);
        masks[2][1] = piece.maskOut(dMap.make(R.drawable.case_u1), part_up);
        masks[2][2] = piece.maskOut(dMap.make(R.drawable.case_u2), part_up);
        masks[2][3] = piece.maskOut(dMap.make(R.drawable.case_u3), part_up);
        masks[2][4] = piece.maskOut(dMap.make(R.drawable.case_u4), part_up);

        masks[3][0] = dMap.make(R.drawable.part0_dn);
        masks[3][1] = piece.maskOut(dMap.make(R.drawable.case_d1), part_dn);
        masks[3][2] = piece.maskOut(dMap.make(R.drawable.case_d2), part_dn);
        masks[3][3] = piece.maskOut(dMap.make(R.drawable.case_d3), part_dn);
        masks[3][4] = piece.maskOut(dMap.make(R.drawable.case_d4), part_dn);
        return masks;
    }
    public Bitmap[][] makeOut(Context cxt, int outerSize) {

        // remember to set com.riopapa.jigsawpuzzle.SetBoundaryVal() nextInt with this value
        Bitmap [][] masks = new Bitmap[4][5];
        Drawable2bitmap dMap = new Drawable2bitmap(cxt, outerSize);

        Bitmap part_up = dMap.make(R.drawable.part_up);
        Bitmap part_le = dMap.make(R.drawable.part_le);
        Bitmap part_ri = dMap.make(R.drawable.part_ri);
        Bitmap part_dn = dMap.make(R.drawable.part_dn);

        masks[0][0] = dMap.make(R.drawable.part0_le);
        masks[0][1] = piece.maskOut(dMap.make(R.drawable.case_l1o), part_le);
        masks[0][2] = piece.maskOut(dMap.make(R.drawable.case_l2o), part_le);
        masks[0][3] = piece.maskOut(dMap.make(R.drawable.case_l3o), part_le);
        masks[0][4] = piece.maskOut(dMap.make(R.drawable.case_l4o), part_le);

        masks[1][0] = dMap.make(R.drawable.part0_ri);
        masks[1][1] = piece.maskOut(dMap.make(R.drawable.case_r1o), part_ri);
        masks[1][2] = piece.maskOut(dMap.make(R.drawable.case_r2o), part_ri);
        masks[1][3] = piece.maskOut(dMap.make(R.drawable.case_r3o), part_ri);
        masks[1][4] = piece.maskOut(dMap.make(R.drawable.case_r4o), part_ri);

        masks[2][0] = dMap.make(R.drawable.part0_up);
        masks[2][1] = piece.maskOut(dMap.make(R.drawable.case_u1o), part_up);
        masks[2][2] = piece.maskOut(dMap.make(R.drawable.case_u2o), part_up);
        masks[2][3] = piece.maskOut(dMap.make(R.drawable.case_u3o), part_up);
        masks[2][4] = piece.maskOut(dMap.make(R.drawable.case_u4o), part_up);

        masks[3][0] = dMap.make(R.drawable.part0_dn);
        masks[3][1] = piece.maskOut(dMap.make(R.drawable.case_d1o), part_dn);
        masks[3][2] = piece.maskOut(dMap.make(R.drawable.case_d2o), part_dn);
        masks[3][3] = piece.maskOut(dMap.make(R.drawable.case_d3o), part_dn);
        masks[3][4] = piece.maskOut(dMap.make(R.drawable.case_d4o), part_dn);
        return masks;
    }
}