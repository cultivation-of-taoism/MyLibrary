package com.hk.library.retrofit

import com.google.gson.Gson
import com.hk.library.retrofit.HKConverterFactory.Companion.MEDIA_TYPE
import com.hk.library.retrofit.HKConverterFactory.Companion.UTF_8
import okhttp3.RequestBody
import retrofit2.Converter

class MapRequestBodyConverter : Converter<Map<String, *>, RequestBody>{
    private val gson = Gson()
    override fun convert(value: Map<String, *>?): RequestBody {
        val json = gson.toJson(value)
        return RequestBody.create(MEDIA_TYPE, json.toByteArray(UTF_8))
    }
}