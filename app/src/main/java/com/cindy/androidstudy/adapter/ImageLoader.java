package com.cindy.androidstudy.adapter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.BaseAdapter;

import java.util.Hashtable;
import java.util.Stack;

/**
 * http://egloos.zum.com/dhjin/v/2617663 참고
 */
public class ImageLoader {

    Hashtable<Integer, Bitmap> loadImages;
    Hashtable<Integer, String> positionRequested;
    RecyclerView.Adapter listener;
    int runningCount = 0;
    Stack<ItemPair> queue;
    ContentResolver resolver;

    public ImageLoader(ContentResolver r, RecyclerView.Adapter adapter) {
        loadImages = new Hashtable<Integer, Bitmap>();
        positionRequested = new Hashtable<Integer, String>();
        queue = new Stack<ItemPair>();
        resolver = r;
        listener = adapter;
        reset();
    }

    public void reset() {
        positionRequested.clear();
        runningCount = 0;
        queue.clear();
    }

    public Bitmap getImage(int uid, String path, int orientation, int size) {

        Bitmap image = loadImages.get(uid);
        if(image != null)
            return image;
        if (!positionRequested.containsKey(uid)) {
            positionRequested.put(uid, path);
            if (runningCount >= 15) {
                queue.push(new ItemPair(uid, path, orientation, size));
            } else {
                runningCount++;
                new LoadImageAsyncTask().execute(uid, path, orientation, size);
            }
        }
        return null;
    }

    public void getNextImage() {

        if (!queue.isEmpty()) {
            ItemPair item = queue.pop();
            new LoadImageAsyncTask().execute(item.uid, item.path, item.orientation, item.size);
        }
    }

    public class LoadImageAsyncTask extends AsyncTask<Object, Void, Bitmap> {

        Integer uid;

        @Override
        protected Bitmap doInBackground(Object... params) {

            this.uid = (Integer) params[0];
            String path = (String) params[1];
            int orientation = (Integer) params[2];
            int size = (Integer) params[3];
            String[] proj = { MediaStore.Images.Thumbnails.DATA };

            //micro_kind 썸네일은 크기가 너무 작군!!
            //Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(resolver, uid, MediaStore.Images.Thumbnails.MICRO_KIND, null);
            //if (bitmap == null) { }

            Cursor mini = MediaStore.Images.Thumbnails.queryMiniThumbnail(resolver, uid, MediaStore.Images.Thumbnails.MINI_KIND, proj);
            if (mini != null && mini.moveToFirst()) {
                path = mini.getString(mini.getColumnIndex(proj[0]));
            }
            mini.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = 1;
            if (options.outWidth > size) {
                int ws = options.outWidth / size + 1;
                if (ws > options.inSampleSize)
                    options.inSampleSize = ws;
            }
            if (options.outHeight > size) {
                int hs = options.outHeight / size + 1;
                if (hs > options.inSampleSize)
                    options.inSampleSize = hs;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);

            if (orientation > 0) {
                Matrix matrix = new Matrix();
                matrix.setRotate(orientation);
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            runningCount--;
            if (result != null) {
                loadImages.put(uid, result);
                listener.notifyDataSetChanged();
                getNextImage();
            }
        }
    }

    public static class ItemPair {

        Integer uid;
        String path;
        int orientation;
        int size;

        public ItemPair(Integer uid, String path, int orientation, int size) {
            this.uid = uid;
            this.path = path;
            this.orientation = orientation;
            this.size = size;
        }
    }

}
