package com.crte.monitoring

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.crte.monitoring.test4.*
import kotlinx.android.synthetic.main.activity_test.*

class MainActivity : BaseActivity() {
    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    var isCheckPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        var ipString = SharedPreferencUtil.getValue("ip","") as String
        var portString = SharedPreferencUtil.getValue("port","") as String
        ip.setText(ipString)
        port.setText(portString)

        record.setOnClickListener {
            if (TextUtils.isEmpty(ip.text.toString()) || TextUtils.isEmpty(port.text.toString())) {
                Toast.makeText(this, "服务器地址、端口号不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(group.text.toString())) {
                Toast.makeText(this, "编组不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            App.groupId = group.text.toString().toInt()
            Content.host = "http://${ip.text.toString()}:${port.text.toString()}"
            saveData()
            if (!isCheckPermission) {
                Toast.makeText(this, "尚未设置请求权限", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this, Test4RecordActivity::class.java))
        }

        play.setOnClickListener {
            if (TextUtils.isEmpty(ip.text.toString()) || TextUtils.isEmpty(port.text.toString())) {
                Toast.makeText(this, "服务器地址、端口号不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(group.text.toString())) {
                Toast.makeText(this, "编组不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            App.groupId = group.text.toString().toInt()
            Content.host = "http://${ip.text.toString()}:${port.text.toString()}"
            if (!isCheckPermission)
                Toast.makeText(this, "尚未设置请求权限", Toast.LENGTH_SHORT).show()
            else
                startActivity(Intent(this, Test4PlayActivity::class.java))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (p in permissions) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, 101)
                    return
                }
            }
        }
        isCheckPermission = true
    }

    private fun saveData() {
        SharedPreferencUtil.putValue("ip",ip.text.toString())
        SharedPreferencUtil.putValue("port",port.text.toString())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 101) {
            for (i in grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    return
                }
            }
            isCheckPermission = true
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}