package com.cindy.androidstudy.animation;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.cindy.androidstudy.R;

public class SlideBackgroundActivity extends AppCompatActivity implements View.OnClickListener {

    private View bgPlay;
    private Button btnPlay;
    private Button btnPause;
    private TextView tvLeftTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_background);

        bgPlay = findViewById(R.id.bg_play);

        btnPause = (Button) findViewById(R.id.btn_pause);
        btnPause.setVisibility(View.INVISIBLE);
        btnPause.setOnClickListener(this);

        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);

        tvLeftTime = (TextView) findViewById(R.id.tv_left_time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play : {
                //음성파일 가져오기
                playVoiceMessage(4);
                /*
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playVoiceMessage(4);
                    }
                }, 1000);
                */
                break;
            }
        }
    }

    private int mCurrentLeftTime;
    private CountDownTimer mTimer;
    private void playVoiceMessage(final int seconds) {

        mCurrentLeftTime = seconds;
        mTimer = new CountDownTimer((mCurrentLeftTime + 1) * 1000 + 500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvLeftTime.setText(getLeftTime(mCurrentLeftTime--));
            }

            @Override
            public void onFinish() {
                tvLeftTime.setText(getLeftTime(seconds));
            }
        };

        mTimer.start();
    }

    /*
    private Animation animPlaying;
    private int mCurrentLeftTime;
    private void playVoiceMessage(final int seconds) {

        if (animPlaying == null) {
            animPlaying = new TranslateAnimation(-bgPlay.getWidth(), bgPlay.getWidth(), 0, 0);
            animPlaying.setDuration(seconds * 1000);
            animPlaying.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    bgPlay.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bgPlay.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        mCurrentLeftTime = seconds;
        final CountDownTimer timer = new CountDownTimer((seconds + 1) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvLeftTime.setText(getLeftTime(--mCurrentLeftTime));
            }

            @Override
            public void onFinish() {
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.INVISIBLE);
                tvLeftTime.setText(getLeftTime(seconds));
            }
        };

        bgPlay.postDelayed(new Runnable() {
            @Override
            public void run() {
                timer.start();
            }
        }, 1000);

        bgPlay.startAnimation(animPlaying);
    }
    */

    private String getLeftTime(int leftSeconds) {
        int min = leftSeconds / 60;
        int sec = leftSeconds - (min * 60);
        return String.format("%02d:%02d", min, sec);
    }

}
