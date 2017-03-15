package com.cindy.androidstudy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cindy.androidstudy.R;
import com.cindy.androidstudy.data.Picture;
import com.cindy.androidstudy.widget.SquareImageView;

import java.util.ArrayList;
import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> {

    private Context context;
    private List<Picture> items = new ArrayList<>();

    public PictureAdapter(Context context) {
        this.context = context;
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
        holder.bindPicture(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    //======== View Holder

    public class PictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Picture mData;
        private SquareImageView mImageView;

        public PictureViewHolder(View v) {
            super(v);
            mImageView = (SquareImageView) v;
            mImageView.setOnClickListener(this);
        }

        public void bindPicture(Picture picture) {
            mData = picture;

            mImageView.setImagePath(mData.getThumbData());
            //mImageView.setImageBitmap(getThumbnail(mData.getThumbData(), 200, 200));

        }

        private Bitmap getThumbnail(String imgFilePath, int w, int h) {
            //imgFilePath의 이미지를 비트맵으로 가져오기
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8; //실제 사이즈의 1/8로 decode
            Bitmap bitmap = BitmapFactory.decodeFile(imgFilePath, options);

            //보여줄 사이즈로 조정
           return Bitmap.createScaledBitmap(bitmap, w, h, true);
        }

        @Override
        public void onClick(View v) {
            Log.d(">>>>>>>", mData.getThumbData());
        }
    }
}
