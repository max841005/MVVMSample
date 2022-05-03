@file:Suppress("unused")

package com.max.mvvmsample.utils

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory.decodeStream
import android.graphics.Matrix
import android.net.Uri.fromFile
import android.os.Build
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import androidx.activity.result.ActivityResult
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.exifinterface.media.ExifInterface.*
import com.max.mvvmsample.data.config.AUTHORITY
import java.io.File
import java.io.IOException

class PhotoUtils(
    context: Context
) {

    companion object {
        const val REQUEST_CODE_CAMERA_WITH_DATA = 0
        const val RESULT_CHOOSE_PHOTO = 1
    }

    private val appContext = context.applicationContext

    private lateinit var currentPhotoFile: File

    lateinit var photoCallback: PhotoCallback

    val pickPhotoIntent = Intent(ACTION_PICK, EXTERNAL_CONTENT_URI)

    fun dealActivityResult(
        result: ActivityResult,
        type: Int
    ) {

        if (result.resultCode != RESULT_OK) return

        when (type) {

            REQUEST_CODE_CAMERA_WITH_DATA -> when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> FileProvider.getUriForFile(appContext, AUTHORITY, currentPhotoFile)
                else -> fromFile(currentPhotoFile)
            }

            RESULT_CHOOSE_PHOTO -> result.data?.data

            else -> null

        }?.let { uri ->

            var inputStream = appContext.contentResolver.openInputStream(uri)

            try {

                if (inputStream != null) {

                    val rotate = when (ExifInterface(inputStream).getAttributeInt(TAG_ORIENTATION, ORIENTATION_NORMAL)) {
                        ORIENTATION_ROTATE_270 -> 270f
                        ORIENTATION_ROTATE_180 -> 180f
                        ORIENTATION_ROTATE_90 -> 90f
                        else -> 0f
                    }

                    inputStream = appContext.contentResolver.openInputStream(uri)

                    val bitmap = decodeStream(inputStream)

                    photoCallback.onPhotoSelected(createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height,
                        Matrix().apply { postRotate(rotate) },
                        true
                    ))
                }
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
            finally {

                inputStream?.run {

                    try {
                        close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun interface PhotoCallback {
        fun onPhotoSelected(bitmap: Bitmap)
    }
}