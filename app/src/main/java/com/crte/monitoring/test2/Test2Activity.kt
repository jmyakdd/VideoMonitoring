package com.crte.monitoring.test2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crte.monitoring.R
import kotlinx.android.synthetic.main.activity_test.*

class Test2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        record.setOnClickListener {
            startActivity(Intent(this@Test2Activity, Test2RecordActivity::class.java))
        }

        play.setOnClickListener {
            startActivity(Intent(this@Test2Activity, Test2PlayActivity::class.java))
        }
    }
}
