package com.crte.monitoring.test2;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import com.crte.monitoring.R;

public class Test2RecordActivity extends Activity {
 
	private SurfaceView mSurfaceView;
	private Server mServer;
	private VideoDecoder mVideoDecoder;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test2_record);
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mServer = Server.getInstance();
		mVideoDecoder = new VideoDecoder(mSurfaceView.getHolder().getSurface(),
				mServer);
		mVideoDecoder.start();
	}
}
