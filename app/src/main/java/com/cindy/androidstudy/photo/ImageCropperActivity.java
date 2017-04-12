package com.cindy.androidstudy.photo;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.cindy.androidstudy.BaseActivity;
import com.cindy.androidstudy.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Android Image Cropper 라이브러리 사용
 * https://github.com/ArthurHub/Android-Image-Cropper
 */
public class ImageCropperActivity extends BaseActivity implements CropImageView.OnCropImageCompleteListener, View.OnClickListener {

    public static final String EXTRA_RESULT_FILE_PATH = "EXTRA_RESULT_FILE_PATH";
    public static final String EXTRA_ERROR_MESSAGE = "EXTRA_ERROR_MESSAGE";

    private CropImageView cropImageView;

    private ImageButton btnClose;
    private Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropper);

        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        btnComplete = (Button) findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(this);

        /**
         * CropImageView를 사용하여 기능 구현
         */
        //crop할 이미지 uri
        Uri imageUri = getIntent().getData();

        cropImageView = (CropImageView) findViewById(R.id.img_crop);
        cropImageView.setAspectRatio(1, 1);
        cropImageView.setCropShape(CropImageView.CropShape.OVAL);
        cropImageView.setGuidelines(CropImageView.Guidelines.ON_TOUCH);
        cropImageView.setScaleType(CropImageView.ScaleType.FIT_CENTER);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setAutoZoomEnabled(false);

        cropImageView.setImageUriAsync(imageUri);

    }

    @Override
    protected void onStart() {
        super.onStart();
        cropImageView.setOnCropImageCompleteListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cropImageView.setOnCropImageCompleteListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close : {
                finish();
                break;
            }
            case R.id.btn_complete : {
                Uri resultUri = getOutputUri();
                cropImageView.saveCroppedImageAsync(resultUri);
                break;
            }
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        if (result.isSuccessful()) {

            //TODO ImageQuality가 결정되면 상수로 정리해야지
            Uri resultUri = result.getUri();
            Bitmap orgInputBitmap, resizeInputBitmap;
            try {
                orgInputBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                resizeInputBitmap = Bitmap.createScaledBitmap(orgInputBitmap, 200, 200, true);
            } catch (IOException e) {
                Log.e(TAG_LOG, e.getMessage());
                Intent intent = new Intent().putExtra(EXTRA_ERROR_MESSAGE, e.getMessage());
                setResult(RESULT_EXCEPTION, intent);

                finish();
                return;
            }

            final Bitmap outputBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(outputBitmap);
            final int color = Color.GRAY;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, 200, 200);
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);


            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(resizeInputBitmap, rect, rect, paint);

            //TODO 프로파일 이미지가 등록이 완료될 때 까지는 임시이므로. cache 폴더에 static한 이름으로 만들어야 할 듯.. 등록이 완료되는 시점에 내부 스토리지로 옮기고 패스를 저장??
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("profile", Context.MODE_PRIVATE);
            File resultFile = new File(directory, String.format("profile_%d.png", System.currentTimeMillis()));

            FileOutputStream fileOutputStream;
            try {

                fileOutputStream = new FileOutputStream(resultFile);
                outputBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();

                Intent intent = new Intent().putExtra(EXTRA_RESULT_FILE_PATH, resultFile.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();

            } catch (IOException e) {

                Log.e(TAG_LOG, e.getMessage());
                Intent intent = new Intent().putExtra(EXTRA_ERROR_MESSAGE, e.getMessage());
                setResult(RESULT_EXCEPTION, intent);

                finish();

            } finally {
                orgInputBitmap.recycle();
                resizeInputBitmap.recycle();
                outputBitmap.recycle();
            }

        } else {
            Log.e(TAG_LOG, result.getError().getMessage());
            Intent intent = new Intent().putExtra(EXTRA_ERROR_MESSAGE, result.getError().getMessage());
            setResult(RESULT_EXCEPTION, intent);

            finish();
        }
    }

    private Uri getOutputUri() {
        Uri outputUri = null;
        try {
            //cache 디렉토리에 저장하는 경우 저장공간이 부족해지면 경고없이 시스템에 삭제할 수 있음
            outputUri = Uri.fromFile(File.createTempFile("cropped", ".jpg", getCacheDir()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp file for output image", e);
        }
        return outputUri;
    }
}
