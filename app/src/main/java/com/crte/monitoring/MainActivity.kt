package com.crte.monitoring

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crte.monitoring.test4.Test4PlayActivity
import com.crte.monitoring.test4.Test4RecordActivity
import kotlinx.android.synthetic.main.activity_test.*

class MainActivity:AppCompatActivity() {
    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        record.setOnClickListener {
            startActivity(Intent(this, Test4RecordActivity::class.java))
        }

        play.setOnClickListener {
            startActivity(Intent(this, Test4PlayActivity::class.java))
        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            for(p in permissions){
                if(checkSelfPermission(p)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(permissions,101)
                    return
                }
            }
        }
    }
}