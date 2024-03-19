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

        masks[0][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_0l), part_le);
        masks[0][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_1l), part_le);
        masks[0][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_2l), part_le);
        masks[0][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_3l), part_le);
        masks[0][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_4l), part_le);
        masks[0][5] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_5l), part_le);
        masks[0][6] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_6l), part_le);

        masks[1][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_0r), part_ri);
        masks[1][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_1r), part_ri);
        masks[1][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_2r), part_ri);
        masks[1][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_3r), part_ri);
        masks[1][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_4r), part_ri);
        masks[1][5] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_5r), part_ri);
        masks[1][6] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_6r), part_ri);

        masks[2][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_00u), part_up);
        masks[2][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_10u), part_up);
        masks[2][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_20u), part_up);
        masks[2][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_30u), part_up);
        masks[2][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_40u), part_up);
        masks[2][5] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_50u), part_up);
        masks[2][6] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_60u), part_up);

        masks[3][0] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_00d), part_dn);
        masks[3][1] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_10d), part_dn);
        masks[3][2] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_20d), part_dn);
        masks[3][3] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_30d), part_dn);
        masks[3][4] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_40d), part_dn);
        masks[3][5] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_50d), part_dn);
        masks[3][6] = pieceImage.maskSrcMap(dMap.make(R.drawable.case_60d), part_dn);

        return masks;
    }

}
