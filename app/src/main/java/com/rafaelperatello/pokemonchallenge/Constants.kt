package com.rafaelperatello.pokemonchallenge

internal object ImageConstants {

    object Cache {

        const val DIRECTORY = "image_cache"
        const val DISK_SIZE = 200 * 1024 * 1024L
        const val MEMORY_PERCENT = 0.2
    }
}

internal object NetworkConstants {

    object Cache {

        const val DIRECTORY = "http_cache"
        const val SIZE = 20 * 1024 * 1024
    }
}

internal object SettingsModuleConstants {

    const val DATA_STORE_PREFERENCES_NAME = "app_preferences"
}