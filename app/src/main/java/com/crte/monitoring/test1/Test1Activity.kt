package com.crte.monitoring.test1

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crte.monitoring.R
import kotlinx.android.synthetic.main.activity_test.*

class Test1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        record.setOnClickListener {
            startActivity(Intent(this@Test1Activity, Test1RecordActivity::class.java))
        }

        play.setOnClickListener {
            startActivity(Intent(this@Test1Activity, Test1PlayActivity::class.java))
        }
    }
}
