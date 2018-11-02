package com.crte.monitoring;

import android.app.Application;
import com.crte.monitoring.test4.Content;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoRendererGui;

import java.net.URISyntaxException;

public class App extends Application {
    public static Socket socket;
    public static String callId = "";
    public static boolean isConnectServer = false;
    public static MediaStream localStream = null;
    public static PeerConnectionFactory factory;

    @Override
    public void onCreate() {
        super.onCreate();
        PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true, VideoRendererGui.getEGLContext());
        factory = new PeerConnectionFactory();
    }

    public static void connect() {
        try {
            socket = IO.socket(Content.host);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        socket.on("id", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                isConnectServer = true;
                callId = args[0].toString();
                JSONObject message = new JSONObject();
                try {
                    message.put("name", "android_test");
                    socket.emit("readyToStream", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        socket.connect();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        factory.dispose();
        socket.off();
        socket.disconnect();
        socket.close();
    }
}
