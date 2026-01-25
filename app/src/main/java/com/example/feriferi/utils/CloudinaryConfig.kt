package com.example.feriferi.utils

import android.content.Context
import com.cloudinary.android.MediaManager

object CloudinaryConfig {

    fun init(context: Context) {

        // Prevent re-initialization (IMPORTANT)
        if (MediaManager.get() != null) return

        val config = hashMapOf(
            "cloud_name" to "dizcwwcat"
        )

        MediaManager.init(context, config)
    }
}