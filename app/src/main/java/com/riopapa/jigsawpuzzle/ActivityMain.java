package com.riopapa.jigsawpuzzle;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.riopapa.jigsawpuzzle.databinding.ActivityMainBinding;
import com.riopapa.jigsawpuzzle.func.HistoryGetPut;
import com.riopapa.jigsawpuzzle.func.PhoneMetrics;
import com.riopapa.jigsawpuzzle.model.GVal;
import com.riopapa.jigsawpuzzle.model.History;

import java.util.ArrayList;
import java.util.Timer;

public class ActivityMain extends Activity {

    public static Activity mActivity;

    public static Context mContext;

    public static Timer invalidateTimer;
    ActivityMainBinding binding;

//    public static int maxImageCount;
    RecyclerView imageRecyclers;
    ImageSelAdapter imageSelAdapter;

    public static int gameMode;
    public static int appVersion = 000100;

    public static int chosenNumber;
    public static String currGame, currGameLevel;
    public static int currLevel;
    public static GVal gVal;
    public static ArrayList<History> histories = null;

    final public static int ANI_TO_FPS = 10123;
    final public static int ANI_ANCHOR = 10321;
    final public static int GAME_NEW = 0;
    final public static int GAME_SELECT_IMAGE = 2011;
    final public static int GAME_SELECT_LEVEL = 2022;
    final public static int GAME_STARTED = 2033;
    final public static int GAME_PAUSED = 2044;
    final public static int GAME_GOBACK_TO_MAIN = 2047;
    final public static int GAME_COMPLETED = 2055;
    final public static int GAME_ALL_COMPLETED = 2088;

    final public static String[] levelNames = {"Easy", "Norm", "Hard", "Expert"};

    public static int screenX, screenY, screenBottom; // physical screen size, center puzzleBox


    public static float fPhoneInchX, fPhoneInchY;
    public static Bitmap[][] srcMaskMaps, outMaskMaps;
    public static Bitmap[] fireWorks, congrats;

    /*
    ** Following will be handled with Set Menu
     */
    public static boolean vibrate = true;
    public static boolean showBack = true;
    public static boolean sound = false;

    public static boolean debugMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("Main","onCreate gameMode="+gameMode);
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

        SharedPreferences sharedPref = getSharedPreferences("params", Context.MODE_PRIVATE);
        showBack = sharedPref.getBoolean("showBack", true);
        vibrate = sharedPref.getBoolean("vibrate", true);
        sound = sharedPref.getBoolean("sound", true);

//        View decorView = getWindow().getDecorView();
//        int uiOptions = decorView.getSystemUiVisibility();
//        uiOptions |= SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        new PhoneMetrics(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.w("Main ","onResume "+gameMode+" currGame="+currGame);
        invalidateTimer = new Timer();
        RecyclerView recyclerView = findViewById(R.id.imageRecycler);
        recyclerView.setVisibility(View.VISIBLE);
        ImageView imageView = findViewById(R.id.chosen_image);
        imageView.setVisibility(View.GONE);


        // if newly restarted then read gVal
//        if (gameMode == 0) {
//            SharedPreferences sharedPref = mContext.getSharedPreferences("saved", Context.MODE_PRIVATE);
//            String prevGame = sharedPref.getString("prevGame", "");
//            if (prevGame.equals("")) {
//                gameMode = GAME_NEW;
//                initiateGame();
//            }
//        }

        if (histories == null)
            histories = new HistoryGetPut().get(this);

        // get physical values depend on Phone
        // then set picXSizes
//        new SetPicSizes(screenX);

//        maxImageCount = new ImageStorage().count();

        // ready image recycler view
        imageRecyclers = findViewById(R.id.imageRecycler);
        imageSelAdapter = new ImageSelAdapter();
        imageRecyclers.setAdapter(imageSelAdapter);

        // StaggeredGridLayoutManager makes various height
        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager((fPhoneInchX > 3) ?3: 2, StaggeredGridLayoutManager.VERTICAL);
        imageRecyclers.setLayoutManager(staggeredGridLayoutManager);

        if (gameMode == GAME_GOBACK_TO_MAIN) {
            imageSelAdapter.notifyItemChanged(chosenNumber);
        }
        gameMode = GAME_SELECT_IMAGE;

    }

    @Override
    protected void onPause() {
//        new GValGetPut().put(currGameLevel, gVal, this);
        super.onPause();
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
