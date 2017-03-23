package com.cindy.androidstudy;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FullScreenActivity extends AppCompatActivity {

    private View mDecorView;

    /**
     * 몰입모드 : 상단의 상태바와 하단의 소프트키를 모두 숨겨 전체화면을 다 사용한다
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        //actionbar가 있는 테마인 경우 숨김처리
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mDecorView = getWindow().getDecorView();

        int UIOption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            UIOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            UIOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            UIOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        mDecorView.setSystemUiVisibility( UIOption );
    }
}
