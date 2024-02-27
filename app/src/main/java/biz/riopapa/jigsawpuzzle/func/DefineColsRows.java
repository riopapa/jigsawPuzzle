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
            col = row * imageWidth / imageHeight + 1;  // one more possible?
            float szH = (float) imageHeight / (float) (row);
            int col2 = (int) ((float) imageWidth / szH);
            if (col2 > col + 2)
                col++;
        } else {
            col = sizes[level];
            col = (int) Math.sqrt(col * col + sz*(level+2)) / 2;
            row = col * imageHeight / imageWidth + 1;  // to avoid over y size
            float szW = (float) imageWidth / (float) (col);
            int row2 = (int) ((float) imageHeight / szW);
            if (row2 > row + 2)
                row++;
        }
        int[] colRow = {col, row};;
        return colRow;
    }
}