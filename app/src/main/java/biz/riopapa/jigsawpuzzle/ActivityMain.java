package biz.riopapa.jigsawpuzzle;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Timer;

import biz.riopapa.jigsawpuzzle.adaptors.ImageSelAdapter;
import biz.riopapa.jigsawpuzzle.databinding.ActivityMainBinding;
import biz.riopapa.jigsawpuzzle.func.BuildJigFilesFromDrawable;
import biz.riopapa.jigsawpuzzle.func.DownloadTask;
import biz.riopapa.jigsawpuzzle.func.FileIO;
import biz.riopapa.jigsawpuzzle.func.HistoryGetPut;
import biz.riopapa.jigsawpuzzle.func.Permission;
import biz.riopapa.jigsawpuzzle.func.PhoneMetrics;
import biz.riopapa.jigsawpuzzle.images.ImageStorage;
import biz.riopapa.jigsawpuzzle.model.GVal;
import biz.riopapa.jigsawpuzzle.model.History;
import biz.riopapa.jigsawpuzzle.model.JigFile;

public class ActivityMain extends Activity implements DownloadCompleteListener {

    public static Activity mActivity;

    public static Context mContext;

    ActivityMainBinding binding;

//    public static int maxImageCount;
    RecyclerView imageRecyclers;
    public static ImageSelAdapter imageSelAdapter;
    public static int gameMode;
    public static String nowVersion = "000101";

    public static int chosenNumber;
    public static String currGame, currGameLevel;
    public static int currLevel;
    public static GVal gVal;
    public static ArrayList<History> histories = null;

    final public static int ANI_TO_FPS = 10123;
    final public static int ANI_ANCHOR = 10321;
    final public static int GAME_SELECT_IMAGE = 2011;
    final public static int GAME_SELECT_LEVEL = 2022;
    final public static int GAME_STARTED = 2033;
    final public static int GAME_PAUSED = 2044;
    final public static int GAME_GOBACK_TO_MAIN = 2047;

    final public static int GAME_COMPLETED = 3333;

    final public static String[] levelNames = {"Easy", "Norm", "Hard", "Guru"};

    public static int screenX, screenY, screenBottom; // physical screen size, center puzzleBox

    public static float fPhoneInchX, fPhoneInchY;
    public static Bitmap[][] srcMaskMaps, outMaskMaps;
    public static Bitmap[] fireWorks, congrats, jigDones;

    /*
    ** Following will be handled with Set Menu
     */
    public static boolean vibrate = true;
    public static int showBack = 0, showBackCount = 0;
    public static final int showBackLoop = 3333;
    public static boolean sound = false;
    public static int backColor = 0;
    public static String appVersion = "";

    public static boolean debugMode = false;
    public static boolean showCR = false;
    public final static long INVALIDATE_INTERVAL = 30;

    // Google Drive related variables
    final String imageListId = "1HoO4s3dv4i8GAG5s5Nsl6HzMzF5TQ9Hf";

    final String imageListOnDrive = "_imageList.txt";

    public static String downloadFileName = "";
    public static int downloadPosition = -1;
    public static long downloadSize = 0;

    public static ArrayList<JigFile> jigFiles = null;
    public static JigFile jigFile = null;
    public static String jpgFolder = "jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("Main","onCreate gameMode="+gameMode);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            Permission.ask(this, this, info);
        } catch (Exception e) {
            Log.e("Permission", "No Permission " + e);
        }

        mContext = getApplicationContext();
        mActivity = this;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences sharedPref = getSharedPreferences("params", Context.MODE_PRIVATE);
        showBack = sharedPref.getInt("showBack", 0);
        vibrate = sharedPref.getBoolean("vibrate", true);
        sound = sharedPref.getBoolean("sound", true);
        backColor = sharedPref.getInt("backColor", 0);
        appVersion = sharedPref.getString("appVersion","none");

        new PhoneMetrics(this);

        // create jigFiles with existing files
        new BuildJigFilesFromDrawable();

        /* fileName, position status
            1) file = text, pos = -1; download jigsaw.txt
            2) file = .jpg, pos = n ; downloading jpg start @ onDownloadCompleted
            3) file = null, pos = n ; downloading & thumbnail created @ downloadTask
            4) continue to 2)       ; find next items @ onDownloadCompleted
         */
        downloadFileName = imageListOnDrive;
        downloadPosition = -1;
        DownloadTask task = new DownloadTask( this, imageListId, "", downloadFileName);
        task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("Main ","onResume "+gameMode+" currGame="+currGame);
//        invalidateTimer = new Timer();
        ImageView imageView = findViewById(R.id.chosen_image);
        imageView.setVisibility(View.GONE);

        if (histories == null || !appVersion.equals(nowVersion))
            new HistoryGetPut().set(this);

        // ready image recycler view
        imageRecyclers = findViewById(R.id.imageRecycler);
        imageRecyclers.setVisibility(View.VISIBLE);
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
    public void onDownloadComplete() {

        // if download if completed
        // downloadFile name = .jpg, downloadPosition > 0  then update thumbnail
        // downloadFile name =
        if (downloadPosition > 0)   // continue to check possible download
            downloadNewJpg();

        Log.w("download","complted "+downloadFileName+" pos="+downloadPosition);
        // at first time, drive image list from drive is loaded

        if (downloadFileName != null && downloadFileName.equals(imageListOnDrive)) {
            String str = FileIO.readTextFile("", imageListOnDrive); // no dir for list
            String[] ss = str.split("\n");
            boolean newlyAdd = false;
            for (int i = 1; i < ss.length; i++) {
                String[] imgInfo = ss[i].split(";");
                String nGame = imgInfo[0].trim();
                if (newJpgFile(nGame)) {
                    JigFile jf = new JigFile();
                    jf.game = nGame;
                    jf.imageId = imgInfo[1].trim();
                    jf.timeStamp = imgInfo[2].trim();       // not used now
                    jf.keywords = imgInfo[3].trim();        // not used now
                    jigFiles.add(jf);
                    imageSelAdapter.notifyItemInserted(jigFiles.size()-1);
                    newlyAdd = true;
                }
            }
            if (newlyAdd)
                downloadNewJpg();
        }
    }

    private boolean newJpgFile(String game) {
        for (int i = new ImageStorage().count(); i < jigFiles.size(); i++) {
            if (game.equals(jigFiles.get(i).game))
                return false;
        }
        return true;
    }

    private void downloadNewJpg() {
        // at least one jpg has been done
        Log.w("begin","downloadNewJpg ");
        for (int i = new ImageStorage().count(); i < jigFiles.size(); i++) {
            JigFile jf = jigFiles.get(i);
            if (jf.thumbnailMap == null &&
                FileIO.existJPGFile(jpgFolder, jf.game+".jpg") == null) {
                downloadFileName = jf.game + ".jpg";
                downloadPosition = i;
                Log.w("pos="+i,"downloadNewJpg "+downloadFileName);
                DownloadTask task = new DownloadTask(this, jf.imageId, jpgFolder, downloadFileName);
                task.execute();
                return;
            }
        }
        downloadPosition = -1;
    }

    @Override
    protected void onPause() {
//        new GValGetPut().put(currGameLevel, gVal, this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
//    // ↓ ↓ ↓ P E R M I S S I O N    RELATED /////// ↓ ↓ ↓ ↓
//    ArrayList<String> permissions = new ArrayList<>();
//    private final static int ALL_PERMISSIONS_RESULT = 101;
//    ArrayList<String> permissionsToRequest;
//    ArrayList<String> permissionsRejected = new ArrayList<>();
//
////    private void askPermission() {
//////        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
////        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
////        permissionsToRequest = findUnAskedPermissions(permissions);
////        if (permissionsToRequest.size() != 0) {
////            requestPermissions(permissionsToRequest.toArray(new String[0]),
//////            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
////                    ALL_PERMISSIONS_RESULT);
////        }
////    }
////
////    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
////        ArrayList <String> result = new ArrayList<>();
////        for (String perm : wanted) if (hasPermission(perm)) result.add(perm);
////        return result;
////    }
//    private boolean hasPermission(String permission) {
//        return (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == ALL_PERMISSIONS_RESULT) {
//            for (String perms : permissionsToRequest) {
//                if (hasPermission(perms)) {
//                    permissionsRejected.add(perms);
//                }
//            }
//            if (permissionsRejected.size() > 0) {
//                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
//                    String msg = "These permissions are mandatory for the application. Please allow access.";
//                    showDialog(msg);
//                }
//            } else
//                Toast.makeText(mContext, "Permissions not granted.", Toast.LENGTH_LONG).show();
//        }
//    }
//    private void showDialog(String msg) {
//        showMessageOKCancel(msg,
//                (dialog, which) -> requestPermissions(permissionsRejected.toArray(
//                        new String[0]), ALL_PERMISSIONS_RESULT));
//    }
//    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(this)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show();
//    }
//
//// ↑ ↑ ↑ ↑ P E R M I S S I O N    RELATED /////// ↑ ↑ ↑

}

//
//        for (int y = 0; y < jigROWs-1; y++) {
//            String s = "";
//            for (int x = 0; x < jigCOLUMNs - 1; x++) {
//                s +="  ("+jigInfo[x][y].dType+"."+jigInfo[x][y+1].uType+") ";
//            }
//            Log.w("y "+y, s);
//        }
