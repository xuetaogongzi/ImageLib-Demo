package com.wangjw.imagelibdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjw on 16/11/5.
 */

public class PhotoAcapter extends RecyclerView.Adapter<PhotoAcapter.PhotoViewHolder> {

    private Context mContext;
    private List<String> mImgFileList = new ArrayList<String>();

    public PhotoAcapter(Context context) {
        mContext = context;
    }

    public void addData(File file) {
        if (mImgFileList.contains(file.getAbsolutePath())) {
            Toast.makeText(mContext, "重复照片", Toast.LENGTH_SHORT).show();
            return;
        }

        mImgFileList.add(file.getAbsolutePath());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mImgFileList.size();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.grit_item_photo, null);
        PhotoViewHolder holder = new PhotoViewHolder(itemView);

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int width = dm.widthPixels; // 屏幕宽度（像素）
        float density = dm.density; // 屏幕密度（0.75 / 1.0 / 1.5）

        int w = (int) ((width - density * 32 - density * 16) / 3);
        ViewGroup.LayoutParams params = holder.mImgPhoto.getLayoutParams();
        params.width = w;
        params.height = w;
        holder.mImgPhoto.setLayoutParams(params);

        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(mImgFileList.get(position));
        Log.d("MainActivity", "width = " + bitmap.getWidth() + ", height = " + bitmap.getHeight());
        holder.mImgPhoto.setImageBitmap(bitmap);
    }

    public List<String> getImgFileList() {
        return mImgFileList;
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImgPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            mImgPhoto = (ImageView) itemView.findViewById(R.id.ImageView_Photo);
        }

    }
}
