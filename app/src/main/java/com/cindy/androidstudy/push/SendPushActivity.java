package com.cindy.androidstudy.push;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cindy.androidstudy.R;
import com.cindy.androidstudy.volley.FcmData;
import com.cindy.androidstudy.volley.FcmRequest;
import com.cindy.androidstudy.volley.FcmResponse;
import com.cindy.androidstudy.volley.GsonRequest;
import com.cindy.androidstudy.volley.HttpRequestQueue;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class SendPushActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etTitle;
    private EditText etMessage;
    private Button btnSendFCM;
    private TextView tvResult;

    private HttpRequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        mRequestQueue = HttpRequestQueue.getInstance(this);

        btnSendFCM = (Button) findViewById(R.id.btn_send_fcm);
        btnSendFCM.setOnClickListener(this);

        etTitle = (EditText) findViewById(R.id.et_title);
        etMessage = (EditText) findViewById(R.id.et_message);
        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_send_fcm : {
                sendSimpleRequest();
                break;
            }
        }
    }

    private void sendSimpleRequest() {

        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        String url = "https://gcm-http.googleapis.com/gcm/send";

        Log.d("fcmToken", fcmToken);

        FcmRequest requestBody = new FcmRequest();
        requestBody.setTo(fcmToken);
        FcmData data = new FcmData();
        data.setTitle(etTitle.getText().toString());
        data.setMessage(etMessage.getText().toString());
        requestBody.setData(data);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "key=AIzaSyBP5SJM-sMQxHgoou9KJ5bv81ZdNK3GeQE");

        GsonRequest<FcmResponse> request = new GsonRequest(url, FcmResponse.class, headers, requestBody,
                new Response.Listener<FcmResponse>() {
                    @Override
                    public void onResponse(FcmResponse response) {
                        tvResult.setText("success :" + response.getSuccess());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvResult.setText(error.getMessage());
                    }
                });
        mRequestQueue.add(request);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
    }
}
