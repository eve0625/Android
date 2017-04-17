package com.cindy.androidstudy.device;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.cindy.androidstudy.R;

public class KeyboardActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "KeyboardActivity";

    private EditText etID;
    private EditText etPassword;
    private Button btnShow;
    private Button btnHide;
    private Button btnToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        etID = (EditText) findViewById(R.id.id);
        etPassword = (EditText) findViewById(R.id.password);

        btnShow = (Button) findViewById(R.id.btn_show);
        btnShow.setOnClickListener(this);

        btnHide = (Button) findViewById(R.id.btn_hide);
        btnHide.setOnClickListener(this);

        btnToggle = (Button) findViewById(R.id.btn_toggle);
        btnToggle.setOnClickListener(this);

        //입력필드에 포커스를 준다고 해서 자동으로 키보드가 올라오지는 않는단 말씀
        //etPassword.requestFocus();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //바로 호출하면 안나와.. 왜지?
        //showSoftKeyboard(etPassword);

        //이건 동작하는구만..
        /*
        etPassword.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard(etPassword);
            }
        }, 100);
        */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show : {
                showSoftKeyboard(etPassword);
                break;
            }
            case R.id.btn_hide : {
                hideSoftKeyboard();
                break;
            }
            case R.id.btn_toggle : {
                toggleSoftKeyboard();
                break;
            }
        }
    }

    public void toggleSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(0, 0); //언제나 동작한다!
        //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //코드가 아니라면 SHOW_FORCED가 없으니, 이것으로도 충분함..
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            Log.e(TAG, "requestFocus:success");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
            //imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            Log.e(TAG, "find Focused View:success");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            //SHOW_IMPLICIT로 노출한 키보드만 Hide 함. 사용자가 직접 올린 건 Hide 하지 못함
            //imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY); //1

            //SHOW_FORCED로 노출된 키보드가 아니라면, 나머지 경우의 키보드는 Hide함
            //imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); //2

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //아앗! 이 녀석은 모든 것이 닫혀!
        }
    }

    /*
    1. hideSoftInputFromWindow flag에 0를 주면 어떠한 상황에서든 닫힘
    2. hideSoftInputFromWindow flag에 HIDE_NOT_ALWAYS를 주면 SHOW_FORCED를 제외하고는 닫힘
    3. hideSoftInputFromWindow flag에 HIDE_IMPLICIT_ONLY 를 주면, SHOW_IMPLICIT 로 보여진 키보드만 닫힘
     */
}
