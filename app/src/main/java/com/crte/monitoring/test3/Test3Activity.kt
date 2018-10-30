package com.crte.monitoring.test3

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crte.monitoring.R
import kotlinx.android.synthetic.main.activity_test.*

class Test3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        record.setOnClickListener {
            startActivity(Intent(this, Test3RecordActivity::class.java))
        }
        play.setOnClickListener {
            startActivity(Intent(this, Test3PlayActivity::class.java))
        }
    }
}