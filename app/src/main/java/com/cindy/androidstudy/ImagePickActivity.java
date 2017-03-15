package com.cindy.androidstudy;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.UserDictionary;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * 캡쳐 관련 문서 : https://developer.android.com/training/camera/photobasics.html
 */
public class ImagePickActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG =  "ImagePickActivity";

    private static final int REQUEST_IMAGE_PICK = 0;
    private static final int REQUEST_IMAGE_CAPTURE = REQUEST_IMAGE_PICK + 1;

    private ParcelFileDescriptor mFileDescriptor;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_request);

        mImageView = (ImageView) findViewById(R.id.select_image);

        Button btnSendIntent = (Button) findViewById(R.id.btn_send_intent);
        btnSendIntent.setOnClickListener(this);

        Button btnCaptureImage = (Button) findViewById(R.id.btn_capture_image);
        btnCaptureImage.setOnClickListener(this);

        Button btnRetrieveImages = (Button) findViewById(R.id.btn_retrieve_file);
        btnRetrieveImages.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_PICK : {
                if (resultCode == RESULT_OK) { //이미지가 선택된 경우, 해당 이미지의 uri가 전달됨
                    Uri returnUri = data.getData();
                    String mimeType = getContentResolver().getType(returnUri);

                    Log.d(LOG_TAG, returnUri.getPath() + ":" + mimeType);

                    Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                    returnCursor.moveToFirst();

                    String displayName = returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    long size = returnCursor.getLong(returnCursor.getColumnIndex(OpenableColumns.SIZE));
                    String imagePath = returnCursor.getString(returnCursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    Log.d(LOG_TAG, String.format("DISPLAY_NAME:%s, SIZE:%d, PATH:%s", displayName, size, imagePath));

                    mImageView.setImageBitmap(getResizedBitmap(mImageView, imagePath));

                    returnCursor.close();
                }
            }
            break;
            case REQUEST_IMAGE_CAPTURE : {
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mImageView.setImageBitmap(imageBitmap);
                }
            }
            default :
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 해당 uri의 이미지를 resize한 bitmap을 반환
     * @param imageView
     * @param filePath
     * @return
     */
    private Bitmap getResizedBitmap(ImageView imageView, String filePath) {

        //out of memory 에러 방지를 위하여 1/4 사이즈로 decode 함
        //참고 : http://chiyo85.tistory.com/entry/Android-Bitmap-Object-Resizing-Tip
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap src = BitmapFactory.decodeFile(filePath, options);

        return Bitmap.createScaledBitmap(src, imageView.getWidth(), imageView.getHeight(), true);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_send_intent : {
                //ACTION_PICK을 구현한 앱 호출
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
                pickImageIntent.setType("image/jpg");
                startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK);
            }
            break;
            case R.id.btn_capture_image : {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
            break;
            case R.id.btn_retrieve_file : {
                Intent intent = new Intent(this, ThumbnailGridActivity.class);
                startActivity(intent);
            }
            break;
        }

    }

}
