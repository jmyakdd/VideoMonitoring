package com.crte.monitoring.test4

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.crte.monitoring.R
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_test4_play.*
import org.json.JSONException
import org.json.JSONObject
import org.webrtc.*
import java.net.URISyntaxException
import java.util.*


class Test4PlayActivity : AppCompatActivity() {

    // Local preview screen position before call is connected.
    private val LOCAL_X_CONNECTING = 0
    private val LOCAL_Y_CONNECTING = 0
    private val LOCAL_WIDTH_CONNECTING = 100
    private val LOCAL_HEIGHT_CONNECTING = 100

    private val TAG = Test4PlayActivity::class.java.canonicalName
    private lateinit var socket: Socket
    private var peer: PeerConnection? = null
    private lateinit var factory: PeerConnectionFactory
    private var pcConstraints = MediaConstraints()
    private val iceServers = LinkedList<PeerConnection.IceServer>()
    private var callId = Content.callID
    private var remoteRender: VideoRenderer.Callbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test4_play)

        btn_init.setOnClickListener {
            createConnect()
        }

        btn_open.setOnClickListener {
            var json = JSONObject()
            json.put("data", "test")
            sendMessage(callId, "opt", json)
        }

        btn_close.setOnClickListener {
            var json = JSONObject()
            json.put("data", "test")
            sendMessage(callId, "close", json)
        }

        gsv.setPreserveEGLContextOnPause(true)
        gsv.setKeepScreenOn(true)
        VideoRendererGui.setView(gsv) { init() }

        remoteRender = VideoRendererGui.create(
            LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
            LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, false
        )
    }

    private fun createConnect() {
        sendMessage(callId, "init", null)
    }

    private fun init() {
        factory = PeerConnectionFactory()
        try {
            socket = IO.socket(Content.host)
        } catch (e: URISyntaxException) {
            return
        }
        socket.on("id", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                setText(args[0].toString())

                val message = JSONObject()
                message.put("name", "android_test")
                socket.emit("readyToStream", message)
            }
        })
        socket.on("message", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                var json: JSONObject = args[0] as JSONObject
                Log.e("test", json.toString())
                var type = json.getString("type")
                var from = json.getString("from")
                var payload: JSONObject? = null
                if (!type.equals("init")) {
                    payload = json.getJSONObject("payload")
                }
                if (peer == null) {
                    peer = factory.createPeerConnection(iceServers, pcConstraints, MyObserver())
                }
                when (type) {
                    "init" -> {
                        peer!!.createOffer(MySdpObserver(), pcConstraints)
                    }
                    "offer" -> {
                        val sdp = SessionDescription(
                            SessionDescription.Type.fromCanonicalForm(payload!!.getString("type")),
                            payload.getString("sdp")
                        )
                        peer!!.setRemoteDescription(MySdpObserver(), sdp)
                        peer!!.createAnswer(MySdpObserver(), pcConstraints)
                    }
                    "answer" -> {
                        val sdp = SessionDescription(
                            SessionDescription.Type.fromCanonicalForm(payload!!.getString("type")),
                            payload.getString("sdp")
                        )
                        peer!!.setRemoteDescription(MySdpObserver(), sdp)
                    }
                    "candidate" -> {
                        if (peer!!.getRemoteDescription() != null) {
                            val candidate = IceCandidate(
                                payload!!.getString("id"),
                                payload.getInt("label"),
                                payload.getString("candidate")
                            )
                            peer!!.addIceCandidate(candidate)
                            Log.e(TAG,"addIceCandidate")
                        }
                    }
                }
            }
        })
        socket.connect()

        pcConstraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
        pcConstraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
        pcConstraints.optional.add(MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"))
    }

    fun setText(string: String) {
        runOnUiThread {
            tv_content.setText(string)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (peer != null) {
            peer!!.close()
            peer = null
        }
        factory.dispose()
        socket.disconnect()
        socket.close()
        socket.disconnect()
        socket.close()
    }

    /**
     * Send a message through the signaling server
     *
     * @param to      id of recipient
     * @param type    type of message
     * @param payload payload of message
     * @throws JSONException
     */
    fun sendMessage(to: String, type: String, payload: JSONObject?) {
        val message = JSONObject()
        message.put("to", to)
        message.put("type", type)
        message.put("payload", payload)
        socket.emit("message", message)
    }

    inner class MyObserver : PeerConnection.Observer {

        override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
            Log.e(TAG, "onIceGatheringChange")
        }

        override fun onAddStream(p0: MediaStream?) {
            Log.e(TAG, "onAddStream")
            if (p0 != null) {
                p0.videoTracks.get(0).addRenderer(VideoRenderer(remoteRender))
            }
        }

        override fun onIceCandidate(candidate: IceCandidate?) {
            Log.e(TAG, "onIceCandidate")
            val payload = JSONObject()
            payload.put("label", candidate!!.sdpMLineIndex)
            payload.put("id", candidate.sdpMid)
            payload.put("candidate", candidate.sdp)
            sendMessage(callId, "candidate", payload)
        }

        override fun onDataChannel(p0: DataChannel?) {
            Log.e(TAG, "onDataChannel")
        }

        override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
            Log.e(TAG, "onSignalingChange")
        }

        override fun onRemoveStream(p0: MediaStream?) {
            Log.e(TAG, "onRemoveStream")
            peer!!.close()
            peer = null
        }

        override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
            Log.e(TAG, "onIceConnectionChange")
            if (p0 == PeerConnection.IceConnectionState.DISCONNECTED) {
                Log.e(TAG, "连接断开")
                peer!!.close()
                peer = null
            }
        }

        override fun onRenegotiationNeeded() {
            Log.e(TAG, "onRenegotiationNeeded")
        }
    }

    inner class MySdpObserver : SdpObserver {
        override fun onSetFailure(p0: String?) {
            Log.e(TAG, "onSetFailure")
        }

        override fun onSetSuccess() {
            Log.e(TAG, "onSetSuccess")
        }

        override fun onCreateSuccess(sdp: SessionDescription?) {
            Log.e(TAG, "onCreateSuccess")
            if (sdp != null) {
                val payload = JSONObject()
                payload.put("type", sdp.type.canonicalForm())
                payload.put("sdp", sdp.description)
                sendMessage(callId, sdp.type.canonicalForm(), payload)
                peer!!.setLocalDescription(this, sdp)
            }
        }

        override fun onCreateFailure(p0: String?) {
            Log.e(TAG, "onCreateFailure")
        }

    }
}