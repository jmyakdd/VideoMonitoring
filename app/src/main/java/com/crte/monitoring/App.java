package com.crte.monitoring;

import android.app.Application;
import com.crte.monitoring.test4.Content;
import com.crte.monitoring.test4.SharedPreferencUtil;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.*;

import java.net.URISyntaxException;

public class App extends Application {
    public static PeerConnectionFactory factory;
    public static int groupId;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencUtil.init(this,"Video_Monitoring");
        PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true, VideoRendererGui.getEGLContext());
        factory = new PeerConnectionFactory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        factory.dispose();
    }
}
