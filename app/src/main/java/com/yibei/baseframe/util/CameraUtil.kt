package com.yibei.baseframe.util

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.blankj.utilcode.util.LogUtils
import java.io.ByteArrayOutputStream


/**
 * Create by YiBei on 2019/7/16
 * Description : 静默拍照工具类
 */
class CameraUtil(val context: Activity) {
    private val cameraTag = "CameraUtil"
    private var mCurrentCamIndex = 0
    private var fileName = ""
    private var mFrontCamera: Camera? = null
    private var previewing: Boolean = true
    private var canTake = false
    private lateinit var mMessageReceiver: MessageReceiver

    init {
        registerMessageReceiver()
    }


    private fun openFrontFacingCameraGingerbread(): Camera? {
        val cameraCount: Int = Camera.getNumberOfCameras()
        var cam: Camera? = null
        val cameraInfo = Camera.CameraInfo()

        for (camIdx in 0 until cameraCount) {
            Camera.getCameraInfo(camIdx, cameraInfo)
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx)
                    mCurrentCamIndex = camIdx
                } catch (e: RuntimeException) {
                    LogUtils.e(cameraTag, "Camera failed to open: " + e.localizedMessage)
                }

            }
        }

        return cam
    }


    inner class SurfaceViewCallback : SurfaceHolder.Callback {

        override fun surfaceChanged(arg0: SurfaceHolder, arg1: Int, w: Int, h: Int) {
            try {
                if (previewing) {
                    mFrontCamera!!.stopPreview()
                    previewing = false
                }
                mFrontCamera!!.setPreviewDisplay(arg0)
                val parameters = mFrontCamera!!.parameters
                val size = getBestPreviewSize(w, h)
                LogUtils.e("camera size  ------>   ${size!!.width}     ${size.height}")
                parameters.setPreviewSize(size!!.width, size.height)
//                parameters.setPreviewSize(size!!.width, size.height)
                for (mode in parameters.supportedFocusModes) {
                    if (mode == Camera.Parameters.FOCUS_MODE_AUTO) {
                        parameters.focusMode = mode
                        break
                    }
                }
//                parameters.setPreviewSize(720, 720)
//                view.layoutParams.apply {
//                    width = size.width
//                    height = size.height
//                }
                mFrontCamera!!.parameters = parameters
                mFrontCamera!!.startPreview()
                previewing = true
                setCameraDisplayOrientation(context, mCurrentCamIndex, mFrontCamera!!)
            } catch (e: Exception) {
            }

        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            //				mFrontCamera = Camera.open();
            //change to front camera
            try {
                mFrontCamera = openFrontFacingCameraGingerbread()
                // get Camera parameterssaveMyBitmap
                val params = mFrontCamera!!.parameters

                val focusModes = params.supportedFocusModes
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    // Autofocus mode is supported
                }
                mFrontCamera!!.setPreviewCallback { bytes, camera ->
                    if (canTake) {
                        LogUtils.e(
                            cameraTag,
                            "  ----------------------------------------  onPreviewFrame $canTake             ---------------------------- ------- "
                        )
                        getSurfacePic(bytes, camera)
                        canTake = false
                    }
                }
            } catch (e: Exception) {
                LogUtils.e(
                    cameraTag,
                    "  ----------------------------------------  Exception ${e.message}             ---------------------------- ------- "
                )
                e.printStackTrace()
            }

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            LogUtils.e(
                cameraTag,
                "  ----------------------------------------  surfaceDestroyed     ---------------------------- ------- "
            )
            if (mFrontCamera != null) {
                mFrontCamera!!.stopPreview()
                mFrontCamera!!.setPreviewCallback(null)
                mFrontCamera!!.release()
                mFrontCamera = null
                previewing = false
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver)
        }


    }


    fun getSurfacePic(data: ByteArray, camera: Camera) {
        val size = camera.parameters.previewSize
        val image = YuvImage(data, ImageFormat.NV21, size.width, size.height, null)
        if (image != null) {
            val stream = ByteArrayOutputStream()
            image.compressToJpeg(Rect(0, 0, size.width, size.height), 80, stream)

            val bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())

            //**********************
            //因为图片会放生旋转，因此要对图片进行旋转到和手机在一个方向上
            fileName = BitmapUtil.rotateMyBitmap(bmp)
            //**********************************
            LogUtils.e("hhh", "file name is $fileName")
            val msgIntent = Intent("com.fubang.fish.MESSAGE_PHOTO").apply {
                putExtra("pic", fileName)
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent)
        }
        LogUtils.e(
            cameraTag,
            "  ----------------------------------------  getSurfacePic              ---------------------------- ------- "
        )
    }


    private fun setCameraDisplayOrientation(activity: Activity, cameraId: Int, camera: Camera) {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        val rotation = activity.windowManager.defaultDisplay.rotation
        LogUtils.e("camera rotation is ---->$rotation")

        //degrees  the angle that the picture will be rotated clockwise. Valid values are 0, 90, 180, and 270.
        //The starting position is 0 (landscape).
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var result: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360  // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360
        }
        camera.setDisplayOrientation(result)
    }

    private fun registerMessageReceiver() {
        mMessageReceiver = MessageReceiver()
        val filter = IntentFilter()
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        filter.addAction("com.fubang.fish.CameraUtil")
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, filter)
    }

    inner class MessageReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            LogUtils.e(
                cameraTag,
                "  ----------------------------------------  onReceive Common   ---------------------------- ------- "
            )

            if ("com.fubang.fish.CameraUtil" == intent.action) {
                LogUtils.e(
                    cameraTag,
                    "  ----------------------------------------  onReceive   ---------------------------- ------- "
                )
                canTake = true
            }

        }
    }


    private fun getBestPreviewSize(width: Int, height: Int): Camera.Size? {
        //在下面叙述
        var result: Camera.Size? = null
        val p = mFrontCamera!!.parameters
        //特别注意此处需要规定rate的比是大的比小的，不然有可能出现rate = height/width，但是后面遍历的时候，current_rate = width/height,所以我们限定都为大的比小的。
        val rate = Math.max(width, height).toFloat() / Math.min(width, height).toFloat()
        var tmp_diff: Float
        var min_diff = -1f
        for (size in p.supportedPreviewSizes) {
//            if(size.width>size.height){
//                val w=size.width
//                val h=size.height
//                size.width=h
//                size.height=w
//            }
            val current_rate = size.width.coerceAtLeast(size.height).toFloat() / Math.min(
                size.width,
                size.height
            ).toFloat()
            tmp_diff = Math.abs(current_rate - rate)
            if (min_diff < 0) {
                min_diff = tmp_diff
                result = size
            }
            if (tmp_diff < min_diff) {
                min_diff = tmp_diff
                result = size
            }
        }
        return result
    }
}