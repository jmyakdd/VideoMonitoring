package com.crte.monitoring.test3

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.crte.monitoring.R
import kotlinx.android.synthetic.main.activity_test3_record.*
import org.webrtc.MediaStream
import org.webrtc.VideoRenderer
import org.webrtc.VideoRendererGui

class Test3RecordActivity1 : AppCompatActivity(), WebRtcClient.RtcListener {

    // Local preview screen position before call is connected.
    private val LOCAL_X_CONNECTING = 0
    private val LOCAL_Y_CONNECTING = 0
    private val LOCAL_WIDTH_CONNECTING = 100
    private val LOCAL_HEIGHT_CONNECTING = 100

    private var localRender: VideoRenderer.Callbacks? = null
    private var client: WebRtcClient? = null

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
        client = WebRtcClient(this, "http://192.168.2.130:3001", VideoRendererGui.getEGLContext())
    }

    override fun onCallReady(callId: String?) {
        runOnUiThread {
            text.setText(callId)
        }
        client!!.start("android_test")
        client!!.setCamera()
    }

    override fun onStatusChanged(newStatus: String?) {
        Log.e("test",newStatus)
    }

    override fun onLocalStream(localStream: MediaStream?) {
        if (localStream != null) {
            localStream.videoTracks.get(0).addRenderer(VideoRenderer(localRender))
        }
    }

    override fun onAddRemoteStream(remoteStream: MediaStream?, endPoint: Int) {

    }

    override fun onRemoveRemoteStream(endPoint: Int) {

    }

    override fun onPause() {
        super.onPause()
        gsv.onPause()
        if (client != null) {
            client!!.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        gsv.onResume()
        if (client != null) {
            client!!.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (client != null) {
            client!!.onDestroy()
        }
    }
}