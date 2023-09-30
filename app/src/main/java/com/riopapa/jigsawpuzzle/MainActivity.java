package com.riopapa.jigsawpuzzle;

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
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityMainBinding;
import com.riopapa.jigsawpuzzle.model.JigTable;
import com.riopapa.jigsawpuzzle.func.intGlobalValues;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    TextView tvLeft, tvRight;

    public static ImageView iv1, imageAnswer;

    public static int outerSize, innerSize, pieceGap;  // real piece size

    public static int recySize, picOSize, picISize, picGap, picHSize;
        // recycler size, at Paintview size;
        // picOSize : picture outer size
        // picISize : picture inner size
        // picGap   : gap between picISize and picOSize
        // picHSize : half of picOSize;
    // class modules

    public static Piece piece;

    public static int jigRecyclePos; // jigsaw slide x, y count

    public static Bitmap fullImage, grayedImage, brightImage;

    public static JigTable[][] jigTables;

    public static int jigCOLUMNs, jigROWs; // jigsaw slices column by row

    public static int nowC, nowR, jigCR;   // fullImage piece array column, row , x*10000+y

    public static int countRC, leftC, rightC, topR, bottomR; // on screen drawable

    public static int offsetC, offsetR; // show offset Column, Row;

    public static int jPosX, jPosY; // absolute x,y position drawing current jigsaw

    public static int screenX, screenY, puzzleSize; // physical screen size, center puzzleBox

    public static int fullWidth, fullHeight; // puzzle photo size (in dpi)

    public static RecyclerView zigRecyclerView;

    public static Bitmap [][] maskMaps, outMaps;

    public static int baseX, baseY; // puzzle view x, y offset

    public static PaintView paintView;

    public static ArrayList<Integer> recyclerJigs;

    public static Activity mActivity;

    public static Context mContext;

    public static RecycleJigAdapter jigRecycleAdapter;

    public static int showMax;   // how many pieces can be in columns / rows
    public static int showShift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
//        }
        mContext = getApplicationContext();
        mActivity = this;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tvLeft = findViewById(R.id.debug_left);
        tvRight = findViewById(R.id.debug_right);

        iv1 = findViewById(R.id.image1);
        imageAnswer = findViewById(R.id.image_answer);

        TextView leftBtn = findViewById(R.id.move_left);
        leftBtn.setOnClickListener(v -> {
            offsetC -= showShift;
            if (offsetC < 0)
                offsetC = 0;
            paintView.invalidate();
        });
        TextView rightBtn = findViewById(R.id.move_right);
        rightBtn.setOnClickListener(v -> {
            offsetC += showShift;
            if (offsetC >= jigCOLUMNs - showMax)
                offsetC = jigCOLUMNs - showMax;
            paintView.invalidate();
        });
        TextView upBtn = findViewById(R.id.move_up);
        upBtn.setOnClickListener(v -> {
            offsetR -= showShift;
            if (offsetR < 0)
                offsetR = 0;
            paintView.invalidate();
        });
        TextView downBtn = findViewById(R.id.move_down);
        downBtn.setOnClickListener(v -> {
            offsetR += showShift;
            if (offsetR >= jigROWs - showMax)
                offsetR = jigROWs - showMax;
            paintView.invalidate();
        });

        fullImage =
                BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.scenary, null);
        grayedImage = null;

        jigCOLUMNs = 20;
        jigROWs = 20;

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = decorView.getSystemUiVisibility();
        uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        new intGlobalValues(this, this);

        jigTables = new JigTable[jigCOLUMNs][jigROWs];
        new com.riopapa.jigsawpuzzle.func.initJigTable(jigTables, jigCOLUMNs, jigROWs);

        maskMaps = new Masks().make(mContext, outerSize);
        outMaps = new Masks().makeOut(mContext, outerSize);

        jigCR = -1;
        paintView = findViewById(R.id.paintview);
        paintView.init(this, tvLeft, tvRight);

        makeRecycleArrays();

        zigRecyclerView = mActivity.findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        zigRecyclerView.getLayoutParams().height = recySize;
        jigRecycleAdapter = new RecycleJigAdapter();
        ItemTouchHelper.Callback mainCallback = new RecycleTouchHelper(jigRecycleAdapter, mContext);
        ItemTouchHelper mainItemTouchHelper = new ItemTouchHelper(mainCallback);
        jigRecycleAdapter.setTouchHelper(mainItemTouchHelper);
        mainItemTouchHelper.attachToRecyclerView(zigRecyclerView);
        zigRecyclerView.setAdapter(jigRecycleAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        zigRecyclerView.setLayoutManager(mLinearLayoutManager);
        new ImageGray().build();
        new ImageBright().build();



    }

    private static void makeRecycleArrays() {
        int mxSize = jigCOLUMNs * jigROWs;
        recyclerJigs = new ArrayList<>();
        int []wk = new int[mxSize];
        int wkIdx = new Random(System.currentTimeMillis()).nextInt(mxSize/2);
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
            int x = tmp / jigCOLUMNs;
            int y = tmp - x * jigCOLUMNs;

            recyclerJigs.add(x*10000+y);
            wk[tmp] = 1;
            wkIdx = tmp;
        }
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
//        for (int y = 0; y < jigROWs-1; y++) {
//            String s = "";
//            for (int x = 0; x < jigCOLUMNs - 1; x++) {
//                s +="  ("+jigInfo[x][y].dType+"."+jigInfo[x][y+1].uType+") ";
//            }
//            Log.w("y "+y, s);
//        }
