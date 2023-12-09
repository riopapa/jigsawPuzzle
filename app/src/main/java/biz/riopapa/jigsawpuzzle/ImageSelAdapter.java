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
import static biz.riopapa.jigsawpuzzle.ActivityMain.mContext;
import static biz.riopapa.jigsawpuzzle.ActivityMain.screenX;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Timer;
import java.util.TimerTask;

import biz.riopapa.jigsawpuzzle.func.ImageStorage;
import biz.riopapa.jigsawpuzzle.model.History;

public class ImageSelAdapter extends RecyclerView.Adapter<ImageSelAdapter.ViewHolder> {

    static Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iVImage, iVStatus;
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
            iVStatus = itemView.findViewById(R.id.status);
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
        RelativeLayout.LayoutParams parImage = (RelativeLayout.LayoutParams) holder.iVImage.getLayoutParams();
        int width = screenX * 3 / 7;
        int height = width*bitmap.getHeight()/bitmap.getWidth();
        if (width < height) {
            width = width * 8/10;
            height = height * 8/10;
        }
        if (fPhoneInchX > 3f && width > height)   // tablet height should be shortened
            height = height * 8 / 10;
        parImage.width = width; parImage.height = height;
        holder.iVImage.setLayoutParams(parImage);
        holder.iVImage.setImageBitmap(bitmap);

        RelativeLayout.LayoutParams parStatus = (RelativeLayout.LayoutParams) holder.iVStatus.getLayoutParams();
        parStatus.width = width; parStatus.height = height;
        holder.iVStatus.setLayoutParams(parStatus);

        String game = new ImageStorage().getStr(position).substring(0,3);
        holder.itemView.setTag(game);
        for (int i = 0; i < histories.size(); i++) {
            History hist = histories.get(i);
            if (hist.game.equals(game)) {
                int histLocked = 0;
                for (int j = 0; j < 4; j++)
                    histLocked += hist.locked[j];
                if (histLocked > 0) {   // if any pieces are locked then show status
                    showHistoryStatus(holder, hist, width, height);
                    break;
                }
            }
        }
    }

    private static void showHistoryStatus(@NonNull ViewHolder holder, History hist,
                                          int width, int height) {

//        Paint boxPaint = new Paint();
//        boxPaint.setColor(Color.BLACK);
//        boxPaint.setStrokeWidth(4);
//        Paint [] paint = new Paint[4];
//        for (int i = 0; i < 4; i++) {
//            paint[i] = new Paint();
//            paint[i].setColor(boxColors[i]);
//        }
        Bitmap statusMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(statusMap);
        if (hist == null)
            hist = new History();
        for (int i = 0; i < 4; i++) {
            if (hist.locked[i] > 0) {
                drawStatusCircle(canvas, width, height, i, hist.percent[i]);

//                canvas.drawLine(w4 * i + d, d, w4 * i + w4 - d, d, boxPaint);   // top
//                canvas.drawLine(w4 * i + d , h, w4 * i + w4 - d, h, boxPaint);   // bottom
//                canvas.drawLine(w4 * i + d, d, w4 * i, h-d, boxPaint);    // left
//                canvas.drawLine(w4 * i + w4 - d, d, w4 * i + w4-d, h-d, boxPaint);    // right
//                canvas.drawRect(w4 * i + d, h - (float) (h * hist.percent[i]) / 100,
//                        w4 * i + w4-d, h-d, paint[i]);
            }
        }
        holder.iVStatus.setImageBitmap(statusMap);
    }

    static void drawStatusCircle(Canvas canvas, int width, int height, int i, int pct) {
        Paint paint = new Paint();
        RectF rect = new RectF();
        int [] boxColors = { 0x7f00FF00, 0x7f3AAFC0, 0x7fCC44FF, 0x7FFF3333};
        float offX = (i == 0 || i == 2) ? 0: width/2f;
        float offY = (i == 0 || i == 1) ? 0: height/2f;
        float d = 30;
        rect.set(offX + d, offY + d, offX + width/2f - d, offY + height/2f - d);
        paint.setColor(boxColors[i]);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        canvas.drawArc(rect, 90f- (pct*180f/100f), pct*360f/100f , false, paint);
        canvas.drawArc(rect, -90, pct*360f/100f , true, paint);

        paint.setColor(0xFF000000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawArc(rect, -90, pct*360f/100f , true, paint);

    }


}
