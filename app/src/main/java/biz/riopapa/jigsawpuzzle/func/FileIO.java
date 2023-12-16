package biz.riopapa.jigsawpuzzle.func;

import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
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

    public static Bitmap getJPGFile(Context context, String dir, String fileName) {
        try {
            File file = existJPGFile(context, dir, fileName);
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

    public static File existJPGFile(Context context, String dir, String fileName) {
        File myDir = mContext.getDir(dir, Context.MODE_PRIVATE); //Creating an internal dir;
        File file = new File(myDir, fileName); //Getting a file within the dir.
        // Check if the file exists
        if (!file.exists())
            return null;
        return file;
    }

    public static String bitmap2string(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Adjust quality as needed
        byte[] bitmapBytes = outputStream.toByteArray();
        return Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
    }

    public static Bitmap string2bitmap (String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    void writeFile(File targetFolder, String fileName, String outText) {
        try {
            File targetFile = new File(targetFolder, fileName);
            FileWriter fileWriter = new FileWriter(targetFile, false);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(outText);
            bufferedWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
