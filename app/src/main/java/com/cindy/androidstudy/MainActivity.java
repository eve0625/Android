package com.cindy.androidstudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cindy.androidstudy.dialog.DialogActivity;
import com.cindy.androidstudy.fab.FABActivity;
import com.cindy.androidstudy.fab.FABLibActivity;
import com.cindy.androidstudy.surfaceview.SurfaceViewActivity;
import com.cindy.androidstudy.push.SendPushActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickButton(View view) {

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_full_screen : { //전체화면 모드
                intent.setClass(this, FullScreenActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_surface_view : {
                intent.setClass(this, SurfaceViewActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_dialog : {
                intent.setClass(this, DialogActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_floating_action_button : {
                //intent.setClass(this, FABActivity.class);
                intent.setClass(this, FABLibActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_fcm : {
                intent.setClass(this, SendPushActivity.class);
                startActivity(intent);
            }
        }
    }

}
