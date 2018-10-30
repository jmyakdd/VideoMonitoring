package com.crte.monitoring.test3;

import android.content.Context;
import android.opengl.EGLContext;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.*;

import java.util.LinkedList;

public class WebRtcClient1 {
    public final static int RECEIVE = 0;
    public final static int SEND = 1;
    private final static String TAG = WebRtcClient.class.getCanonicalName();
    private PeerConnectionFactory factory;
    private LinkedList<PeerConnection.IceServer> iceServers = new LinkedList<>();
    private MediaConstraints pcConstraints = new MediaConstraints();
    private MediaStream localMS;
    private VideoSource videoSource;
    private WebRtcClient.RtcListener mListener;
    private Socket client;
    private int type = RECEIVE;
    private PeerConnection pc;

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                String from = data.getString("from");
                String type = data.getString("type");
                JSONObject payload = null;
                if (!type.equals("init")) {
                    payload = data.getJSONObject("payload");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onId = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String id = (String) args[0];
            mListener.onCallReady(id);
        }
    };

    public WebRtcClient1(Context context, int type, String host, EGLContext mEGLcontext) {
        PeerConnectionFactory.initializeAndroidGlobals(context, true, true,
                true, mEGLcontext);
        factory = new PeerConnectionFactory();

        pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
        pcConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        pc = factory.createPeerConnection(iceServers, pcConstraints, new MyPeerObserver());
    }

    private class MyPeerObserver implements PeerConnection.Observer {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {

        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {

        }

        @Override
        public void onAddStream(MediaStream mediaStream) {

        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {

        }

        @Override
        public void onRenegotiationNeeded() {

        }
    }
}
