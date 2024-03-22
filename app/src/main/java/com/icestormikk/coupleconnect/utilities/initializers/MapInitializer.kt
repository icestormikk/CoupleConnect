package com.icestormikk.coupleconnect.utilities.initializers

import android.content.Context
import com.yandex.mapkit.MapKitFactory


object MapInitializer {
    private var isMapInitialized = false

    fun initialize(apiKey: String, context: Context) {
        if (isMapInitialized) {
            return
        }

        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.setLocale("ru_RU")
        MapKitFactory.initialize(context)
        isMapInitialized = true
    }
}