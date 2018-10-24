package com.crte.monitoring.test2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.crte.monitoring.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

public class Test2PlayActivity extends Activity {
	private Client mClient;
	private byte[] fileBytes;
	private int count = 0;
	private Button bt_spspps;
	private Button bt_h264;
 
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test2_paly);
		bt_spspps = (Button) findViewById(R.id.bt_spspps);
		bt_h264 = (Button) findViewById(R.id.bt_h264);
		fileBytes = getByte("/storage/emulated/0/Movies/1280_720.h264");
		bt_spspps.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					@SuppressWarnings("unused")
					@SuppressLint("NewApi")
					@Override
					public void run() {
						mClient = Client.getInstance();
						mClient.connect();
						start: for (int i = 0; i < fileBytes.length; i++) {
							if (fileBytes[i] == 0 && fileBytes[i + 1] == 0
									&& fileBytes[i + 2] == 0
									&& fileBytes[i + 3] == 1) {
								end: for (int j = i + 4; j < fileBytes.length; j++) {
									if (fileBytes[j] == 0
											&& fileBytes[j + 1] == 0
											&& fileBytes[j + 2] == 0
											&& fileBytes[j + 3] == 1) {
										byte[] temp = Arrays.copyOfRange(
												fileBytes, i, j);
										mClient.sendLength(intToBytes(temp.length));
										mClient.sendSPSPPS(temp);
										count++;
										if (count == 2) {
											runOnUiThread(new Runnable() {
												public void run() {
													bt_spspps.setEnabled(false);
												}
											});
											return;
										}
										break end;
									}
								}
							}
						}
					}
				}).start();
 
			}
		});
		bt_h264.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					@SuppressWarnings("unused")
					@Override
					public void run() {
						// TODO Auto-generated method stub
						start: for (int i = 0; i < fileBytes.length; i++) {
							if (fileBytes[i] == 0 && fileBytes[i + 1] == 0
									&& fileBytes[i + 2] == 0
									&& fileBytes[i + 3] == 1) {
								end: for (int j = i + 4; j < fileBytes.length; j++) {
									if (fileBytes[j] == 0
											&& fileBytes[j + 1] == 0
											&& fileBytes[j + 2] == 0
											&& fileBytes[j + 3] == 1) {
										byte[] temp = Arrays.copyOfRange(
												fileBytes, i, j);
										mClient.sendLength(intToBytes(temp.length));
										mClient.sendFrame(temp);
										break end;
									}
								}
							}
						}
					}
				}).start();
			}
		});
	}
 
	private byte[] getByte(String path) {
		File f = new File(path);
		InputStream in;
		byte bytes[] = null;
		try {
			if(!f.exists()){
				f.mkdirs();
			}
			in = new FileInputStream(f);
			bytes = new byte[(int) f.length()];
			in.read(bytes);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return bytes;
	}
 
	public byte[] intToBytes(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (i & 0xff);
		bytes[1] = (byte) ((i >> 8) & 0xff);
		bytes[2] = (byte) ((i >> 16) & 0xff);
		bytes[3] = (byte) ((i >> 24) & 0xff);
		return bytes;
	}
}
