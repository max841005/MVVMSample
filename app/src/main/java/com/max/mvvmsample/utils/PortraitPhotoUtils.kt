@file:Suppress("unused")

package com.max.mvvmsample.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import androidx.core.content.FileProvider
import com.max.mvvmsample.data.config.AUTHORITY
import com.theartofdev.edmodo.cropper.CropImage.ActivityResult
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PortraitPhotoUtils(
    context: Context
) {

    private val appContext = context.applicationContext

    private var fileName = ""

    fun getPhotoFileUri(
        isRequest: Boolean
    ): Uri? {

        if (isRequest) fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("'IMG'_yyyyMMdd_HHmmss'.jpg'"))

        return when {
            SDK_INT < N -> Uri.fromFile(getPhotoFile(fileName))
            else -> FileProvider.getUriForFile(appContext, AUTHORITY, getPhotoFile(fileName))
        }
    }

    private fun getPhotoFile(fileName: String) = File(appContext.externalCacheDir, fileName)

    fun getPhotoBitmap(
        result: ActivityResult
    ): Bitmap? {

        var resultBitmap: Bitmap? = null

        runCatching {
            appContext.contentResolver.openInputStream(result.uri)
        }.onSuccess {

            resultBitmap = BitmapFactory.decodeStream(it)

            it?.close()

        }.onFailure {
            it.printStackTrace()
        }

        return resultBitmap
    }

}