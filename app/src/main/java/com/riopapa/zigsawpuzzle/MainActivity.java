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
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.zigsawpuzzle.model.JigTable;

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
    public static int outerSize, innerSize, x5;  // real piece size
    public static int recySize, picSize; // recycler size, at Paintview size;
    public static int jigCntX, jigCntY; // jigsaw slide x, y count
    // class modules
    public static Piece piece;
    public static Bitmap fullImage, grayedImage, brightImage;
    public static int screenX, screenY, fullWidth, fullHeight;
    public static JigTable[][] jigTables;
    public static int jigX, jigY, jigX00Y;   // array x, y, x*10000+y
    public static float jPosX, jPosY; // absolute x,y position drawing current jigsaw
    public static float pxVal, dipVal; // absolute x,y position drawing current jigsaw
    public static float fullScale; // fullImage -> screenX
    public static RecyclerView zigRecyclerView;
    public static Bitmap [][] maskMaps, outMaps;
    public static int screenOffsetX, screenOffsetY; // paintView block left top position
    public static int imageOffsetX, imageOffsetY; // image Offset View left Top
    public static PaintView paintView;
    public static ArrayList<Integer> recyclerJigs;
    public static Activity mActivity;
    public static RecycleJigAdapter jigAdapter;

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
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
        grayedImage = null;

        jigCntX = 15;
        jigCntY = 15;
        initiatePixels(fullImage, jigCntX, jigCntY);

        jigTables = new JigTable[jigCntX][jigCntY];
        new SetBoundaryVals(jigTables, jigCntX, jigCntY);

        maskMaps = new Masks().make(mContext, outerSize);
        outMaps = new Masks().makeOut(mContext, outerSize);

        Log.w("sizeCheck","fullWidth="+ fullWidth +", fullHeight="+ fullHeight +", outerSize="+ outerSize +", x5="+x5+", innerSize="+ innerSize);

        jigX00Y = -1;
        paintView = findViewById(R.id.paintview);
        paintView.init(this, tvGo);
        paintView.load(jigTables, jigCntX/2, jigCntY/2);

        makeRecycleArrays();

        zigRecyclerView = mActivity.findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        zigRecyclerView.getLayoutParams().height = recySize;
        jigAdapter = new RecycleJigAdapter();
        ItemTouchHelper.Callback mainCallback = new MyItemTouchHelper(jigAdapter, mContext);
        ItemTouchHelper mainItemTouchHelper = new ItemTouchHelper(mainCallback);
        jigAdapter.setTouchHelper(mainItemTouchHelper);
        mainItemTouchHelper.attachToRecyclerView(zigRecyclerView);
        zigRecyclerView.setAdapter(jigAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        zigRecyclerView.setLayoutManager(mLinearLayoutManager);
        new ImageGray().build();
        new ImageBright().build();
        fullScale = screenX / fullWidth;
        jigX = 4; jigY = 5;
        if (jigTables[jigX][jigY].oLine == null)
            piece.make(jigX, jigY);
        iv99.setImageBitmap(jigTables[jigX][jigY].oLine);
    }

    private static void makeRecycleArrays() {
        int mxSize = jigCntX * jigCntY;
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
            int x = tmp / jigCntX;
            int y = tmp - x * jigCntX;

            recyclerJigs.add(x*10000+y);
            wk[tmp] = 1;
            wkIdx = tmp;
        }
    }

    void initiatePixels(Bitmap fullImage, int xMax, int yMax) {

        fullWidth = fullImage.getWidth();
        fullHeight = fullImage.getHeight();
        jPosX = -1; // prevent drawing without preload
        innerSize = fullHeight / (yMax+1);
        if (fullWidth / (xMax+1) < innerSize)
            innerSize = fullWidth / (xMax-1);
        Log.w("FullImage", fullWidth +" x "+ fullHeight +" innerSize = "+ innerSize);
        x5 = innerSize *5/14;
        outerSize = x5 + x5 + innerSize;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenX = metrics.widthPixels;
        screenY = metrics.heightPixels;
        Log.w("Main","Screen "+screenX+" x "+screenY);
//        innerDSize = screenX / 12; //Math.round(metrics.densityDpi/2);
//        recySize = innerDSize * (5+5+14)/ 14;
        pxVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1000f,
                this.getApplicationContext().getResources().getDisplayMetrics());
                        // about 10 in recycler view
        dipVal = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1000f,
                this.getApplicationContext().getResources().getDisplayMetrics());
        recySize = (int) (pxVal / 5f);
//        picSize = (int) ((float) recySize * pxVal / dipVal);
        picSize = recySize;
        Log.w("TypeValue","pxVal="+ pxVal+", dipVal="+dipVal+" recySize="+ recySize +" picSize="+ picSize);

        piece = new Piece(outerSize, x5, innerSize);
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
//        for (int y = 0; y < jigCntY-1; y++) {
//            String s = "";
//            for (int x = 0; x < jigCntX - 1; x++) {
//                s +="  ("+jigInfo[x][y].dType+"."+jigInfo[x][y+1].uType+") ";
//            }
//            Log.w("y "+y, s);
//        }
