package com.cindy.androidstudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {

        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.btn_pick_photo :
                intent.setClass(this, ImagePickActivity.class);
                break;
        }

        startActivity(intent);
    }
}
