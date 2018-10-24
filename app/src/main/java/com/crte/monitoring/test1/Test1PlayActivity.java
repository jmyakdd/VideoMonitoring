package com.crte.monitoring.test1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import com.crte.monitoring.R;

public class Test1PlayActivity extends Activity {
    RevImageThread revImageThread;
    public static ImageView image;
    private static Bitmap bitmap;
    private static final int COMPLETED = 0x111;
    private MyHandler handler;
     
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1_play);
        image=(ImageView)findViewById(R.id.imageView1);
        handler = new MyHandler();
        revImageThread = new RevImageThread(handler);
        new Thread(revImageThread).start();
        
    }
    
    static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
             if (msg.what == COMPLETED) {
                 bitmap = (Bitmap)msg.obj;
                image.setImageBitmap(bitmap); 
                super.handleMessage(msg);
             }  
        }
    }
}