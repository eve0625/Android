package com.cindy.androidstudy.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.cindy.androidstudy.R;

import java.io.IOException;

/**
 * SurfaceView는 직접 사용하는 것이 아니라, 상속받아 구현하고 Callback 클래스를 구현 및 설정하여 사용해야 함
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder; //실제 Surface를 관리하는 객체
    private Camera mCamera;

    public CameraPreview(Context context) {
        super(context);

        mHolder = getHolder();  //이 SurfaceView에 접근 가능한 Holder를 가져옴
        mHolder.addCallback(this); //Callback을 등록하여 SurfaceView의 생성, 변경, 소면을 알 수 있음
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) { //Surface 생성
        //카메라 초기화
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(holder); //미리보기를 표시할 객체 지정
        } catch (IOException e) {
            e.printStackTrace();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewSize(width, height);
        mCamera.setParameters(params);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { //Surface 종료 (다른 화면으로 이동하는 경우 등.)
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
}
