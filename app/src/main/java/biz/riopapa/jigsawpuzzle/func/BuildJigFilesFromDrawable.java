package biz.riopapa.jigsawpuzzle.func;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import biz.riopapa.jigsawpuzzle.images.ImageStorage;
import biz.riopapa.jigsawpuzzle.model.JigFile;

import static biz.riopapa.jigsawpuzzle.ActivityMain.jigFiles;
import static biz.riopapa.jigsawpuzzle.ActivityMain.jpgFolder;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

public class BuildJigFilesFromDrawable {
    public BuildJigFilesFromDrawable() {
        jigFiles = new ArrayList<>();

        // add mipmap images into jigFiles;

        ImageStorage iStorage = new ImageStorage();
        int imgCnt = iStorage.count();
        for (int i = 0; i < imgCnt ; i++) {
            JigFile jF = new JigFile();
            jF.game = iStorage.getGame(i);
            jF.thumbnailMap = iStorage.getThumbnail(i);
//            jF.thumbnailMap = FileIO.bitmap2string(iStorage.getThumbnail(i));
            jigFiles.add(jF);
        }
//        Log.w("jigFiles","jigFiles after iStorage sz= "+jigFiles.size());

        // List all files in the directory, then add to jigFiles
        File myDir = mContext.getDir(jpgFolder, Context.MODE_PRIVATE); //Creating an internal dir;
        File[] files = myDir.listFiles();

        if (files != null) {
            for (File file : files) {
                String fName = file.getName();
                JigFile jf = new JigFile();
                if (fName.endsWith("T.jpg")) {  // thumbnail a00T.jpg
                    jf.thumbnailMap = FileIO.getJPGFile(jpgFolder, fName);
                } else {
                    Bitmap bMap = FileIO.getJPGFile(jpgFolder, fName);
                    assert bMap != null;
                    jf.thumbnailMap = Bitmap.createScaledBitmap(bMap,
                            (int) (bMap.getWidth() / 4f), (int) (bMap.getHeight() / 4f), true);
                }
                jf.game = fName.substring(0, 3);
                jf.downloaded = true;
                jigFiles.add(jf);
            }
        }
        Log.w("jigFiles","jigFiles after local files sz= "+jigFiles.size());
    }
}
//                    jf.thumbnailMap = FileIO.bitmap2string(Bitmap.createScaledBitmap(bMap,
//                            (int) (bMap.getWidth() / 4f), (int) (bMap.getHeight() / 4f), true));
