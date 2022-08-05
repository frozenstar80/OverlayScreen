package com.example.overlayscreen.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import com.example.overlayscreen.databinding.FloatingLayoutBinding


class FloatingWindowService : Service() {
    lateinit var floatWindowLayoutParam: WindowManager.LayoutParams
    private var LAYOUT_TYPE: Int? = null
    lateinit var windowManager: WindowManager
    lateinit var binding: FloatingLayoutBinding

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()

        val metrics = applicationContext.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels


        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FloatingLayoutBinding.inflate(inflater)

        LAYOUT_TYPE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // If API Level is more than 26, we need TYPE_APPLICATION_OVERLAY
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {

            WindowManager.LayoutParams.TYPE_TOAST
        }

        //set the size of floating screen to half width and full height
        floatWindowLayoutParam = WindowManager.LayoutParams(
            (width * 0.50f).toInt(), (height * 1.00f).toInt(),
            LAYOUT_TYPE!!,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        floatWindowLayoutParam.gravity = Gravity.CENTER

        floatWindowLayoutParam.x = 0
        floatWindowLayoutParam.y = 0

        windowManager.addView(binding.root, floatWindowLayoutParam)


/*
Stop FloatingWindow Service and close the window
 */
        binding.close.setOnClickListener {
            stopSelf()

            windowManager.removeView(binding.root)

        }

        /*
        Drag floating screens to anywhere on main screen
         */
        binding.root.setOnTouchListener(object : View.OnTouchListener {
            val floatWindowLayoutUpdateParam: WindowManager.LayoutParams = floatWindowLayoutParam
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = floatWindowLayoutUpdateParam.x.toDouble()
                        y = floatWindowLayoutUpdateParam.y.toDouble()

                        px = event.rawX.toDouble()

                        py = event.rawY.toDouble()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        floatWindowLayoutUpdateParam.x = (x + event.rawX - px).toInt()
                        floatWindowLayoutUpdateParam.y = (y + event.rawY - py).toInt()

                        windowManager.updateViewLayout(binding.root, floatWindowLayoutUpdateParam)
                    }
                }
                return false
            }
        })


    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        // Window is removed from the screen at onDestroy()
        windowManager.removeView(binding.root)
    }

}