package com.example.overlayscreen.ui

import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.example.overlayscreen.R
import com.example.overlayscreen.service.FloatingWindowService

class ServiceActivity : AppCompatActivity() {
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        if (isServiceRunning()) {
            stopService(Intent(this, FloatingWindowService::class.java))
        }

            if (checkOverlayDisplayPermission()) {
                startService(Intent(this, FloatingWindowService::class.java))
            } else {
                requestOverlayDisplayPermission()
            }

    }

    //for checking FloatingWindowService is running  or not

    private fun isServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (FloatingWindowService::class.java.name == service.service.className)
                return true
        }
        return false
    }

    //for requesting overlay permission
    private fun requestOverlayDisplayPermission() {
        val builder = AlertDialog.Builder(this)

        builder.setCancelable(true)

        builder.setTitle("Screen Overlay Permission Needed")

        builder.setMessage("Enable 'Display over other apps' from System Settings.")

        builder.setPositiveButton(
            "Open Settings",
            DialogInterface.OnClickListener { dialog, which ->

                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )


                startActivityForResult(intent, RESULT_OK)
            })

        dialog = builder.create()
        dialog.show()
    }

    //checking for overlay permission
    private fun checkOverlayDisplayPermission(): Boolean {

        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }
}