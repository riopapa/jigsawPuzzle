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

import androidx.recyclerview.widget.ItemTouchHelper;
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
    public static int zw, x5, pw, nw;  // real piece size
    int zigX, zigY;

    // class modules
    public static Piece piece;
    public static PieceBitmap pieceBitmap;

    public static Bitmap fullImage;
    public static int screenX, screenY, puzzleWidth, puzzleHeight, jigImageSize;
    public static ZigInfo [][] zigInfo;
    public static int jigX, jigY, jigPos;   // array x, y, x*10000+y
    public static float jPosX, jPosY; // absolute x,y position drawing current jigsaw

    public static RecyclerView zigRecyclerView;
    public static Bitmap [][] maskMaps;

    public static PaintView paintView;
    public static ArrayList<Integer> recyclerJigs;
    public static Activity mActivity;
    public static RecycleJigAdapter zigAdapter;

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

        fullImage =
                BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.scenary, null);

        piece = new Piece(zw, x5, pw);

        initiatePixels(fullImage);

        zigX = 12;
        zigY = 12;
        zigInfo = new ZigInfo[zigX][zigY];
        new ZigSawLRUD(zigInfo, zigX,zigY);

        maskMaps = new MakeCases().generate(mContext, zw, x5, pw);

        Log.w("size","puzzleWidth="+puzzleWidth+", puzzleHeight="+puzzleHeight+", zw="+zw+", x5="+x5+", pw="+pw);

        jigPos = -1;
        paintView = findViewById(R.id.paintview);
        paintView.init(this, tvGo);
//        paintView.load(zigInfo, zigX/2, zigY/2);

        int mxSize = zigX * zigY;
        recyclerJigs = new ArrayList<>();
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

            recyclerJigs.add(x*10000+y);
            wk[tmp] = 1;
            wkIdx = tmp;
        }

        zigRecyclerView = mActivity.findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        zigRecyclerView.getLayoutParams().height = nw + 8;
        zigAdapter = new RecycleJigAdapter();
        ItemTouchHelper.Callback mainCallback = new MyItemTouchHelper(zigAdapter, mContext);
        ItemTouchHelper mainItemTouchHelper = new ItemTouchHelper(mainCallback);
        zigAdapter.setTouchHelper(mainItemTouchHelper);
        mainItemTouchHelper.attachToRecyclerView(zigRecyclerView);
        zigRecyclerView.setAdapter(zigAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        zigRecyclerView.setLayoutManager(mLinearLayoutManager);

    }

    void initiatePixels(Bitmap fullImage) {

        puzzleWidth = fullImage.getWidth();
        puzzleHeight = fullImage.getHeight();
        jPosX = -1; // prevent drawing without preload
        Log.w("FullImage", puzzleWidth+" x "+ puzzleHeight);
        pw = puzzleHeight / 20;
        x5 = pw*5/14;
        zw = x5 + x5 + pw;
        piece = new Piece(zw, x5, pw);
        pieceBitmap = new PieceBitmap();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenX = metrics.widthPixels;
        screenY = metrics.heightPixels;
        nw = screenX / 10; //Math.round(metrics.densityDpi/2);
        Log.w("Main","Screen "+screenX+" x "+screenY+ " jigSize="+jigImageSize+" nw ="+nw);
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
