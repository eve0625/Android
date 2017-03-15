package com.cindy.androidstudy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.PictureViewHolder> {

    private Context context;
    private int cellSize;
    private List<Picture> items = new ArrayList<>();
    private ImageLoader imageLoader;

    public ThumbnailAdapter(Context context, int columnCount) {
        this.context = context;
        this.imageLoader = new ImageLoader(context.getContentResolver(), this);
        this.cellSize = UnitConvertUtil.getScreenWidth(context) / columnCount;
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
        holder.setPicture(items.get(position), cellSize);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //======== View Holder

    public class PictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Picture mData;
        public Bitmap bitmap;
        public SquareImageView mImageView;

        public PictureViewHolder(View v) {
            super(v);
            mImageView = (SquareImageView) v;
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setOnClickListener(this);
        }

        public void setPicture(Picture picture, int size) {
            mData = picture;
            mImageView.setImageBitmap(imageLoader.getImage(picture.getThumbID(), picture.getThumbData(), picture.getOrientation(), size));
        }

        @Override
        public void onClick(View v) {
            Log.d(">>>>>>>", mData.getThumbData());
        }
    }
}
