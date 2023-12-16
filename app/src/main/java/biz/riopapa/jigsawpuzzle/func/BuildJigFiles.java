package biz.riopapa.jigsawpuzzle.func;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import biz.riopapa.jigsawpuzzle.images.ImageStorage;
import biz.riopapa.jigsawpuzzle.model.JigFile;

import static biz.riopapa.jigsawpuzzle.ActivityMain.jigFiles;

public class BuildJigFiles {
    public BuildJigFiles(Context context) {
        jigFiles = new ArrayList<>();

        // add mipmap images into jigFiles;

        ImageStorage iStorage = new ImageStorage();
        int imgCnt = iStorage.count();
        for (int i = 0; i < imgCnt ; i++) {
            JigFile jF = new JigFile();
            jF.game = iStorage.getGame(i);
            jF.thumbnailMap = iStorage.getThumbnail(i);
            jigFiles.add(jF);
        }
        Log.w("jigFiles","jigFiles sz= "+jigFiles.size());

        // List all files in the directory, then add to jigFiles
        File directory = new File(".");
        File[] files = directory.listFiles();
        if (files != null) {
            Log.w("local File", "sz=" + files.length);
            for (File file : files) {
                String fName = file.getName();
                if (fName.endsWith(".jpg")) {
                    JigFile jf = new JigFile();
                    Bitmap bMap = FileIO.getJPGFile(context, "", fName);
                    assert bMap != null;
                    jf.thumbnailMap = Bitmap.createScaledBitmap(bMap,
                            (int) (bMap.getWidth() / 4f), (int) (bMap.getHeight() / 4f), true);
                    jf.game = fName.substring(0, 3);
                    jf.downloaded = true;
                    Log.w("exist","file ="+jf.game);
                    jigFiles.add(jf);
                }
            }
        }
        Log.w("jigFiles","jigFiles sz= "+jigFiles.size());
    }
}
