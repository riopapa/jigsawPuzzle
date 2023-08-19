package com.riopapa.zigsawpuzzle;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    TextView tvGo;
    Context mContext;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv99;
    public int zw,x5, pw, nw;  // real piece size
    int zigX, zigY;
    Piece piece;

    int screenX, screenY, puzzleWidth, puzzleHeight;
    public static ZigInfo [][] zigInfo;

    Bitmap [][] zigZag;

    public PaintView paintView;
    public static ArrayList<Integer> reZigs;
    public static Activity mActivity;
    public static ReZigAdapter zigAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        }

        mContext = getApplicationContext();
        mActivity = this;

        tvGo = findViewById(R.id.go);

        iv1 = findViewById(R.id.image1);
        iv2 = findViewById(R.id.image2);
        iv3 = findViewById(R.id.image3);
        iv4 = findViewById(R.id.image4);
        iv5 = findViewById(R.id.image5);
        iv6 = findViewById(R.id.image6);
        iv7 = findViewById(R.id.image7);
        iv8 = findViewById(R.id.image8);
        iv99 = findViewById(R.id.image99);

        Bitmap fullImage =
                BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.scenary, null);

        generatePixels(fullImage);

        zigX = 12;
        zigY = 12;
        zigInfo = new ZigInfo[zigX][zigY];
        new ZigSawLRUD(zigInfo, zigX,zigY);

        zigZag = new MakeCases().generate(mContext, zw, x5, pw);

        Log.w("size","gWidth="+puzzleWidth+", gHeight="+puzzleHeight+", zw="+zw+", x5="+x5+", pw="+pw+" zig "+zigInfo.length);

        for (int x = 0; x < zigX; x++) {
            for (int y = 0; y < zigY; y++) {
                ZigInfo z = zigInfo[x][y];
                Bitmap srcMap = Bitmap.createBitmap(fullImage, 480 + x*pw, 330 + y*pw, zw, zw);
                Bitmap mask = maskMerge(zigZag[0][z.lType], zigZag[1][z.rType],
                        zigZag[2][z.uType], zigZag[3][z.dType]);
                z.src = piece.cropZig(srcMap, mask);
                z.oLine = piece.getOutline(z.src, 0xFF8899AA);
                z.oLine2 = piece.getOutline(z.oLine,0xFF667788);
                zigInfo[x][y] = z;
            }
        }

        paintView = (PaintView)findViewById(R.id.paintview);

        TextView tv = findViewById(R.id.go);
        paintView.init(screenX, screenY, zw, x5, pw, nw,this, tv);

        paintView.load(zigInfo, 4, 7);

        int mxSize = zigX * zigY;
        reZigs = new ArrayList<>();
        int []wk = new int[mxSize];
        int wkIdx = new Random().nextInt(mxSize/2);
        for (int i = 0; i < mxSize ; i++) {
            int tmp = wkIdx + new Random().nextInt(mxSize/3);
            if (tmp >= mxSize) {
                tmp -= mxSize;
            }
            if (wk[tmp] != 0) {
                while (wk[tmp] == 1) {
                    tmp++;
                    if (tmp >= mxSize)
                        tmp = 0;
                }
            }
            int x = tmp / zigX;
            int y = tmp - x * zigX;

            reZigs.add(x*1000+y);
            wk[tmp] = 1;
            wkIdx = tmp;
        }
        RecyclerView zigRecycler = mActivity.findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        zigRecycler.setLayoutManager(mLinearLayoutManager);
        zigAdapter = new ReZigAdapter(reZigs);
        zigRecycler.setAdapter(zigAdapter);
    }

    void generatePixels(Bitmap fullImage) {

        puzzleWidth = fullImage.getWidth();
        puzzleHeight = fullImage.getHeight();

        Log.w("FullImage", puzzleWidth+" x "+ puzzleHeight);
        pw = puzzleHeight / 20;
        x5 = pw*5/14;
        zw = x5 + x5 + pw;
        piece = new Piece(zw, x5, pw);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenX = metrics.widthPixels;
        screenY = metrics.heightPixels;
        nw = Math.round(metrics.densityDpi/2);
    }
    private Bitmap maskMerge(Bitmap maskL, Bitmap maskR, Bitmap maskU, Bitmap maskD) {
        Bitmap tMap = Bitmap.createBitmap(zw, zw, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tMap);
        canvas.drawBitmap(maskL, 0,0, null);
        canvas.drawBitmap(maskR, 0,0, null);
        canvas.drawBitmap(maskU, 0,0, null);
        canvas.drawBitmap(maskD, 0,0, null);
        return tMap;
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

    // ↓ ↓ ↓ P E R M I S S I O N    RELATED /////// ↓ ↓ ↓ ↓
    ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();

    private void askPermission() {
//        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (permissionsToRequest.size() != 0) {
            requestPermissions(permissionsToRequest.toArray(new String[0]),
//            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                    ALL_PERMISSIONS_RESULT);
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList <String> result = new ArrayList<>();
        for (String perm : wanted) if (hasPermission(perm)) result.add(perm);
        return result;
    }
    private boolean hasPermission(String permission) {
        return (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALL_PERMISSIONS_RESULT) {
            for (String perms : permissionsToRequest) {
                if (hasPermission(perms)) {
                    permissionsRejected.add(perms);
                }
            }
            if (permissionsRejected.size() > 0) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    String msg = "These permissions are mandatory for the application. Please allow access.";
                    showDialog(msg);
                }
            } else
                Toast.makeText(mContext, "Permissions not granted.", Toast.LENGTH_LONG).show();
        }
    }
    private void showDialog(String msg) {
        showMessageOKCancel(msg,
                (dialog, which) -> requestPermissions(permissionsRejected.toArray(
                        new String[0]), ALL_PERMISSIONS_RESULT));
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

// ↑ ↑ ↑ ↑ P E R M I S S I O N    RELATED /////// ↑ ↑ ↑

}

//
//        for (int y = 0; y < zigY-1; y++) {
//            String s = "";
//            for (int x = 0; x < zigX - 1; x++) {
//                s +="  ("+zigInfo[x][y].dType+"."+zigInfo[x][y+1].uType+") ";
//            }
//            Log.w("y "+y, s);
//        }
