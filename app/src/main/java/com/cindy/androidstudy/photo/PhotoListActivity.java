package com.cindy.androidstudy.photo;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cindy.androidstudy.BaseActivity;
import com.cindy.androidstudy.R;

import java.io.File;

public class PhotoListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> , GridView.OnItemClickListener, View.OnClickListener {

    private static final int GALLERY_COL_NUMS = 3;
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_DATA_INDEX = 1;

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_CROP_SELECT_PHOTO = 1;

    private static Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    private GridView mGridView;
    private PhotoCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setNumColumns(GALLERY_COL_NUMS);
        mGridView.setDrawSelectorOnTop(true);
        mGridView.setOnItemClickListener(this);

        mCursorAdapter = new PhotoCursorAdapter(this, null);
        mGridView.setAdapter(mCursorAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("권한 설정")
                            .setMessage("저장공간 읽기 권한이 필요합니다.")
                            .setCancelable(false)
                            .setPositiveButton("설정하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Intent setting = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    setting.setData(Uri.fromParts("package", getPackageName(), null));
                                    startActivity(setting);
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            finish();
                        }

                    }).show();

                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                }
                return;
            }
        }

        getContentResolver().registerContentObserver(externalContentUri, true, contentObserver);

        getPhotoList();
    }

    @Override
    protected void onStop() {
        super.onStop();

        getContentResolver().unregisterContentObserver(contentObserver);
    }

    private final int imageLoaderId = 1;
    private void getPhotoList() {
        Log.d(TAG_LOG, "getPhotoList");

        if (getSupportLoaderManager().getLoader(imageLoaderId) != null) {
            getSupportLoaderManager().restartLoader(imageLoaderId, null, this);
        } else {
            //로더 초기화 (새로 생성하거나, 해당 ID의 로더가 이미 존재하는 경우 마지막 로더 재사용)
            getSupportLoaderManager().initLoader(imageLoaderId, null, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_READ_EXTERNAL_STORAGE : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPhotoList();
                } else {
                    finish();
                }
                break;
            }
            default :
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG_LOG, "onCreateLoader");

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.MIME_TYPE
        };
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        return new CursorLoader(this, externalContentUri, projection, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG_LOG, "onLoadFinished");
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG_LOG, "onLoaderReset");
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Cursor cursor = (Cursor) mCursorAdapter.getItem(position);
        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(PROJECTION_ID_INDEX));

        //ImageCropper의 view를 사용한 custom activity로 이미지 자르기
        Intent intent = new Intent(this, ImageCropperActivity.class);
        intent.setData(imageUri);
        startActivityForResult(intent, REQUEST_CROP_SELECT_PHOTO);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close : {
                finish();
                break;
            }
            case R.id.btn_camera : {
                Toast.makeText(this, "촬영하기", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CROP_SELECT_PHOTO : {
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
            default :
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            Log.d(TAG_LOG, "ContentObserver.onChange");

            getPhotoList();
            super.onChange(selfChange);
        }
    };

    class PhotoCursorAdapter extends CursorAdapter {

        public PhotoCursorAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            ImageView imgView = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_photo_view, null);
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imgView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView imgView = (ImageView) view;

            //Glide 라이브러리 사용
            String dataPath = cursor.getString(PROJECTION_DATA_INDEX);

            Uri uri = Uri.fromFile(new File(dataPath));
            Glide.with(context)
                    .load(uri)
                    .placeholder(R.drawable.ic_perm_media_black)
                    .override(200, 200)
                    .crossFade()
                    .centerCrop()
                    .into(imgView);
        }
    }

}
