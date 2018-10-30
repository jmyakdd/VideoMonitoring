package com.crte.monitoring.test3

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crte.monitoring.R
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_test3_record.*
import org.webrtc.*
import java.net.URISyntaxException
import java.util.*

class Test3PlayActivity:AppCompatActivity() {
    private val LOCAL_X_CONNECTING = 0
    private val LOCAL_Y_CONNECTING = 0
    private val LOCAL_WIDTH_CONNECTING = 100
    private val LOCAL_HEIGHT_CONNECTING = 100

    lateinit var factory: PeerConnectionFactory
    lateinit var localMS: MediaStream
    lateinit var videoSource: VideoSource
    private var remoteRender: VideoRenderer.Callbacks? = null

    lateinit var client: Socket

    lateinit var peerConnection: PeerConnection
    private val pcConstraints = MediaConstraints()
    private val iceServers = LinkedList<PeerConnection.IceServer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test3_record)
        gsv.setPreserveEGLContextOnPause(true)
        gsv.setKeepScreenOn(true)
        VideoRendererGui.setView(gsv) { init() }

        remoteRender = VideoRendererGui.create(
            LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
            LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, false
        )
    }

    private fun init() {
        PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true, VideoRendererGui.getEGLContext())
        factory = PeerConnectionFactory()

        initPeer()
    }

    private fun initPeer() {
        try {
            client = IO.socket("http://192.168.2.130:3001")
        } catch (e: URISyntaxException) {

        }
        client.on("id", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                runOnUiThread {
                    text.setText(args[0].toString())
                }
            }
        })
        client.connect()

        iceServers.add(PeerConnection.IceServer("stun:23.21.150.121"))
        iceServers.add(PeerConnection.IceServer("stun:stun.l.google.com:19302"))
        peerConnection = factory.createPeerConnection(iceServers,pcConstraints,MyPeerObserver())
        peerConnection.addStream(localMS)
    }

    inner class MyPeerObserver : PeerConnection.Observer {
        override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {

        }

        override fun onAddStream(p0: MediaStream?) {

        }

        override fun onIceCandidate(p0: IceCandidate?) {

        }

        override fun onDataChannel(p0: DataChannel?) {

        }

        override fun onSignalingChange(p0: PeerConnection.SignalingState?) {

        }

        override fun onRemoveStream(p0: MediaStream?) {

        }

        override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {

        }

        override fun onRenegotiationNeeded() {

        }
    }
}