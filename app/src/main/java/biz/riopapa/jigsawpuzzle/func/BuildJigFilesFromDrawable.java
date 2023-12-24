package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.histories;
import static biz.riopapa.jigsawpuzzle.ActivityMain.jigFiles;

import java.util.ArrayList;

import biz.riopapa.jigsawpuzzle.images.ImageStorage;
import biz.riopapa.jigsawpuzzle.model.JigFile;

public class BuildJigFilesFromDrawable {
    public BuildJigFilesFromDrawable() {
        jigFiles = new ArrayList<>();

        // add mipmap images into jigFiles;

        ImageStorage iStorage = new ImageStorage();
        int imgCnt = iStorage.count();
        for (int i = 0; i < imgCnt ; i++) {
            JigFile jf = new JigFile();
            jf.game = iStorage.getGame(i);
            FileIO.thumbnail2File(iStorage.getThumbnail(i), jf.game);
            for (int h = 0; h < histories.size(); h++)
                if (histories.get(h).game.equals(jf.game))
                    jf.latestLvl = histories.get(h).latestLvl;
            jigFiles.add(jf);
        }
    }
}
