package com.cindy.androidstudy.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cindy.androidstudy.util.BitmapUtil;

public class SquareImageView extends AppCompatImageView {

    private String imageFilePath;
    private int size = -1;

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        size = View.MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size);

        //원본 이미지의 크기를 구해 scaleFactor를 계산
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFilePath, options);

        int scaleFactor = BitmapUtil.calculateInSampleSize(options.outWidth, options.outHeight, size, size);


        //구해진 scaleFactor로 비트맵을 생성
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, options);

        //setImageBitmap(bitmap);


        Matrix matrix = BitmapUtil.getBitmapRatate(imageFilePath);
        if (matrix == null) {
            setImageBitmap(bitmap);
        } else {
            Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            setImageBitmap(rotateBitmap);
        }


        /* => 속도가 너무 느려!!
        //보여줄 사이즈, 뱡향 조정
        int resize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int x = bitmap.getWidth() > resize ? (bitmap.getWidth() - resize) / 2 : 0;
        int y = bitmap.getHeight() > resize ? (bitmap.getHeight() - resize) / 2 : 0;
        int width = Math.min(resize, bitmap.getWidth());
        int height = Math.min(resize, bitmap.getHeight());


        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height, matrix, true);

        setImageBitmap(resizedBitmap);
        */
    }

    public void setImagePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }




}
