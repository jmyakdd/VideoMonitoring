package com.crte.monitoring.test1;


import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.crte.monitoring.R;

import java.io.ByteArrayOutputStream;

public class Test1RecordActivity extends Activity {
    //    ClientThread clientThread;
    ByteArrayOutputStream outstream;

    Button start;
    Button stop;
    SurfaceView surfaceView;
    SurfaceHolder sfh;
    Camera camera;
    boolean isPreview = false;        //是否在浏览中
    int screenWidth = 300, screenHeight = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test1_record);

//        clientThread = new ClientThread(handler);
//        new Thread(clientThread).start();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        screenHeight = dm.heightPixels;


        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        sfh = surfaceView.getHolder();
        sfh.setFixedSize(screenWidth, screenHeight / 4 * 3);

        sfh.addCallback(new Callback() {

            @Override
            public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
                                       int arg3) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder arg0) {
                start.setEnabled(true);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder arg0) {
                stopCamera();
            }

        });

        start.setEnabled(false);
        //开启连接服务
        start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                start.setEnabled(false);
                initCamera();
            }

        });

        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCamera();
            }
        });


    }

    private void stopCamera() {
        if (camera != null) {
            if (isPreview) {
                camera.stopPreview();
                isPreview = false;
            }
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
            start.setEnabled(true);
        }
    }

    @SuppressWarnings("deprecation")
    private void initCamera() {
        if (!isPreview) {
            camera = Camera.open();
//            ClientThread.size = camera.getParameters().getPreviewSize();
        }
        if (camera != null && !isPreview) {
            try {
                camera.setPreviewDisplay(sfh);                 // 通过SurfaceView显示取景画面    
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(screenWidth, screenHeight / 4 * 3);
                /* 每秒从摄像头捕获5帧画面， */
                parameters.setPreviewFrameRate(5);
                parameters.setPictureFormat(PixelFormat.JPEG);           // 设置图片格式
                parameters.setPictureSize(screenWidth, screenHeight / 4 * 3);    // 设置照片的大小
                camera.setDisplayOrientation(90);
                camera.setPreviewCallback(new PreviewCallback() {

                    @Override
                    public void onPreviewFrame(byte[] data, Camera c) {

                    }
                });
                camera.startPreview();                                   // 开始预览
                camera.autoFocus(null);                                  // 自动对焦
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPreview = true;
        }
    }
}