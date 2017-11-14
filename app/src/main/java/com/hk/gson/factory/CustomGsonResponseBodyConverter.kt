package rundi.investmentadviser.common

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.hk.HKApp
import com.hk.library.ApiException
import okhttp3.ResponseBody
import okhttp3.internal.Util.UTF_8
import retrofit2.Converter
import rundi.investmentadviser.entity.BaseResult
import rundi.investmentadviser.entity.DataResult
import rundi.investmentadviser.entity.ErrorResult
import rundi.investmentadviser.model.ApiError
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by 23641 on 2017/6/28.
 */

class CustomGsonResponseBodyConverter<T>(private val gson: Gson, private val adapter:
TypeAdapter<T>) : Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val response = value.string()
        LogUtils.json(HKApp.TAG, response)
        val httpStatus = gson.fromJson(response, BaseResult::class.java)
        if (httpStatus.isError()) {
            val errorResult = gson.fromJson(response, ErrorResult::class.java)
            value.close()
            throw ApiException(errorResult.code, errorResult.data)
        } else if (httpStatus.type == 2) {//如果是列表数据
            val listRs = gson.fromJson<DataResult<List<Any>>>(response, object : TypeToken<DataResult<List<Any>>>() {
            }.type)
            if (listRs.data == null || listRs.data!!.isEmpty()) {//如果列表数据个数为0或为空
                if (listRs.page != null && listRs.page!!.total > 0)
                    throw ApiException(ApiError.CODE_NO_MORE_DATA,
                            "没有更多数据了!")
                else
                    throw ApiException(ApiError.CODE_NO_DATA, "暂无数据！")
            }
        }

        val contentType = value.contentType()
        val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
        val inputStream = ByteArrayInputStream(response.toByteArray())
        val reader = InputStreamReader(inputStream, charset!!)
        val jsonReader = gson.newJsonReader(reader)

        value.use {
            return adapter.read(jsonReader)
        }
    }
}
