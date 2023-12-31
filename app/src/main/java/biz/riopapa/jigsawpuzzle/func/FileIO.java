package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.jpgFolder;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileIO {

    public static String readTextFile(String dir, String fileName) {
        File myDir = mContext.getDir(dir, Context.MODE_PRIVATE); //Creating an internal dir;
        File jFile = new File(myDir, fileName); //Getting a file within the dir.

        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(jFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                sb.append(strLine).append("\n");
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Bitmap getJPGFile(String dir, String fileName) {
        try {
            File file = existJPGFile(dir, fileName);
            if (file == null)
                return null;

            // Read the file into a byte array
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(bytes);
            inputStream.close();

            // Decode the byte array into a bitmap
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File existJPGFile(String dir, String fileName) {
        File myDir = mContext.getDir(dir, Context.MODE_PRIVATE); //Creating an internal dir;
        File file = new File(myDir, fileName); //Getting a file within the dir.
        // Check if the file exists
        if (!file.exists())
            return null;
        return file;
    }

    // image name : a00, thumbnail name : a00T.jpg
    public static void thumbnail2File(Bitmap bitmap, String fileName) {
        // Creating an internal dir;
        File myDir = mContext.getDir(jpgFolder, Context.MODE_PRIVATE);
        // Setting a file within the dir
        File file = new File(myDir, fileName.substring(0,3) + "T.jpg");
        FileOutputStream os;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
            os.close();
        } catch (IOException e) {
            Log.e("ioException", e.toString());
        }
    }
}
