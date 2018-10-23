package com.crte.monitoring

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        record.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainVideoActivity::class.java))
        }
    }
}
