package com.cindy.androidstudy.service;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cindy.androidstudy.MainActivity;
import com.cindy.androidstudy.MyApplication;
import com.cindy.androidstudy.R;
import com.cindy.androidstudy.fab.FABLibActivity;
import com.cindy.androidstudy.push.ParticularResultActivity;
import com.cindy.androidstudy.push.ResultActivity;
import com.cindy.androidstudy.volley.FcmData;
import com.cindy.androidstudy.volley.FcmResponse;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            MyApplication myApp = (MyApplication) getApplication();
            if (myApp.isBackground()) {
                Log.d(TAG, "MyApplication is on Backgroud");
            } else {
                Log.d(TAG, "MyApplication is on Foregroud");
            }

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");

            //notifyDefaultNotification(title, message);

            //notifyExtendedNotification(title, message);

            notifyNotificationWithNumber(title, message);
        }
    }

    /**
     * 같은 notifyID를 주면 마지막 내용으로 업데이트 됨!
     * @param title
     * @param message
     */
    private void notifyNotificationWithNumber(String title, String message) {

        final int notifyID = 5;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_money)
                .setContentTitle(title)
                .setContentText(message);

        //builder.setContentIntent(getRegularPendingIntent());
        builder.setContentIntent(getParticularPendingIntent());
        builder.setAutoCancel(true); //사용자가 알림을 클릭하면 알림 지우기

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, builder.build());

    }

    /**
     * 확장 레이아웃 적용 : InboxStyle로 알림을 펼쳤을 때 내용을 설정할 수 있음
     * @param title
     * @param message
     */
    private void notifyExtendedNotification(String title, String message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_money)
                .setContentTitle(title)
                .setContentText(message);

        //알림을 확장했을때 나타나는 내용 : 지원하는 디바이스에서만 보여짐 (안드로이드 4.1 이상)
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("큰 제목 타이틀????");
        inboxStyle.addLine("1번째 라인");
        inboxStyle.addLine("2번째 라인");
        inboxStyle.addLine("3번째 라인");
        inboxStyle.addLine("4번째 라인");
        inboxStyle.addLine("5번째 라인");
        inboxStyle.addLine("6번째 라인");
        inboxStyle.addLine("7번째 라인");
        inboxStyle.addLine("8번째 라인");
        inboxStyle.addLine("9번째 라인");
        inboxStyle.addLine("10번째 라인");

        builder.setStyle(inboxStyle);

        //알림이 클릭되었을때 보여줄 Activity
        builder.setContentIntent(getRegularPendingIntent());
        builder.setAutoCancel(true); //사용자가 알림을 클릭하면 알림 지우기

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }

    /**
     * 단순알림 만들기
     * @param title
     * @param message
     */
    private void notifyDefaultNotification(String title, String message) {

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_money)
                .setContentTitle(title)
                .setContentText(message);

        builder.setContentIntent(getRegularPendingIntent());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }

    /**
     * 정규 Activity 실행
     * addParentStack()을 호출하면 부모 액티비티(매니페스트에 설정된)와 함께 계층적으로 액티비티가 실행됨
     * @return
     */
    private PendingIntent getRegularPendingIntent() {

        Intent resultIntent = new Intent(this, ResultActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //MainActivity > ResultActivity로 스택에 쌓아 실행하는 방법 
        /*
        Intent resultIntent = new Intent(this, ResultActivity.class);
         Intent backIntent = new Intent(this, MainActivity.class); 
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);  
        return PendingIntent.getActivities(this, 0, new Intent[]{backIntent, resultIntent}, PendingIntent.FLAG_ONE_SHOT);
        */

    }

    private PendingIntent getParticularPendingIntent() {

        Intent resultIntent = new Intent(this, ParticularResultActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }
}
