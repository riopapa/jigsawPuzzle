package com.riopapa.zigsawpuzzle;

import static com.riopapa.zigsawpuzzle.MainActivity.piece;

import android.content.Context;
import android.graphics.Bitmap;

public class Masks {
    public Bitmap[][] make(Context cxt, int outerSize) {

        // remember to set SetBoundaryVals() nextInt with this value
        // outerSize means puzzle outer size
        Bitmap [][] masks = new Bitmap[4][5];
        Drawable2bitmap dMap = new Drawable2bitmap(cxt, outerSize);
        
        Bitmap part_up = dMap.make(R.drawable.part_up);
        Bitmap part_le = dMap.make(R.drawable.part_le);
        Bitmap part_ri = dMap.make(R.drawable.part_ri_mask);
        Bitmap part_dn = dMap.make(R.drawable.part_dn_mask);

        Bitmap case_ud1 = dMap.make(R.drawable.case_u1);
        Bitmap case_ud2 = dMap.make(R.drawable.case_u2);
        Bitmap case_ud3 = dMap.make(R.drawable.case_u3);
        Bitmap case_ud4 = dMap.make(R.drawable.case_u4);
        Bitmap case_lr1 = dMap.make(R.drawable.case_l1);
        Bitmap case_lr2 = dMap.make(R.drawable.case_l2);
        Bitmap case_lr3 = dMap.make(R.drawable.case_l3);
        Bitmap case_lr4 = dMap.make(R.drawable.case_l4);

        masks[0][0] = dMap.make(R.drawable.part0_le);
        masks[0][1] = piece.makeLeft(case_lr1, part_le);
        masks[0][2] = piece.makeLeft(case_lr2, part_le);
        masks[0][3] = piece.makeLeft(case_lr3, part_le);
        masks[0][4] = piece.makeLeft(case_lr4, part_le);

        masks[1][0] = dMap.make(R.drawable.part0_ri);
        masks[1][1] = piece.makeRight(case_lr1, part_ri);
        masks[1][2] = piece.makeRight(case_lr2, part_ri);
        masks[1][3] = piece.makeRight(case_lr3, part_ri);
        masks[1][4] = piece.makeRight(case_lr4, part_ri);

        masks[2][0] = dMap.make(R.drawable.part0_up);
        masks[2][1] = piece.makeUpper(case_ud1, part_up);
        masks[2][2] = piece.makeUpper(case_ud2, part_up);
        masks[2][3] = piece.makeUpper(case_ud3, part_up);
        masks[2][4] = piece.makeUpper(case_ud4, part_up);

        masks[3][0] = dMap.make(R.drawable.part0_dn);
        masks[3][1] = piece.makeDown(case_ud1, part_dn);
        masks[3][2] = piece.makeDown(case_ud2, part_dn);
        masks[3][3] = piece.makeDown(case_ud3, part_dn);
        masks[3][4] = piece.makeDown(case_ud4, part_dn);
        return masks;
    }
    public Bitmap[][] makeOut(Context cxt, int outerSize) {

        // remember to set SetBoundaryVals() nextInt with this value
        Bitmap [][] masks = new Bitmap[4][5];
        Drawable2bitmap dMap = new Drawable2bitmap(cxt, outerSize);

        Bitmap part_up = dMap.make(R.drawable.part_up);
        Bitmap part_le = dMap.make(R.drawable.part_le);
        Bitmap part_ri = dMap.make(R.drawable.part_ri_mask);
        Bitmap part_dn = dMap.make(R.drawable.part_dn_mask);

        Bitmap case_ud1 = dMap.make(R.drawable.case_u1o);
        Bitmap case_ud2 = dMap.make(R.drawable.case_u2o);
        Bitmap case_ud3 = dMap.make(R.drawable.case_u3o);
        Bitmap case_ud4 = dMap.make(R.drawable.case_u4o);
        Bitmap case_lr1 = dMap.make(R.drawable.case_l1o);
        Bitmap case_lr2 = dMap.make(R.drawable.case_l2o);
        Bitmap case_lr3 = dMap.make(R.drawable.case_l3o);
        Bitmap case_lr4 = dMap.make(R.drawable.case_l4o);

        masks[0][0] = dMap.make(R.drawable.part0_le);
        masks[0][1] = piece.makeLeft(case_lr1, part_le);
        masks[0][2] = piece.makeLeft(case_lr2, part_le);
        masks[0][3] = piece.makeLeft(case_lr3, part_le);
        masks[0][4] = piece.makeLeft(case_lr4, part_le);

        masks[1][0] = dMap.make(R.drawable.part0_ri);
        masks[1][1] = piece.makeRight(case_lr1, part_ri);
        masks[1][2] = piece.makeRight(case_lr2, part_ri);
        masks[1][3] = piece.makeRight(case_lr3, part_ri);
        masks[1][4] = piece.makeRight(case_lr4, part_ri);

        masks[2][0] = dMap.make(R.drawable.part0_up);
        masks[2][1] = piece.makeUpper(case_ud1, part_up);
        masks[2][2] = piece.makeUpper(case_ud2, part_up);
        masks[2][3] = piece.makeUpper(case_ud3, part_up);
        masks[2][4] = piece.makeUpper(case_ud4, part_up);

        masks[3][0] = dMap.make(R.drawable.part0_dn);
        masks[3][1] = piece.makeDown(case_ud1, part_dn);
        masks[3][2] = piece.makeDown(case_ud2, part_dn);
        masks[3][3] = piece.makeDown(case_ud3, part_dn);
        masks[3][4] = piece.makeDown(case_ud4, part_dn);
        return masks;
    }
}
