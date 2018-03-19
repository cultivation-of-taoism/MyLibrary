package rundi.investmentadviser.retrofit

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by 23641 on 2017/10/18.
 * 用于显示文件上传和下载进度的拦截器
 */
class FileInterceptor(private val progressListener: ProgressListener): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body()
        if (requestBody!=null){
            val fileBody = FileRequestBody(requestBody,progressListener)
            return chain.proceed(request.newBuilder().post(fileBody).build())
        }

        val response = chain.proceed(request)
        val responseBody = response.body()
        if (responseBody!=null){
            val fileBody = FileResponseBody(responseBody,progressListener)
            return response.newBuilder().body(fileBody).build()
        }
        return response
    }
}