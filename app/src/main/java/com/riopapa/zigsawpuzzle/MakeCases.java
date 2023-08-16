package com.riopapa.zigsawpuzzle;

import android.content.Context;
import android.graphics.Bitmap;

public class MakeCases {
    public Bitmap[][] generate(Context mContext, int zw, int x5, int pw) {

        Bitmap [][] cases = new Bitmap[4][4];

        Piece piece = new Piece(zw, x5, pw);

        Bitmap part_up = new Drawable2bitmap().make(mContext, zw, R.drawable.part_up);
        Bitmap part_le = new Drawable2bitmap().make(mContext, zw, R.drawable.part_le);
        Bitmap part_riM = new Drawable2bitmap().make(mContext, zw, R.drawable.part_ri_mask);
        Bitmap part_dnM = new Drawable2bitmap().make(mContext, zw, R.drawable.part_dn_mask);

        Bitmap case_ud1 = new Drawable2bitmap().make(mContext, zw, R.drawable.case_ud1);
        Bitmap case_ud2 = new Drawable2bitmap().make(mContext, zw, R.drawable.case_ud2);
        Bitmap case_ud3 = new Drawable2bitmap().make(mContext, zw, R.drawable.case_ud3);
        Bitmap case_lr1 = new Drawable2bitmap().make(mContext, zw, R.drawable.case_lr1);
        Bitmap case_lr2 = new Drawable2bitmap().make(mContext, zw, R.drawable.case_lr2);
        Bitmap case_lr3 = new Drawable2bitmap().make(mContext, zw, R.drawable.case_lr3);

        cases[0][0] = new Drawable2bitmap().make(mContext, zw, R.drawable.part0_le);
        cases[0][1] = piece.makeLeft(case_lr1, part_le);
        cases[0][2] = piece.makeLeft(case_lr2, part_le);
        cases[0][3] = piece.makeLeft(case_lr3, part_le);

        cases[1][0] = new Drawable2bitmap().make(mContext, zw, R.drawable.part0_ri);
        cases[1][1] = piece.makeRight(case_lr1, part_riM);
        cases[1][2] = piece.makeRight(case_lr2, part_riM);
        cases[1][3] = piece.makeRight(case_lr3, part_riM);

        cases[2][0] = new Drawable2bitmap().make(mContext, zw, R.drawable.part0_up);
        cases[2][1] = piece.makeUpper(case_ud1, part_up);
        cases[2][2] = piece.makeUpper(case_ud2, part_up);
        cases[2][3] = piece.makeUpper(case_ud3, part_up);

        cases[3][0] = new Drawable2bitmap().make(mContext, zw, R.drawable.part0_dn);
        cases[3][1] = piece.makeDown(case_ud1, part_dnM);
        cases[3][2] = piece.makeDown(case_ud2, part_dnM);
        cases[3][3] = piece.makeDown(case_ud3, part_dnM);
        return cases;
    }
}
