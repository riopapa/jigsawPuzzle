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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityMainBinding;
import com.riopapa.jigsawpuzzle.func.AdjustThumbNail;
import com.riopapa.jigsawpuzzle.func.FullRecyclePiece;
import com.riopapa.jigsawpuzzle.model.FloatPiece;
import com.riopapa.jigsawpuzzle.model.JigTable;
import com.riopapa.jigsawpuzzle.func.intGlobalValues;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    public static TextView tvLeft, tvRight;

    public static ImageView iv1, imageAnswer, thumbNail, moveL, moveR, moveU, moveD;

    public static int outerSize, innerSize, gapSize;  // real piece size

    public static int recySize, picOSize, picISize, picGap, picHSize;
        // recycler size, at Paintview size;
        // picOSize : picture outer size
        // picISize : picture inner size
        // picGap   : gap between picISize and picOSize
        // picHSize : half of picOSize;
        // pieceISize: one PieceSize for slicing images
        // pieceOSize: one Outer for slicing images

    public static Piece piece;

    public static int jigRecyclePos; // jigsaw slide x, y count

    public static Bitmap fullImage, grayedImage, brightImage;

    public static JigTable[][] jigTables;

    public static int jigCOLUMNs, jigROWs; // jigsaw slices column by row

    public static int nowC, nowR, jigCR;   // fullImage piece array column, row , x*10000+y


    public static int offsetC, offsetR; // show offset Column, Row;

    public static int jPosX, jPosY; // absolute x,y rightPosition drawing current jigsaw

    public static int screenX, screenY, puzzleSize; // physical screen size, center puzzleBox

    public static int fullWidth, fullHeight; // puzzle photo size (in dpi)
    public static float fullRatio;  // 0.7f something
    public static boolean oneItemSelected; // now on dragging ..

    public static boolean hangOn; // wait while one action completed
    public static RecyclerView jigRecyclerView;

    public static Bitmap [][] maskMaps, outMaps;

    public static int baseX, baseY; // puzzle view x, y offset

    public static PaintView paintView;

    public static ArrayList<Integer> allPossibleJigs, activeRecyclerJigs;
    // allRandomJigs contains jigsaws id which is not moved to floatingPiece
    // activeRecyclerJigs contains available jigsaws currently

    public static ArrayList<FloatPiece> fPs;    // floating jigsaws
    public static Activity mActivity;

    public static Context mContext;

    public static RecycleJigListener jigRecycleAdapter;

    public static int showMax;   // how many pieces can be in columns / rows
    public static int showShift;

    public static Timer invalidateTimer;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());

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
        thumbNail = findViewById(R.id.thumbnail);

        moveL = findViewById(R.id.move_left);
        moveR = findViewById(R.id.move_right);
        moveU = findViewById(R.id.move_up);
        moveD = findViewById(R.id.move_down);

        binding.moveLeft.setOnClickListener(v -> {
            offsetC -= showShift;
            if (offsetC < 0)
                offsetC = 0;
            copy2RecyclerPieces();
        });
        binding.moveRight.setOnClickListener(v -> {
            offsetC += showShift;
            if (offsetC >= jigCOLUMNs - showMax)
                offsetC = jigCOLUMNs - showMax;
            copy2RecyclerPieces();
        });
        binding.moveUp.setOnClickListener(v -> {
            offsetR -= showShift;
            if (offsetR < 0)
                offsetR = 0;
            copy2RecyclerPieces();
        });
        binding.moveDown.setOnClickListener(v -> {
            offsetR += showShift;
            if (offsetR >= jigROWs - showMax)
                offsetR = jigROWs - showMax;
            copy2RecyclerPieces();
        });

        fullImage = BitmapFactory.decodeResource
                (mContext.getResources(), R.mipmap.zigsaw01, null);
        fullWidth = fullImage.getWidth();
        fullHeight = fullImage.getHeight();
        fullRatio = fullHeight / fullWidth;     // usually under 1.0 if landscape

        grayedImage = null;
        jigCOLUMNs = 8;
        jigROWs = jigCOLUMNs * fullHeight / fullWidth;  // to avoid over y size

// Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        new intGlobalValues(this, this);
        new AdjustThumbNail();

        jigTables = new JigTable[jigCOLUMNs][jigROWs];
        new com.riopapa.jigsawpuzzle.func.initJigTable(jigTables, jigCOLUMNs, jigROWs);

        maskMaps = new Masks().make(mContext, outerSize);
        outMaps = new Masks().makeOut(mContext, outerSize);

        paintView = findViewById(R.id.paintview);
        paintView.init(this);

        new FullRecyclePiece();

        jigRecyclerView = mActivity.findViewById(R.id.piece_recycler);
        int layoutOrientation = RecyclerView.HORIZONTAL;
        jigRecyclerView.getLayoutParams().height = recySize;
        jigRecycleAdapter = new RecycleJigListener();
//        ItemTouchHelper.Callback mainCallback = new PaintViewTouchCallback(jigRecycleAdapter, mContext);
//        ItemTouchHelper mainItemTouchHelper = new ItemTouchHelper(mainCallback);
//        jigRecycleAdapter.setTouchHelper(mainItemTouchHelper);
        ItemTouchHelper helper = new ItemTouchHelper(new PaintViewTouchCallback(jigRecycleAdapter, mContext));;
//        jigRecycleAdapter.setTouchHelper(mainItemTouchHelper);

        helper.attachToRecyclerView(jigRecyclerView);
        jigRecyclerView.setAdapter(jigRecycleAdapter);
        LinearLayoutManager mLinearLayoutManager
                = new LinearLayoutManager(mContext, layoutOrientation, false);
        jigRecyclerView.setLayoutManager(mLinearLayoutManager);
        copy2RecyclerPieces();

    }

    void showThumbNail() {
        int h, w, rectSize, xOff, yOff;
        if (fullHeight > fullWidth) {
            h = 500;
            w = h * fullWidth / fullHeight;
            rectSize = 500 * (showMax) / jigROWs;    // 24 to show line boundary
            xOff = offsetC * 500  / jigROWs;
            yOff = offsetR * 500 / jigROWs;
        } else {
            w = 500;
            h = w * fullHeight / fullWidth;
            rectSize = 500 * (showMax) / jigCOLUMNs;
            xOff = offsetC * 500  / jigCOLUMNs;
            yOff = offsetR * 500 / jigCOLUMNs;
        }
        if (xOff + rectSize >= w)
            xOff = w - rectSize;
        if (yOff + rectSize >= h)
            yOff = h - rectSize;

        Bitmap thumb = Bitmap.createScaledBitmap(fullImage, w, h, true);
        Canvas canvas = new Canvas(thumb);
        Paint paint = new Paint();
        paint.setColor(0x6fff0000);
        paint.setStrokeWidth(10f);
        canvas.drawLine(xOff, yOff, xOff+rectSize, yOff, paint);
        canvas.drawLine(xOff, yOff, xOff, yOff+rectSize, paint);
        canvas.drawLine(xOff+rectSize, yOff, xOff+rectSize, yOff+rectSize, paint);
        canvas.drawLine(xOff, yOff+rectSize, xOff+rectSize, yOff+rectSize, paint);

        binding.thumbnail.setImageBitmap(thumb);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paintView != null) {
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    paintView.invalidate();
                }
            };
            invalidateTimer = new Timer();
            invalidateTimer.schedule(tt, 200, 150);
        }
        showThumbNail();

    }


    @Override
    protected void onPause() {
        super.onPause();
        invalidateTimer.cancel();
    }

    // make recycler list with random jigsaws


    // build recycler from all pieces within in leftC, rightC, topR, bottomR
    public void copy2RecyclerPieces() {
        activeRecyclerJigs = new ArrayList<>();
        for (int i = 0; i < allPossibleJigs.size(); i++) {
            int cr = allPossibleJigs.get(i);
            int c = cr / 10000;
            int r = cr - c * 10000;
            if (!jigTables[c][r].locked && !jigTables[c][r].outRecycle &&
                    c >= offsetC && c < offsetC + showMax && r >= offsetR && r < offsetR + showMax) {
                activeRecyclerJigs.add(cr);
            }
        }
        jigRecycleAdapter.notifyDataSetChanged();
        Log.w("jigRecycleAdapter", "size="+activeRecyclerJigs.size());
        showThumbNail();

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
