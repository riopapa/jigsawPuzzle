package biz.riopapa.jigsawpuzzle;

import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageHeight;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageMap;
import static biz.riopapa.jigsawpuzzle.ActivityJigsaw.chosenImageWidth;
import static biz.riopapa.jigsawpuzzle.ActivityMain.GAME_SELECT_LEVEL;
import static biz.riopapa.jigsawpuzzle.ActivityMain.chosenNumber;
import static biz.riopapa.jigsawpuzzle.ActivityMain.currGame;
import static biz.riopapa.jigsawpuzzle.ActivityMain.fPhoneInchX;
import static biz.riopapa.jigsawpuzzle.ActivityMain.gameMode;
import static biz.riopapa.jigsawpuzzle.ActivityMain.histories;
import static biz.riopapa.jigsawpuzzle.ActivityMain.levelNames;
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import biz.riopapa.jigsawpuzzle.func.ImageStorage;
import biz.riopapa.jigsawpuzzle.model.History;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ImageSelAdapter extends RecyclerView.Adapter<ImageSelAdapter.ViewHolder> {

    static Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iVImage;
        TextView tVInfo;

        ViewHolder(final View itemView) {
            super(itemView);

            iVImage = itemView.findViewById(R.id.image);
            iVImage.setOnClickListener(view -> {

                chosenNumber = getBindingAdapterPosition();
                gameMode = GAME_SELECT_LEVEL;
                chosenImageMap = new ImageStorage().getMap(chosenNumber);
                chosenImageWidth = chosenImageMap.getWidth();
                chosenImageHeight = chosenImageMap.getHeight();

                currGame = itemView.getTag().toString();
                ImageView imageView = iVImage.getRootView().findViewById(R.id.chosen_image);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(chosenImageMap);
                RecyclerView imageRecycler = iVImage.getRootView().findViewById(R.id.imageRecycler);
                imageRecycler.setVisibility(View.GONE);

                new Timer().schedule(new TimerTask() {
                    public void run() {
                    Intent intent = new Intent(context, ActivitySelLevel.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    }
                }, 300);


            });
            tVInfo = itemView.findViewById(R.id.info);
        }

    }

    @Override
    public int getItemCount() {
        context = mContext;
        return new ImageStorage().count();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Bitmap oMap = new ImageStorage().getMap(position);

        Bitmap bitmap = Bitmap.createScaledBitmap(oMap, oMap.getWidth()/2, oMap.getHeight()/2, true);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.iVImage.getLayoutParams();
        int width = screenX * 3 / 7;
        int height = width*bitmap.getHeight()/bitmap.getWidth();
        if (width < height) {
            width = width * 8/10;
            height = height * 8/10;
        }
        if (fPhoneInchX > 3f && width > height)   // tablet height should be shortened
            height = height * 8 / 10;
        params.width = width; params.height = height;
        holder.iVImage.setLayoutParams(params);
        holder.iVImage.setImageBitmap(bitmap);
        String game = new ImageStorage().getStr(position).substring(0,3);
        String histStr = game;
        final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd", Locale.getDefault());
        for (int i = 0; i < histories.size(); i++) {
            History hist = histories.get(i);
            if (hist.game.equals(game)) {
                for (int j = 0; j < 4; j++) {
                    if (hist.time[j] != 0 && hist.locked[j] != 0) {
                        histStr += "\n"+levelNames[j] + " : " + sdf.format(hist.time[j])
                                + " " + ((hist.percent[j] > 99) ? "Done" : hist.percent[j] + "%");
                    }
                }
                break;
            }
        }

        holder.itemView.setTag(game);
        holder.tVInfo.setText(histStr);

    }
}
