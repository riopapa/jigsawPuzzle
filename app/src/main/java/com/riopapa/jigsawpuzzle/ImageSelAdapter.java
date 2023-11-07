package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.mActivity;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riopapa.jigsawpuzzle.databinding.ActivityMainBinding;
import com.riopapa.jigsawpuzzle.func.TargetImage;

public class ImageSelAdapter extends RecyclerView.Adapter<ImageSelAdapter.ViewHolder> {

    static Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iVImage;
        TextView tVInfo;

        ViewHolder(final View itemView) {
            super(itemView);

            iVImage = itemView.findViewById(R.id.image);
            iVImage.setOnClickListener(view -> {

                vars.selectedImageNbr = getBindingAdapterPosition();
                vars.selectedImage = new TargetImage().get(vars.selectedImageNbr);
                vars.selectedWidth = vars.selectedImage.getWidth();
                vars.selectedHeight = vars.selectedImage.getHeight();

                launchJigsawActivity();

            });
            tVInfo = itemView.findViewById(R.id.info);
        }

    }

    @Override
    public int getItemCount() {
        context = mContext;
        return vars.possibleImageCount;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Bitmap oMap = new TargetImage().get(position);
        Bitmap bitmap = Bitmap.createScaledBitmap(oMap, oMap.getWidth()/3, oMap.getHeight()/3, true);

        holder.iVImage.setImageBitmap(bitmap);
        holder.tVInfo.setText(" "+position);

    }


    static void launchJigsawActivity() {

        ActivityMainBinding binding = ActivityMainBinding.inflate(mActivity.getLayoutInflater());
        int width = vars.screenX * 8 / 10;
        int height = width * vars.selectedHeight / vars.selectedWidth;
        if (height > vars.screenY * 7 /10)
            height = vars.screenY * 7 / 10;

        binding.selImage.getLayoutParams().width = width;
        binding.selImage.getLayoutParams().height = height;
        binding.selImage.setImageBitmap(vars.selectedImage);

        vars.selectedImageNbr = 0;
        binding.imageRecycler.setVisibility(View.GONE);
//        Intent intent = new Intent(context, ActivityJigsaw.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }
}
