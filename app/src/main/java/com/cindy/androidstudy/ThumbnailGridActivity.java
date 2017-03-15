package com.cindy.androidstudy;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.cindy.androidstudy.adapter.ImageLoader;
import com.cindy.androidstudy.adapter.PictureAdapter;
import com.cindy.androidstudy.adapter.ThumbnailAdapter;
import com.cindy.androidstudy.data.Picture;

public class ThumbnailGridActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ThumbnailGridActivity";

    private static final int GRID_COL_COUNT = 3;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private ThumbnailAdapter mAdapter;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_grid);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, GRID_COL_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ThumbnailAdapter(this, mLayoutManager.getSpanCount());
        getImageData(mAdapter); //이미지 목록 조회

        mRecyclerView.setAdapter(mAdapter);

    }

    private void getImageData(ThumbnailAdapter adapter) {

        String[] columns = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.ORIENTATION,
        };

        Cursor cursor = new CursorLoader(
                getApplicationContext(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns, //columns
                null, //rows
                null, //where
                MediaStore.Images.Media.DATE_TAKEN + " DESC" //order
        ).loadInBackground();

        Log.d(LOG_TAG, "Picture Count : " + cursor.getCount());

        if (cursor.moveToFirst()) {
            String data, displayName;
            int id, orientation;

            int colIndexId = cursor.getColumnIndex(columns[0]);
            int colIndexData = cursor.getColumnIndex(columns[1]);
            int colIndexDisplayName = cursor.getColumnIndex(columns[2]);
            int colIndexOrientation = cursor.getColumnIndex(columns[3]);

            do {
                id = cursor.getInt(colIndexId);
                data = cursor.getString(colIndexData);
                displayName = cursor.getString(colIndexDisplayName);
                orientation = cursor.getInt(colIndexOrientation);

                adapter.addItem(new Picture(id, data, orientation));

                Log.d(LOG_TAG, String.format("(%s) %s (orientation:%d, data:%s)",
                        id, displayName, orientation, data));

            } while (cursor.moveToNext());
        }
        cursor.close();

    }


}
