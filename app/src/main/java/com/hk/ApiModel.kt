package com.hk

import com.hk.entity.TResult
import com.hk.library.Model
import com.hk.library.common.RefreshHelper.Companion.PAGE_SIZE
import com.hk.library.presenter.IPresenter
import com.hk.library.retrofit.ApiException
import com.hk.library.retrofit.HKRetrofit
import com.hk.library.retrofit.RetrofitSubscriber
import com.hk.library.retrofit.enqueue
import com.lingniu.vestbag.entity.Information
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rundi.investmentadviser.entity.DataResult
import rundi.investmentadviser.entity.Token

/**
 * Created by 23641 on 2017/10/9.
 */
class ApiModel(val iPresenter:IPresenter): Model(iPresenter) {
    val retrofit = HKRetrofit.getDefaultRetrofit(HKApp.URL_BASE)
    val service = retrofit.create(APIService::class.java)
    interface APIService{
        @POST(HKApp.URL_REGISTER_TOKEN)
        @FormUrlEncoded
        fun registerToken(@Field("serial") serial:String): Flowable<DataResult<Token>>
        @POST(HKApp.URL_INFORMATION)
        fun getInformationList(@Body param:Map<String, String>): Flowable<TResult<List<Information>>>
    }
    fun registerToken(serial: String){
        service.registerToken(serial).
                enqueue(MySubscribe(iPresenter, Task.REGISTER_TOKEN))
    }
    fun getInformationList(page: Int){
        val service = HKRetrofit.getDefaultRetrofit(HKApp.URL_BASE_1).create(APIService::class.java)
        val param = mapOf("currentPage" to "$page","pageSize" to "$PAGE_SIZE", "classId" to
                "1001")
        service.getInformationList(param)
                .enqueue(object : RetrofitSubscriber<TResult<List<Information>>>(iPresenter, Task.INFORMATION.ordinal){
                    override fun checkResult(t: TResult<List<Information>>) {
                        if (t.code != 100) throw ApiException(t.code, t.msg)
                    }

                    override val isList: Boolean
                        get() = true

                })
    }

    enum class Task {
        REGISTER_TOKEN,
        INFORMATION
    }

    class MySubscribe<T>(iPresenter:IPresenter, task: Task) : RetrofitSubscriber<T>
    (iPresenter,task.ordinal){
        override fun checkResult(t: T) {
            if (t is DataResult<*>)
                if (t.code!=200) throw ApiException(t.code, "服务器错误！")
        }
    }
}