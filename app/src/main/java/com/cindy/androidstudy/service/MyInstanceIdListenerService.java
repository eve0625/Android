package com.cindy.androidstudy.service;

import com.cindy.androidstudy.utils.SharedPrefsUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyInstanceIdListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        //토큰은 아래와 같은 상황에서 변경됨
        /*
        - 앱에서 인스턴스 ID 삭제
        - 새 기기에서 앱 복원
        - 사용자가 앱 삭제/재설치
        - 사용자가 앱 데이터 소거
         */
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //push를 보낼 서버에 토큰을 저장해 놓아야 함
        SharedPrefsUtils.setFcmToken(getApplicationContext(), refreshedToken);
    }
}
