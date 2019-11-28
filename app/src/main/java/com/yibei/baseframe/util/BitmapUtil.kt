package com.yibei.baseframe.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import java.io.*


/**
 * Create by YiBei on 2019/6/17
 * Description :Bitmap图像处理工具类
 */
object BitmapUtil{

    //保存图片
    fun saveMyBitmap(mBitmap: Bitmap):String {
        val fileName = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .toString()
                + File.separator
                + "PicTest_" + System.currentTimeMillis() + ".jpg")
        val file = File(fileName)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdir()
        }
        var fOut: FileOutputStream? = null
        try {
            fOut = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        try {
            if (null != fOut) {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.flush()
                fOut.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return fileName
    }

    //旋转图片
    fun rotateMyBitmap(bmp: Bitmap):String {
        //*****旋转一下
        val matrix = Matrix()
        matrix.postRotate(270f)

        val bitmap = Bitmap.createBitmap(bmp.width, bmp.height, Bitmap.Config.RGB_565)

        val nbmp2 = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)

        return saveMyBitmap(compressImage(nbmp2)!!)

        //*******显示一下
        // imgview.setImageBitmap(nbmp2)

    }


    //压缩图片
    fun compressImage(image: Bitmap): Bitmap? {
        val baos = ByteArrayOutputStream()
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        val isBm = ByteArrayInputStream(baos.toByteArray())
        // 把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null)
    }

}