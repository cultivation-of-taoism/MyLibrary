package com.hk

import com.google.gson.Gson
import com.hk.library.Model
import com.hk.library.retrofit.RetrofitBase
import com.hk.library.retrofit.RetrofitErrorBase
import com.hk.library.presenter.IPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rundi.investmentadviser.common.CustomGsonConverterFactory
import rundi.investmentadviser.entity.DataResult
import rundi.investmentadviser.entity.Token

/**
 * Created by 23641 on 2017/10/9.
 */
class ApiModel(val iPresenter:IPresenter): Model(iPresenter) {
    companion object {
        val retrofit = RetrofitBase.retrofitBuilder
                .addConverterFactory(CustomGsonConverterFactory.create(Gson()))
                .baseUrl(HKApp.URL_BASE)
                .build()
        val service = retrofit.create(APIService::class.java)
    }
    interface APIService{
        @POST(HKApp.URL_REGISTER_TOKEN)
        @FormUrlEncoded
        fun registerToken(@Field("serial") serial:String): Observable<DataResult<Token>>
    }
    fun registerToken(serial: String){
        service.registerToken(serial).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer{
                    it->iPresenter.onSuccess(it.data!!,Task.REGISTER_TOKEN)
                }, RetrofitErrorBase(iPresenter, Task.REGISTER_TOKEN))
    }

    object Task {
        const val REGISTER_TOKEN = 1
    }
}