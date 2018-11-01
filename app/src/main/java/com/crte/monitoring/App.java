package com.crte.monitoring;

import android.app.Application;
import com.github.nkzawa.socketio.client.Socket;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoRendererGui;

public class App extends Application {
    public static Socket socket;
    @Override
    public void onCreate() {
        super.onCreate();
        PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true, VideoRendererGui.getEGLContext());
    }
}
