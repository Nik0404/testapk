package com.acroninspector.app.common.utils

import android.content.Context
import android.hardware.camera2.CameraManager

class FlashlightManager(private val context: Context) {

    private lateinit var cameraManager: CameraManager
    private var isFlashlightOn = false

    init {
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    fun toggleFlashlight() {
        try {
            val cameraListId = cameraManager.cameraIdList[0]
            if (!isFlashlightOn) {
                cameraManager.setTorchMode(cameraListId, true)
                isFlashlightOn = true
            } else {
                cameraManager.setTorchMode(cameraListId, false)
                isFlashlightOn = false
            }
        } catch (e: Exception) {

        }
    }
}