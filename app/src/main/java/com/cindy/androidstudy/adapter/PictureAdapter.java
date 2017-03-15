package com.cindy.androidstudy.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cindy.androidstudy.R;
import com.cindy.androidstudy.data.Picture;
import com.cindy.androidstudy.util.BitmapUtil;
import com.cindy.androidstudy.util.UnitConvertUtil;
import com.cindy.androidstudy.widget.SquareImageView;

import java.util.ArrayList;
import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {

    private Context context;
    private int columnWidth;
    private List<Picture> items = new ArrayList<>();

    public PictureAdapter(Context context, int columnCount) {
        this.context = context;

        columnWidth = UnitConvertUtil.getScreenWidth(context) / columnCount;
    }

    public void addItem(Picture picture) {
        items.add(picture);
    }

    @Override
    public PictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_picture_thumbnail, null);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        holder.setPicture(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private Bitmap getPhotoBitmap(String imageFilePath) {

        //원본 이미지의 크기를 구해 scaleFactor를 계산
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFilePath, options);

        int size = columnWidth;

        int scaleFactor = BitmapUtil.calculateInSampleSize(options.outWidth, options.outHeight, size, size);

        //구해진 scaleFactor로 비트맵을 생성
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, options);

        Matrix matrix = BitmapUtil.getBitmapMatrix(imageFilePath);
        Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return rotateBitmap;
        
        /*
        //보여줄 사이즈, 뱡향 조정
        int resize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int x = bitmap.getWidth() > resize ? (bitmap.getWidth() - resize) / 2 : 0;
        int y = bitmap.getHeight() > resize ? (bitmap.getHeight() - resize) / 2 : 0;
        int width = Math.min(resize, bitmap.getWidth());
        int height = Math.min(resize, bitmap.getHeight());

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height, matrix, true);
        */
    }

    //======== View Holder

    public class PictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Picture mData;
        public SquareImageView mImageView;

        public PictureViewHolder(View v) {
            super(v);
            mImageView = (SquareImageView) v;
            mImageView.setOnClickListener(this);
        }

        public void setPicture(Picture picture) {
            mData = picture;
            mImageView.setImageBitmap(getPhotoBitmap(picture.getThumbData()));
        }

        @Override
        public void onClick(View v) {
            Log.d(">>>>>>>", mData.getThumbData());
        }
    }
}
