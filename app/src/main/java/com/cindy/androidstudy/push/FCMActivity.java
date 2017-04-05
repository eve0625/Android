package com.cindy.androidstudy.push;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cindy.androidstudy.R;
import com.google.firebase.iid.FirebaseInstanceId;

public class FCMActivity extends AppCompatActivity {

    TextView tvFcmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);

        //현재 토큰 검색
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("fcmToken", fcmToken);

        tvFcmId = (TextView) findViewById(R.id.tv_fcm_id);
        tvFcmId.setText(fcmToken);
    }
}
