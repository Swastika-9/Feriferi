package com.example.feriferi

import android.app.Application
import com.cloudinary.android.MediaManager

class FeriferiApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Cloudinary
        val config = HashMap<String, String>()
        config["cloud_name"] = "dizcwwcat"
        config["api_key"] = "934843177742589"
        config["api_secret"] = "txri4GAHnxok5sBY0pB2gdiGMw4"

        MediaManager.init(this, config)
    }
}