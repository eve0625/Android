package com.cindy.androidstudy.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.cindy.androidstudy.R;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "Notification";

    private Button btnNotiBasic;
    private Button btnNotiCustom;
    private Button btnNotiHeadUp;

    /**
     *
     * 안드로이드 5.0 롤리팝부터 환경설정에서 앱별로 알림을 관리(알림허용/무음으로 설정/중요 알림으로 설정)할 수 있게 됨
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        btnNotiBasic = (Button) findViewById(R.id.btn_noti_basic);
        btnNotiBasic.setOnClickListener(this);

        btnNotiCustom = (Button) findViewById(R.id.btn_noti_custom);
        btnNotiCustom.setOnClickListener(this);

        btnNotiHeadUp = (Button) findViewById(R.id.btn_noti_headup);
        btnNotiHeadUp.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {

        //알림이 차단된 경우 return
        if (!checkNotificationEnabled()) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_noti_basic : {

                //3초 후 알림 표시
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendBasicNotification();
                    }
                }, 3000);
                break;
            }
            case R.id.btn_noti_custom : {

                //Custom View를 쓰니 헤드업 알림은 뜨지 않는다!
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendCustomNotification();
                    }
                }, 3000);
                break;

            }
            case R.id.btn_noti_headup: {

                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendHeadsUpNotification();
                    }
                }, 3000);

                break;
            }
        }

    }

    private void sendStatusBarNotification() {

    }

    private void sendHeadsUpNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_place)
                .setContentTitle("헤드업 알림")
                .setContentText("알림 우선순위가 높으면서(MAX, HIGH) PendingIntent를 가지고 있는 경우 헤드업 알림이 보여집니다.")
                .setColor(Color.RED) //알림의 상단 smallicon + 앱이름 색상
                .setSubText("헤드업 알림 테스트") //상단 앱 이름 옆에 표시되는 텍스트
                .setAutoCancel(true); //사용자가 알림 터치시 자동으로 알림이 사라지도록 설정

        Intent intent = new Intent(this, NotiResultActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        builder.setPriority(Notification.PRIORITY_HIGH) //MAX/HIGH인 경우 상단에 알림뷰를 보여줌
            .setContentIntent(pendingIntent);

        //fullscreenIntent를 설정하면 헤드업 알림이 상단에 고정됨
        //두번째 인수(highPriority)를 true로 설정하면 다른 알림을 억제하고 상단 고정됨
        //전화나 알람같은 매우 중요한 알림에만 사용할 것
        //builder.setFullScreenIntent(pendingIntent, false);

        //action은 3개까지 추가 가능
        builder.addAction(R.drawable.ic_round, "돈벌러가기", pendingIntent);
        builder.addAction(R.drawable.ic_settings, "전화하기", pendingIntent);

        //알림음 설정 : 진동/무음모드가 아닐 경우에 사운드
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        //진동 설정 : 무음모드가 아닐 경우에 진동
        builder.setVibrate(new long[]{100, 1000, 300, 500, 100, 500}); //대기시간, 울림시간, 대기시간, 울림시간...

        //램프 설정 : 화면이 꺼져있을때 램프 표시됨
        builder.setLights(Color.BLUE, 1000, 10 * 1000); //on/off 시간



        int notifyId = 1; //같은 ID의 알림이 반복되면 마지막 알림 내용으로 업데이트 됨
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, builder.build());
    }

    private void sendCustomNotification() {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_custom_view);
        Intent intent = new Intent(this, NotiResultActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_place)
                .setTicker("Ticker입니다.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCustomContentView(remoteViews);
                //.setContent(remoteViews); //플랫폼 템플릿 대신에 사용할 커스텀 RemoteViews를 제공

        //일반보기 레이아웃은 64dp, 확장보기 레이아웃은 256dp로 제한되어 있다!
        remoteViews.setImageViewResource(R.id.image_left, R.mipmap.ic_launcher);
        remoteViews.setImageViewResource(R.id.image_right, R.drawable.ic_calendar);
        remoteViews.setTextViewText(R.id.title, "현실에서 쥔 칼자루 드라마에도 휘둘렀네");
        remoteViews.setTextViewText(R.id.text, "똑같은 조선 건국 과정을 놓고도 시대에 따라 해석이 다르다 1980년대만 해도 이성계와 이방원은 도탄에 빠진 백성을...");

         /*
         * 기기가 활성 상태일 때, 즉 잠금해제 상태이며 화면이 켜져있는 경우 (API 21부터 지원)
         * 상단에 작은 부동 창으로 노출되며 작업 버튼도 표시 가능하다.
         * 사용자 액티비티가 전체화면 모드이거나(fullScreenIntent를 사용할 경우)
         * 알림의 우선순위가 높고 벨소리나 진동을 사용할 경우 헤드업 알림이 트리거 됨
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        }

        int notifyId = 2;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, builder.build());

    }

    private void sendBasicNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_place)
                .setContentTitle("기본 알림")
                .setContentText("가장 기본적인 Notification입니다.")
                .setColor(Color.RED) //알림의 상단 smallicon + 앱이름 색상
                .setSubText("서브텍스트는 무엇일지??") //상단 앱 이름 옆에 표시되는 텍스트
                /**
                 * Visibility : 보안 잠금 화면에 알림이 얼마나 표시될 것인지 설정 (????)
                 * VISIBILITY_SECRET : 알림의 어떤 부분도 표시하지 않음 (진동, 램프 등만..)
                 * VISIBILITY_PRIVATE : 알림 아이콘, 제목 등의 기본 정보만 표시
                 * VISIBILITY_PUBLIC : 알림의 전체 콘텐츠를 표시
                 */
                .setVisibility(Notification.VISIBILITY_PRIVATE)
                //.setLargeIcon()
                //.setUsesChronometer(true) //알림 도착시간 대신 스톱워치 필드를 보여줌 (도착 후 몇분, 몇초가 경과하였는지 표시)
                .setAutoCancel(true); //사용자가 알림 터치시 자동으로 알림이 사라지도록 설정

        //환경설정 애플이케이션 알림의 [잠금화면에 표시할 내용]이 내용숨기기로 설정된 경우에만 동작함 (setVisibility와 상관이 없네??)
        NotificationCompat.Builder publicNotiBuilder = new NotificationCompat.Builder(this);
        publicNotiBuilder.setContentTitle("새로운 알림 도착")
                .setSmallIcon(R.drawable.ic_place)
                .setContentText("내용비공개!!");
        builder.setPublicVersion(publicNotiBuilder.build());

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

        //알림 클릭시 실행될 Activity 설정 (PendingIntent)
        /*
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotiResultActivity.class);
        stackBuilder.addNextIntent(new Intent(this, NotiResultActivity.class));
        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        */

        Intent intent = new Intent(this, NotiResultActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT));

        //알림음 설정 : 진동/무음모드가 아닐 경우에 사운드
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        //builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        //builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

        //진동 설정 : 무음모드가 아닐 경우에 진동
        builder.setVibrate(new long[]{100, 1000, 300, 500, 100, 500}); //대기시간, 울림시간, 대기시간, 울림시간...

        //램프 설정 : 화면이 꺼져있을때 램프 표시됨
        builder.setLights(Color.BLUE, 1000, 10 * 1000); //on/off 시간

        //기본값 설정
        //builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        //builder.setDefaults(Notification.DEFAULT_ALL); //시스템에 설정된 기본값으로 알림 (위에 설정한 옵션들이 무시된다!)

        builder.setTicker("this is ticker");
        builder.setContentInfo("정보는 어디에 나타나는 것일까??");
        builder.setNumber(3);
        //builder.setOngoing(true); //Ongoing Notification은 패널에서 일반 알림보다 위에 정렬되고 Close 버튼이 없으며 '모두지우기' 버튼의 영향을 받지 않음
        builder.setOnlyAlertOnce(true); //같은 ID의 알림이 이미 보이는 상태인 경우 다시 알리지 않음

        //우선순위
        builder.setPriority(Notification.PRIORITY_HIGH); //MAX/HIGH인 경우 상단에 알림뷰를 보여줌

        int notifyId = 3; //같은 ID의 알림이 반복되면 마지막 알림 내용으로 업데이트 됨
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, builder.build());

    }

    /**
     * 시스템 설정의 알림이 허용되어 있는지 체크
     * @return
     */
    private boolean checkNotificationEnabled() {

        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("알림 허용")
                    .setMessage("알림이 차단되어 있습니다. 알림을 허용해주세요.")
                    .setPositiveButton("설정", new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                //해당 앱의 [알림 > 고급 알림 설정 > 애플리케이션 알림] 화면으로 이동 : Navi 버튼 클릭이 다른 설정화면들로 자꾸 이동함...
                                Intent intent = new Intent();
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", getPackageName());
                                intent.putExtra("app_uid", getApplicationInfo().uid);
                                startActivity(intent);
                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                                //해당 앱의 [애플리케이션 정보] 화면으로 이동
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }

                        }
                    }).setNegativeButton("취소", new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();

            return false;
        }
        return true;

    }
}
