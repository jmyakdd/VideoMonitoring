package com.crte.monitoring

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crte.monitoring.test1.Test1Activity
import com.crte.monitoring.test2.Test2Activity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity:AppCompatActivity() {
    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test1.setOnClickListener {
            startActivity(Intent(this,Test1Activity::class.java))
        }
        test2.setOnClickListener {
            startActivity(Intent(this, Test2Activity::class.java))
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