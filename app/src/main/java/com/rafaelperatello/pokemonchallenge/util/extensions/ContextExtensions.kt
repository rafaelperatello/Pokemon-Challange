package com.rafaelperatello.pokemonchallenge.util.extensions

import android.content.Context
import com.rafaelperatello.pokemonchallenge.ImageConstants
import com.rafaelperatello.pokemonchallenge.NetworkConstants
import java.io.File

internal fun Context.getImageCacheDir(): File {
    cacheDir.resolve(ImageConstants.Cache.DIRECTORY).let {
        if (!it.exists()) it.mkdirs()
        return it
    }
}

internal fun Context.getNetworkCacheDir(): File {
    cacheDir.resolve(NetworkConstants.Cache.DIRECTORY).let {
        if (!it.exists()) it.mkdirs()
        return it
    }
}
