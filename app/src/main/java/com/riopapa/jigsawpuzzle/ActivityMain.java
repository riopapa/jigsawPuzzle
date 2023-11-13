package com.riopapa.jigsawpuzzle;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.riopapa.jigsawpuzzle.databinding.ActivityMainBinding;
import com.riopapa.jigsawpuzzle.func.PhoneMetrics;
import com.riopapa.jigsawpuzzle.func.SetPicSizes;
import com.riopapa.jigsawpuzzle.func.ImageStorage;
import com.riopapa.jigsawpuzzle.func.VarsGetPut;

import java.util.ArrayList;
import java.util.Timer;

public class ActivityMain extends Activity {

    public static Activity mActivity;

    public static Context mContext;

    public static Timer invalidateTimer;
    ActivityMainBinding binding;
    RecyclerView imageRecyclers;
    ImageSelAdapter imageSelAdapter;

    public static Vars vars;


    final public static int ANI_TO_PAINT = 123;
    final public static int ANI_ANCHOR = 321;
    final public static int GAME_NEW = 0;
    final public static int GAME_SELECT_IMAGE = 11;
    final public static int GAME_SELECT_LEVEL = 22;
    final public static int GAME_STARTED = 33;
    final public static int GAME_PAUSED = 44;
    final public static int GAME_GOBACK_TO_MAIN = 47;
    final public static int GAME_COMPLETED = 55;
    final public static int GAME_ALL_COMPLETED = 88;

    final public static String[] gameLevels= {"Easy", "Normal", "Hard", "Expert"};

    public static int screenX, screenY; // physical screen size, center puzzleBox

    public static float fPhoneInchX, fPhoneInchY;
    public static Bitmap[][] srcMaskMaps, outMaskMaps;


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

        vars = new VarsGetPut().get(this);
        if (vars == null)
            vars = new Vars();

        // get physical values depend on Phone
        new PhoneMetrics(this);
        // then set picXSizes
        new SetPicSizes(screenX);

        vars.maxImageCount = new ImageStorage().count();

//        binding.imageRecycler.getLayoutParams().height = vars.screenY * 8 / 10;

        // ready image recycler view
        imageRecyclers = findViewById(R.id.imageRecycler);
        imageSelAdapter = new ImageSelAdapter();
        imageRecyclers.setAdapter(imageSelAdapter);

        // StaggeredGridLayoutManager makes various height
        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager((fPhoneInchX > 3) ?3: 2, StaggeredGridLayoutManager.VERTICAL);
        imageRecyclers.setLayoutManager(staggeredGridLayoutManager);
        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

    }


    @Override
    protected void onResume() {
        super.onResume();

        invalidateTimer = new Timer();

//        if (vars.gameMode == GAME_PAUSED) {
//            // todo: ask whether to continue
//            Intent intent = new Intent(this, ActivitySelLevel.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else
            vars.gameMode = GAME_SELECT_IMAGE;

    }


    @Override
    protected void onPause() {
        new VarsGetPut().put(vars, this);
        super.onPause();
    }

    // make recycler list with random jigsaws




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
