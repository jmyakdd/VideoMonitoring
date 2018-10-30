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

class Test3RecordActivity : AppCompatActivity() {
    // Local preview screen position before call is connected.
    private val LOCAL_X_CONNECTING = 0
    private val LOCAL_Y_CONNECTING = 0
    private val LOCAL_WIDTH_CONNECTING = 100
    private val LOCAL_HEIGHT_CONNECTING = 100

    lateinit var factory: PeerConnectionFactory
    lateinit var localMS: MediaStream
    lateinit var videoSource: VideoSource
    private var localRender: VideoRenderer.Callbacks? = null

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

        localRender = VideoRendererGui.create(
            LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
            LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, false
        )
    }

    private fun init() {
        PeerConnectionFactory.initializeAndroidGlobals(this, true, true, true, VideoRendererGui.getEGLContext())
        factory = PeerConnectionFactory()

        setCamera()

        initConnect()
    }

    private fun initConnect() {
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
        client.on("message",object :Emitter.Listener{
            override fun call(vararg args: Any?) {

            }
        })
        client.connect()
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

    private fun setCamera() {
        //打开摄像头
        var backCameraName = VideoCapturerAndroid.getNameOfBackFacingDevice()
        var videoCapturer = VideoCapturerAndroid.create(backCameraName)
        var videoConstraints = MediaConstraints()
        //获取视频流
        localMS = factory.createLocalMediaStream("ARDAMS")
        videoSource = factory.createVideoSource(videoCapturer, videoConstraints)
        localMS.addTrack(factory.createVideoTrack("ARDAMSv0", videoSource))
        localMS.videoTracks.get(0).addRenderer(VideoRenderer(localRender))

    }

    override fun onDestroy() {
        if (videoSource != null) {
            videoSource.stop()
        }
        if (localMS != null) {
            localMS.dispose()
        }
        super.onDestroy()
    }
}