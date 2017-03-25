package com.cindy.androidstudy.dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cindy.androidstudy.R;

/**
 * back key를 직접 제어하는 방법이 없다.
 * setCancelable(true) 인 경우는 back key 클릭시 dialog가 닫히고, false인 경우는 dialog가 닫히지 않는데
 * 둘다 onBackPress() 콜백이 호출되지 않아, 직접 제어할 수가 없다.
 */
public class ProgressDialogActivity extends AppCompatActivity
        implements View.OnClickListener, DialogInterface.OnClickListener {

    private ProgressDialog mDialog;
    private MyAsyncTask mMyAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_dialog);

        Log.e("onCreate", "ProgressDialogActivity");

        Button btnSpinProgressDialog = (Button) findViewById(R.id.btn_spin_progress_dialog);
        btnSpinProgressDialog.setOnClickListener(this);

        Button btnBarProgressDialog = (Button) findViewById(R.id.btn_bar_progress_dialog);
        btnBarProgressDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_spin_progress_dialog : {
                if (mDialog == null) {
                    mDialog = new ProgressDialog(this);
                    mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mDialog.setTitle("알림");
                    mDialog.setMessage("진행중입니다!!!");
                    mDialog.setCanceledOnTouchOutside(false); //바깥영역 터치시 취소되지 않도록 설정 (default는 true)
                    mDialog.setCancelable(false); //back키로 취소 불가하도록 설정 (default는 true)
                    mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "확인", this);
                }
                mDialog.show();
                break;
            }
            case R.id.btn_bar_progress_dialog : {
                mMyAsyncTask = new MyAsyncTask();
                mMyAsyncTask.execute();
                break;
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //Dialog는 알아서 종료되는군!
        if (which == DialogInterface.BUTTON_POSITIVE) {
            Toast.makeText(ProgressDialogActivity.this, "확인", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed", "sdfsdfasf");
        super.onBackPressed();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() { //작업 시작전 UI 작업

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ProgressDialogActivity.this);
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
