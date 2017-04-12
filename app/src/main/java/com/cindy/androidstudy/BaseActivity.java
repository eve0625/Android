package com.cindy.androidstudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected final String TAG_LOG = getClass().getSimpleName();

    public static final int RESULT_EXCEPTION = RESULT_FIRST_USER + 1;

    /**
     * 현재 액티비티를 종료하고 메인 액티비티를 띄운다.
     */
    protected void goMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); //finish랑 같은 효과?
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택에 같은 액티비티가 존재하는 경우, 해당 액티비티 위의 모든 액티비티를 종료
        startActivity(intent);
        finish();

    }

}
