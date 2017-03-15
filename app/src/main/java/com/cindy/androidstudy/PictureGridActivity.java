package com.cindy.androidstudy;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;

import com.cindy.androidstudy.adapter.PictureAdapter;
import com.cindy.androidstudy.data.Picture;

public class PictureGridActivity extends AppCompatActivity {

    private static final String LOG_TAG = "PictureGridActivity";

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private PictureAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_grid);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PictureAdapter(this);
        getPictureData(mAdapter); //사진 목록 조회
        mRecyclerView.setAdapter(mAdapter);

    }

    private void getPictureData(PictureAdapter adapter) {

        String[] columns = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DISPLAY_NAME
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
            String id, orientation, data, displayName;
            long dateTaken, size;

            int colIndexId = cursor.getColumnIndex(columns[0]);
            int colIndexDateTaken = cursor.getColumnIndex(columns[1]);
            int colIndexOrientation = cursor.getColumnIndex(columns[2]);
            int colIndexData = cursor.getColumnIndex(columns[3]);
            int colIndexSize = cursor.getColumnIndex(columns[4]);
            int colIndexDisplayName = cursor.getColumnIndex(columns[5]);

            do {
                id = cursor.getString(colIndexId);
                dateTaken = cursor.getLong(colIndexDateTaken);
                orientation = cursor.getString(colIndexOrientation);
                data = cursor.getString(colIndexData);
                size = cursor.getLong(colIndexSize);
                displayName = cursor.getString(colIndexDisplayName);

                adapter.addItem(new Picture(id, data));

                Log.d(LOG_TAG, String.format("(%s) %s (date:%d, orientation:%s, data:%s, size:%d)",
                        id, displayName, dateTaken, orientation, data, size));

            } while (cursor.moveToNext());
        }
        cursor.close();

    }


}
