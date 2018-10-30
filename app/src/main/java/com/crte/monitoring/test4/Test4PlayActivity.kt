package com.crte.monitoring.test4

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crte.monitoring.R
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_test4_play.*
import java.net.URISyntaxException

class Test4PlayActivity : AppCompatActivity() {
    private lateinit var socket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test4_play)

        btn_init.setOnClickListener {
            init()
        }
    }

    private fun init() {
        try {
            socket = IO.socket(Content.host)
        } catch (e: URISyntaxException) {
            return
        }
        socket.on("id", object : Emitter.Listener {
            override fun call(vararg args: Any?) {
                setText(args[0].toString())
            }
        })
        socket.on("message", object : Emitter.Listener {
            override fun call(vararg args: Any?) {

            }
        })
        socket.connect()
    }

    fun setText(string: String) {
        runOnUiThread {
            tv_content.setText(string)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
        socket.close()
    }
}