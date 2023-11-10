package com.riopapa.jigsawpuzzle;

import static com.riopapa.jigsawpuzzle.ActivityMain.GAME_SELECT_LEVEL;
import static com.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static com.riopapa.jigsawpuzzle.ActivityMain.screenX;
import static com.riopapa.jigsawpuzzle.ActivityMain.vars;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
                vars.gameMode = GAME_SELECT_LEVEL;
                Intent intent = new Intent(context, ActivitySelLevel.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

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

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.iVImage.getLayoutParams();
        int width = screenX * 3 / 7;
        int height = width*oMap.getHeight()/oMap.getWidth();
        if (width < height) {
            width = width * 7/10;
            height = height * 7/10;
        }
        params.width = width; params.height = height;
        holder.iVImage.setLayoutParams(params);
        holder.iVImage.setImageBitmap(bitmap);
        holder.tVInfo.setText(" "+position);

    }
}
