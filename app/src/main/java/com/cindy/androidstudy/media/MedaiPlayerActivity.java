package com.cindy.androidstudy.media;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cindy.androidstudy.R;

import java.io.IOException;

public class MedaiPlayerActivity extends AppCompatActivity {

    MediaPlayer mPlayer;
    PlayState mState = PlayState.IDLE;
    boolean isTemporaryPaused = false;

    Button btnPlay;
    Button btnPause;
    Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medai_player);

        //new로 만들면 IDLE 상태가 됨, datasource가 설정되면 INITAILIZED, prepare하면 PREPARED
        mPlayer = MediaPlayer.create(this, R.raw.voice); //create는 위의 세 동작을 모두 수행
        mState = PlayState.PREPARED;

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mState = PlayState.ERROR;
                return false;
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mState = PlayState.PAUSED;
            }
        });

        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == PlayState.INITIALIED || mState == PlayState.STOPPED) {
                    try {
                        mPlayer.prepare();
                        mState = PlayState.PREPARED;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (mState == PlayState.PREPARED || mState == PlayState.PAUSED) {
                    mPlayer.start();
                    mState = PlayState.STARTED;
                }
            }
        });

        btnPause = (Button) findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == PlayState.STARTED) {
                    mPlayer.pause();
                    mState = PlayState.PAUSED;
                }
            }
        });

        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == PlayState.STARTED || mState == PlayState.PAUSED) {
                    mPlayer.stop();
                    mState = PlayState.STOPPED;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPlayer.release();
        mPlayer = null;
        mState = PlayState.END;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mState == PlayState.STARTED) {
            mPlayer.pause();
            mState = PlayState.PAUSED;
            isTemporaryPaused = true;
        } else {
            isTemporaryPaused = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isTemporaryPaused && mState == PlayState.PAUSED) {
            mPlayer.start();
            mState = PlayState.STARTED;
        }
    }
}
