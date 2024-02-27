package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.downloadPosition;
import static biz.riopapa.jigsawpuzzle.ActivityMain.downloadSize;
import static biz.riopapa.jigsawpuzzle.ActivityMain.imageSelAdapter;
import static biz.riopapa.jigsawpuzzle.ActivityMain.jigFiles;
import static biz.riopapa.jigsawpuzzle.ActivityMain.jpgFolder;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import biz.riopapa.jigsawpuzzle.DownloadCompleteListener;
import biz.riopapa.jigsawpuzzle.images.ImageStorage;
import biz.riopapa.jigsawpuzzle.model.JigFile;

public class DownloadTask extends AsyncTask<String, Integer, Long> {

    private static final String TAG = "DownloadTask";
    private final DownloadCompleteListener listener;
    private final String url;
    private final String fileName, fileType;
    private final String jpgDir;

    public DownloadTask(DownloadCompleteListener listener, String fileId, String jpgDir,
                        String fileName, String fileType) {
        this.listener = listener;
        String imageHead = "https://drive.google.com/uc?export=download&id=";
        this.url = imageHead + fileId;
        this.jpgDir = jpgDir;
        this.fileName = fileName;
        this.fileType = fileType;   // ".jpg", ".txt"
    }

    @Override
    protected Long doInBackground(String... urls) {
        long downSize = 0;

        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());

            File myDir = mContext.getDir(jpgDir, Context.MODE_PRIVATE); //Creating an internal dir;
            File fileWithinMyDir = new File(myDir, fileName+fileType); //Getting a file within the dir.
            FileOutputStream out = new FileOutputStream(fileWithinMyDir);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                downloadSize += bytesRead;
            }
            out.flush();
            out.close();
            inputStream.close();
            connection.disconnect();
            Log.w("connection","disconnected "+fileName);
        } catch (Exception e) {
            Log.e(TAG, "Error downloading file: " + e.getMessage());
        }

        downloadSize = downSize;
        return downSize;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);

//        progressBar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        Log.w("download","Post exe for "+fileName);
        if (fileType.equals(".jpg")) {
            // if jpg file downloaded, update to jigFiles.thumbnailMap;
            Log.w("postDownload", fileName + ", jpgFiles="+jigFiles.size());
            Bitmap jigImage = FileIO.getJPGFile(jpgFolder, fileName+fileType);
            if (jigImage != null) {
                Bitmap thumb = Bitmap.createScaledBitmap(jigImage,
                        (int) (jigImage.getWidth() / 5f), (int) (jigImage.getHeight() / 5f), true);
                FileIO.saveThumbnail(thumb, fileName);

                for (int i = new ImageStorage().count(); i < jigFiles.size(); i++) {
                    JigFile jf = jigFiles.get(i);
                    if (jf.game.equals(fileName)) {
                        jf.newFlag = true;
                        jigFiles.set(i, jf);
                        imageSelAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                downloadPosition = -1;
            }
        }

        if (listener != null) {
            listener.onDownloadComplete(); // Notify MainActivity about download completion
        }
    }
}

