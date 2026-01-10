package com.example.feriferi.utils

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

object ImageUtils {

    fun getImageExtension(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType)
    }

    fun isImage(uri: Uri): Boolean {
        return uri.toString().contains("image")
    }
}