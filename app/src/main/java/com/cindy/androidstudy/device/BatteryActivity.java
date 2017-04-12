package com.cindy.androidstudy.device;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cindy.androidstudy.R;

public class BatteryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnBatteryStatus;
    private TextView tvBatteryStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);

        btnBatteryStatus = (Button) findViewById(R.id.btn_battery_status);
        btnBatteryStatus.setOnClickListener(this);

        tvBatteryStatus = (TextView) findViewById(R.id.tv_battery_status);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_battery_status : {
                getBatteryStatue();
                break;
            }
        }
    }

    private void getBatteryStatue() {

        //접착식 인텐트이므로 BroadcastReceiver를 등록할 필요가 없음
        //registerReceiver를 호출하여 null을 수신기로 전달하면 현재 배터리 상태 인텐트가 반환된다.
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int rate = level * 100 / scale;

        tvBatteryStatus.setText(rate + "%"); //배터리 잔여량 퍼센트

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        if (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL) {
            tvBatteryStatus.append("\ncharging...");
        } else {
            tvBatteryStatus.append("\nnot charging...");
        }

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
            tvBatteryStatus.append(" by USB");
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
            tvBatteryStatus.append(" by AC");
        }
        
    }
}
