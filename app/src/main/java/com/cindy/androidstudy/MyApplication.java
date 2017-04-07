package com.cindy.androidstudy;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class MyApplication extends Application {

    private static final String LOG_TAG = "MyApplication";

    private boolean isBackground = true;

    public MyApplication() {
        super();
        Log.e(LOG_TAG, "constructor");
    }

    public boolean isBackground() {
        return isBackground;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(LOG_TAG, "onCreate");

        //화면이 꺼졌을때 receiver -> 백그라운드 상태로 값 변경
        IntentFilter screenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isBackground) {
                    isBackground = false;
                    Log.e(LOG_TAG, "App is on Background(Screen Off)");
                }
            }
        }, screenOffFilter);

        //(어떠한) 액티비티가 실행되면 포그라운드 상태로 값을 변경함
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (isBackground) {
                    isBackground = false;
                    Log.e(LOG_TAG, "App is on Foreground");
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e(LOG_TAG, "onTerminate");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(LOG_TAG, "onLowMemory");
    }

    /**
     * API 14 부터 지원하는 콜백 메서드
     * UI가 숨겨지거나, 시스템 리소스가 부족할때 메모리를 해제함
     * @param level
     * TRIM_MEMORY_UI_HIDDEN : UI가 백그라운드로 옮겨졌을때 모든 UI가 사용하는 메모리를 해제함
     * TRIM_MEMORY_RUNNING_MODERATE, _LOW, _CRITICAL : 앱 실행에 필요하지 않은 메모리를 해제함
     *  앱 실행 도중에 메모리가 낮아질 때 메모리 관련 이벤트의 심각도를 나타냄.
     *  CRITICAL 이벤트가 발생하면 시스템은 백그라운드의 프로세스를 죽이기 시작한다.
     * TRIM_MEMORY_BACKGROUND, _MODERATE, COMPLETE : 프로세스가 가능한 만큼의 메모리를 해제
     *  앱이 LRU 리스트에 있고 시스템은 메모리가 부족할 때 이벤트가 발생
     *  COMPLETE 이벤트가 발생하면 이 프로세스는 첫번째로 종료된다.
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e(LOG_TAG, "onTrimMemory");

        //화면이 꺼진 경우에는 이 콜백이 호출되지 않으므로, Boradcast Receiver로 처리 (onCreate에서 regist)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isBackground = true;
            Log.e(LOG_TAG, "App is on Background");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(LOG_TAG, "onConfigurationChanged");
    }

    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
        Log.e(LOG_TAG, "registerComponentCallbacks");
    }

    @Override
    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        super.unregisterComponentCallbacks(callback);
        Log.e(LOG_TAG, "unregisterComponentCallbacks");
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(callback);
        Log.e(LOG_TAG, "registerActivityLifecycleCallbacks");
    }

    @Override
    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.unregisterActivityLifecycleCallbacks(callback);
        Log.e(LOG_TAG, "unregisterActivityLifecycleCallbacks");
    }

    @Override
    public void registerOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.registerOnProvideAssistDataListener(callback);
        Log.e(LOG_TAG, "registerOnProvideAssistDataListener");
    }

    @Override
    public void unregisterOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.unregisterOnProvideAssistDataListener(callback);
        Log.e(LOG_TAG, "unregisterOnProvideAssistDataListener");
    }

}
