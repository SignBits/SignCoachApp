package com.SDP.signbits

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2

class CameraAPI constructor(context: Context){
    companion object{
        @Volatile
        private var INSTANCE:CameraAPI? = null
        fun getInstance(context: Context) =
            CameraAPI.INSTANCE ?: synchronized(this) {
                CameraAPI.INSTANCE ?: CameraAPI(context).also { INSTANCE = it }
            }
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    /** A safe way to get an instance of the Camera object. */
    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }
}