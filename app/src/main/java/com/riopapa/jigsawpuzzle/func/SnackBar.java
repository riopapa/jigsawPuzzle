package com.riopapa.jigsawpuzzle.func;

import static com.riopapa.jigsawpuzzle.ActivityMain.mActivity;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.riopapa.jigsawpuzzle.R;

public class SnackBar {
    public void show(Activity activity, String title, String text) {
        View  paintView = activity.findViewById(R.id.layout_jigsaw);
        Snackbar snackbar = Snackbar.make(paintView, title, Snackbar.LENGTH_SHORT);
        View sView = activity.getLayoutInflater().inflate(R.layout.snack_message, null);

        TextView tv1 = sView.findViewById(R.id.text_header);
        TextView tv2 = sView.findViewById(R.id.text_body);

        tv1.setText(title);
        tv2.setText(text);

        // now change the layout of the ToastText
        Snackbar.SnackbarLayout snackBarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarLayout.setBackgroundColor(0x0033FFFF);  // transparent background
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackBarLayout.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        sView.setLayoutParams(params);
        snackBarLayout.addView(sView, 0);

        snackbar.show();
    }

}
