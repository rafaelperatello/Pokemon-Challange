package com.rafaelperatello.pokemonchallenge.data.repository.local.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

internal class LocalDateTimeConverter {
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): String {
        return localDateTime.toString()
    }

    @TypeConverter
    fun fromString(localDateTime: String?): LocalDateTime {
        return if (localDateTime == null) {
            LocalDateTime.MIN
        } else {
            LocalDateTime.parse(localDateTime)
        }
    }
}
