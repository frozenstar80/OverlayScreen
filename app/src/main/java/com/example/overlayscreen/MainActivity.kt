package com.example.overlayscreen

import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.overlayscreen.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
  private lateinit var dialog: AlertDialog
  lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isServiceRunning()){
            stopService(Intent(this@MainActivity,FloatingWindowApp::class.java))
        }
        binding.launchLink.setOnClickListener {
            if(checkOverlayDisplayPermission()){
                startService(Intent(this@MainActivity,FloatingWindowApp::class.java))
            }else{
                requestOverlayDisplayPermission()
            }
        }


    }

    //for checking FloatingWindowService is running  or not

   private fun isServiceRunning():Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (FloatingWindowApp::class.java.name == service.service.className)
                return true
        }
        return false
    }

    private fun requestOverlayDisplayPermission() {
        val builder = AlertDialog.Builder(this)

        builder.setCancelable(true)

        builder.setTitle("Screen Overlay Permission Needed")

        builder.setMessage("Enable 'Display over other apps' from System Settings.")

        builder.setPositiveButton("Open Settings", DialogInterface.OnClickListener { dialog, which ->

                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )


                startActivityForResult(intent, RESULT_OK)
            })

        dialog = builder.create()
        dialog.show()
    }

    private fun checkOverlayDisplayPermission(): Boolean {

        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }
}