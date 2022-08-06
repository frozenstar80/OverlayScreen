package com.example.overlayscreen.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.overlayscreen.databinding.ActivityMainBinding
import com.example.overlayscreen.service.FloatingWindowService
import com.example.overlayscreen.utils.Constants


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Constants().isServiceRunning(this)) {
            stopService(Intent(this@MainActivity, FloatingWindowService::class.java))
        }
        binding.launchLink.setOnClickListener {
            if (Constants().checkOverlayDisplayPermission(this)) {
                startService(Intent(this@MainActivity, FloatingWindowService::class.java))
            } else {
                Constants().requestOverlayDisplayPermission(this)
            }
        }


    }
}