package com.hk.library.retrofit

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.nio.charset.Charset

class HKConverterFactory private constructor() : Converter.Factory() {
    companion object {
        val MEDIA_TYPE = MediaType.parse("application/json")
        val UTF_8 = Charset.forName("UTF-8")
        fun create() = HKConverterFactory()
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        if (type is ParameterizedType){//判断是含有泛型的类型
            if (type.rawType == Map::class.java){//判断主类型是map
                if (type.actualTypeArguments[0] == String::class.java)//判断第一个泛型参数是string类型
                    return MapRequestBodyConverter()
            }
        }
        return null
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return null
    }
}