package com.cindy.androidstudy.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.cindy.androidstudy.R;

import java.io.IOException;

public class MedaiPlayerActivity extends AppCompatActivity {

    private static final int UPDATE_INTERVAL = 100;
    private static final float VOLUME_GAP = 0.1f;
    private static final int MUTE_INTERVAL = 100;

    MediaPlayer mPlayer;
    AudioManager mAudioManager;
    PlayState mState = PlayState.IDLE;
    boolean isTemporaryPaused = false;
    boolean isSeeking = false; //사용자가 seeking 여부 플래그

    Button btnPlay;
    Button btnPause;
    Button btnStop;
    SeekBar progressView;
    SeekBar volumeView;
    CheckBox chkMute;

    Handler mHandler = new Handler(Looper.getMainLooper());
    Runnable progressUpdate = new Runnable() {
        @Override
        public void run() {
            //재생중인 경우에만 진행률 표시
            if (mState == PlayState.STARTED) {
                if (!isSeeking) { //사용자가 seeking 하지 않을때
                    progressView.setProgress(mPlayer.getCurrentPosition());
                }
                mHandler.postDelayed(this, UPDATE_INTERVAL); //loop
            }
        }
    };

    float currentVolumn = 1.0f;
    Runnable smoothVolumeDown = new Runnable() {
        @Override
        public void run() {
            if (mPlayer != null) {
                if (currentVolumn > 0) {
                    mPlayer.setVolume(currentVolumn, currentVolumn);
                    currentVolumn -= VOLUME_GAP;
                    mHandler.postDelayed(this, MUTE_INTERVAL);
                } else {
                    currentVolumn = 0;
                    mPlayer.setVolume(0, 0);
                }
            }
        }
    };

    Runnable smoothVolumeUp = new Runnable() {
        @Override
        public void run() {
            if (mPlayer != null) {
                if (currentVolumn < 1) {
                    mPlayer.setVolume(currentVolumn, currentVolumn);
                    currentVolumn += VOLUME_GAP;
                    mHandler.postDelayed(this, MUTE_INTERVAL);
                } else {
                    currentVolumn = 1.0f;
                    mPlayer.setVolume(1.0f, 1.0f);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medai_player);

        //new로 만들면 IDLE 상태가 됨, datasource가 설정되면 INITAILIZED, prepare하면 PREPARED
        mPlayer = MediaPlayer.create(this, R.raw.song); //create는 위의 세 동작 (new, setDataSource, prepare)을 모두 수행
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
                mPlayer.seekTo(0);
                mState = PlayState.PAUSED;
                progressView.setProgress(0);
            }
        });

        progressView = (SeekBar) findViewById(R.id.progress);
        progressView.setMax(mPlayer.getDuration());
        progressView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int position = -1;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //사용자가 seeking 하는 경우 선택된 값을 마킹
                if (fromUser) {
                    this.position = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
                position = -1;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (position != -1) { //seek bar를 움직인 경우
                    if (mState == PlayState.STARTED) {
                        mPlayer.seekTo(position);
                    }
                }
                isSeeking = false;
            }
        });

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        volumeView = (SeekBar) findViewById(R.id.volume);
        volumeView.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)); //음악 볼륨으로 설정된 최대값 조회
        volumeView.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)); //현재 음악 볼륨 조회
        volumeView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) { //사용자가 seekbar를 조작하는 경우에만
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0); //볼륨 조정
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //initialize 이나 stop 상태인 경우, prepare 작업 실행행
               if (mState == PlayState.INITIALIED || mState == PlayState.STOPPED) {
                    try {
                        mPlayer.prepare();
                        mState = PlayState.PREPARED;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //prepare 이거나 pause 상태인 경우, 재생 시작
                if (mState == PlayState.PREPARED || mState == PlayState.PAUSED) {
                    if (progressView.getProgress() != -1) {
                        mPlayer.seekTo(progressView.getProgress());
                    }
                    mPlayer.start();
                    mState = PlayState.STARTED;
                    mHandler.post(progressUpdate); //진행률 표시
                }
            }
        });

        btnPause = (Button) findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //재생 중인 경우에만 pause (현재 위치에서 멈춤)
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

                //재생중이거나 pause 상태인 경우에만 stop (재생이 멈추고 위치가 초기화 됨)
                if (mState == PlayState.STARTED || mState == PlayState.PAUSED) {
                    mPlayer.stop();
                    mState = PlayState.STOPPED;
                    progressView.setProgress(0); //seek bar 위치 초기화
                }
            }
        });

        chkMute = (CheckBox) findViewById(R.id.chk_mute);
        chkMute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //초기화 (이전에 실행중이라면 먼저 stop)
                mHandler.removeCallbacks(smoothVolumeUp);
                mHandler.removeCallbacks(smoothVolumeDown);

                if (isChecked)
                    mHandler.post(smoothVolumeDown);
                else
                    mHandler.post(smoothVolumeUp);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //destroy시 반드시 자원 해제를 해줘야 함
        mPlayer.release();
        mPlayer = null;
        mState = PlayState.END;
    }

    @Override
    protected void onStop() {
        super.onStop();

        //activity가 멈추는 경우, 재생중인 오디오도 멈춤 처리
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

        //activity가 멈추어 pause된 경우는 다시 재생 시작
        if (isTemporaryPaused && mState == PlayState.PAUSED) {
            mPlayer.start();
            mState = PlayState.STARTED;
            mHandler.post(progressUpdate); //진행률 표시
        }
    }
}
