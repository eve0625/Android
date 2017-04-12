package com.cindy.androidstudy.file;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cindy.androidstudy.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileActivity extends AppCompatActivity {

    private static final String TAG = "FileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        Log.d(TAG, "getFilesDir : " + getFilesDir().toString());
        Log.d(TAG, "getCacheDir : " + getCacheDir().toString());

        //createFileByStream();

        //createTempFile();

        Log.d(TAG, "ExternalStorageWritable : " + isExternalStorageWritable());
        Log.d(TAG, "ExternalStorageReadable : " + isExternalStorageReadable());

        getPublicAlbumStorageDir("myAlbum");
        getPrivateAlbumStorageDir("myAlbum");
    }

    private void createFileByConstructor() {

        File file = new File(getFilesDir(), "test.txt"); //parent directory, child name
    }

    private void createFileByStream() {

        String filename = "test.txt";
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {

            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTempFile() {
        File file;
        try {
            file = File.createTempFile("cache_text", ".txt", getCacheDir()); //prefix, suffix, directory
            Log.d(TAG, "tempFile : " + file.toString());
            Log.d(TAG, "tempFile(Name): " + file.getName());
            Log.d(TAG, "tempFile(Parent): " + file.getParent());
            Log.d(TAG, "tempFile(Path): " + file.getPath());
            Log.d(TAG, "tempFile(AbsolutePath) : " + file.getAbsolutePath());
            Log.d(TAG, "tempFile(getCanonicalPath) : " + file.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) { //상태가 MEDIA_MOUNTED라면 쓰기가능
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        Log.d(TAG, "ExternalPublicDirectory(PICTURE) : " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
        if (file != null) Log.d(TAG, "public directory : " + file.getAbsolutePath());
        return file;
    }

    public File getPrivateAlbumStorageDir(String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        Log.d(TAG, "ExternalPrivateDirectory(PICTURE) : " + getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
        if (file != null) Log.d(TAG, "private directory : " + file.getAbsolutePath());
        return file;
    }
}
