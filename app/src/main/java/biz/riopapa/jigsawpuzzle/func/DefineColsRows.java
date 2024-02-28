package biz.riopapa.jigsawpuzzle.func;

public class DefineColsRows {

    /**
     * calculate jigCOLUMNs, jigROWs initially by level info
     * inp : chosenImageHeight, chosenImageWidth : image Height
     * out : colNbr, rowNbr
     **/


    public int[] calc(int level, int imageWidth, int imageHeight) {
        int col, row;    // return values
        int [] sizes = {2, 6, 10, 14};
        double sz = Math.sqrt(Math.sqrt(imageWidth * imageHeight));
        if (imageWidth > imageHeight) {
            row = sizes[level];
            row = (int) Math.sqrt(row * row + sz*(level+2)) / 2;
            int inner = imageHeight /(row+1);
            int gap = inner * 5 / 14;
            col = imageWidth / inner;
            while (col * inner + gap + gap + 4 > imageWidth)
                col--;

        } else {
            col = sizes[level];
            col = (int) Math.sqrt(col * col + sz*(level+2)) / 2;
            int inner = imageWidth /(col+1);
            int gap = inner * 5 / 14;
            row = imageHeight / inner;
            while (row * inner + gap + gap + 4 > imageHeight)
                row--;
        }
        int[] colRow = {col, row};;
        return colRow;
    }
}