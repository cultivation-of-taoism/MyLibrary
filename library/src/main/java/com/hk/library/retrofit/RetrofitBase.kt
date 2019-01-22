package com.hk.library.retrofit

import android.arch.lifecycle.Lifecycle
import com.blankj.utilcode.util.LogUtils
import com.hk.library.presenter.IPresenter
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


/**
 * Created by 23641 on 2017/6/15.
 */
class RetrofitBase {
    companion object{
        val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
        val retrofitBuilder = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())

    }
}

open class RetrofitErrorBase(private val iPresenter: IPresenter, private val task:Int = 0): Consumer<Throwable> {
    override fun accept(e: Throwable) {
        e.printStackTrace()
        when (e) {
            is HttpException -> iPresenter.onError("网络错误!\n错误码:${e.code()}",task)
            is ConnectException -> iPresenter.onError("连接失败，网络不可用！", task)
            is SocketTimeoutException -> iPresenter.onError("网络错误，连接超时！", task)
            is IOException -> iPresenter.onError("连接失败!\n错误信息:${e.localizedMessage ?: "服务端无响应"}",
                    task)
            is ApiException -> iPresenter.onError(e,task)
            else -> iPresenter.onError("错误类型:${e::class.java.name}\n错误信息:${e.localizedMessage}",task)
        }

    }
}
/**
 * rxjava异步简写
 */
fun <T> Flowable<T>.enqueue(subscribe: RetrofitSubscriber<T>): Disposable{
    subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext{ subscribe.checkResult(it) }
            .doOnCancel { LogUtils.v("取消网络请求订阅！") }
            .bindUntilEvent(subscribe.iPresenter.lifecycleOwner, Lifecycle.Event.ON_DESTROY)//hint
            // RxLifeCycle不能在stop生命周期之后执行
            .subscribe(subscribe.lambdaSubscriber)
    return subscribe.lambdaSubscriber
}