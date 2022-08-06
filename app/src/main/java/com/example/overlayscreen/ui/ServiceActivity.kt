package com.example.overlayscreen.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.overlayscreen.R
import com.example.overlayscreen.service.FloatingWindowService
import com.example.overlayscreen.utils.Constants

class ServiceActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        if (Constants().isServiceRunning(this)) {
            stopService(Intent(this, FloatingWindowService::class.java))
        }

            if (Constants().checkOverlayDisplayPermission(this)) {
                startService(Intent(this, FloatingWindowService::class.java))
            } else {
                Constants().requestOverlayDisplayPermission(this)
            }

    }
}