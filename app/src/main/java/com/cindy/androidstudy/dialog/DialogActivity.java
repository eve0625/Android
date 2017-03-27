package com.cindy.androidstudy.dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.cindy.androidstudy.R;

/**
 * back key를 직접 제어하는 방법이 없다.
 * setCancelable(true) 인 경우는 back key 클릭시 dialog가 닫히고, false인 경우는 dialog가 닫히지 않는데
 * 둘다 onBackPress() 콜백이 호출되지 않아, 직접 제어할 수가 없다.
 */
public class DialogActivity extends AppCompatActivity
        implements View.OnClickListener, DialogInterface.OnClickListener {

    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;
    private MyAsyncTask mMyAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dialog);

        //상단 status bar의 배경색 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        Log.e("onCreate", "DialogActivity");

        Button btnSpinProgressDialog = (Button) findViewById(R.id.btn_spin_progress_dialog);
        btnSpinProgressDialog.setOnClickListener(this);

        Button btnBarProgressDialog = (Button) findViewById(R.id.btn_bar_progress_dialog);
        btnBarProgressDialog.setOnClickListener(this);

        Button btnAlertDialog = (Button) findViewById(R.id.btn_alert_dialog);
        btnAlertDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_spin_progress_dialog : {
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setTitle("알림");
                    mProgressDialog.setMessage("진행중입니다!!!");
                    mProgressDialog.setCanceledOnTouchOutside(false); //바깥영역 터치시 취소되지 않도록 설정 (default는 true)
                    mProgressDialog.setCancelable(false); //back키로 취소 불가하도록 설정 (default는 true)
                    mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "확인", this);
                }
                mProgressDialog.show();
                break;
            }
            case R.id.btn_bar_progress_dialog : {
                mMyAsyncTask = new MyAsyncTask();
                mMyAsyncTask.execute();
                break;
            }
            case R.id.btn_alert_dialog : {
                if (mAlertDialog == null) {
                    //mAlertDialog = new AlertDialog(this);
                }
                break;
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //Dialog는 알아서 종료되는군!
        if (which == DialogInterface.BUTTON_POSITIVE) {
            Toast.makeText(this, "확인", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() { //작업 시작전 UI 작업

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(DialogActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(true);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mMyAsyncTask.cancel(true);
                    }
                });
                progressDialog.setTitle("알림");
                progressDialog.setMessage("진행중입니다!!!");
            }
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 5; i++) {
                    progressDialog.setProgress(i * 30);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) { //작업 종료 후 UI 작업
            progressDialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled() {
            Log.e("AsyncTask", "onCancelled");
            super.onCancelled();
        }
    }
}
