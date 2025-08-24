package com.kuro.notiflow.data.data_source

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

object ListStringConverter {
    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    @TypeConverter
    @JvmStatic
    fun listStringToJson(list: List<String>?): String? =
        list?.let { json.encodeToString(ListSerializer(String.serializer()), it) }

    @TypeConverter
    @JvmStatic
    fun jsonToListString(s: String?): List<String>? =
        s?.let { json.decodeFromString(ListSerializer(String.serializer()), it) }
}