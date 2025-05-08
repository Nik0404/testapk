package com.acroninspector.app.common.utils

import android.app.Activity
import android.content.Context
import android.view.Window
import com.acroninspector.app.presentation.mvp.BaseActivity

class BrightnessRegulator(private val context: Context): BaseActivity() {

    private var brightness = 0.5f

    fun increaseBrightness() {
        brightness += 0.1f
        if (brightness > 1.0f) {
            brightness = 1.0f
        }
        updateBrightness()
    }

    fun setBrightness(value: Float) {
        brightness = value
        updateBrightness()
    }


    fun decreaseBrightness() {
        brightness -= 0.1f
        if (brightness < 0.0f) {
            brightness = 0.0f
        }
        updateBrightness()
    }

    private fun updateBrightness() {
        val window = (context as? Activity)?.window
        if (window != null) {
            val layoutParams = window.attributes
            layoutParams.screenBrightness = brightness
            window.attributes = layoutParams
        }
    }
}