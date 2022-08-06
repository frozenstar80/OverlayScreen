package com.example.overlayscreen.utils

import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.overlayscreen.service.FloatingWindowService

class Constants {

    //for checking FloatingWindowService is running  or not

  fun isServiceRunning(activity: AppCompatActivity): Boolean {
        val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (FloatingWindowService::class.java.name == service.service.className)
                return true
        }
        return false
    }

    //for requesting overlay permission
     fun requestOverlayDisplayPermission(activity: AppCompatActivity) {
        lateinit var dialog: AlertDialog
        val builder = AlertDialog.Builder(activity)

        builder.setCancelable(true)

        builder.setTitle("Screen Overlay Permission Needed")

        builder.setMessage("Enable 'Display over other apps' from System Settings.")

        builder.setPositiveButton(
            "Open Settings",
            DialogInterface.OnClickListener { dialog, which ->

                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${activity.packageName}")
                )


                activity.startActivityForResult(intent, AppCompatActivity.RESULT_OK)
            })

        dialog = builder.create()
        dialog.show()
    }

    //checking for overlay permission
  fun checkOverlayDisplayPermission(activity: AppCompatActivity): Boolean {

        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(activity)
        } else {
            true
        }
    }
}