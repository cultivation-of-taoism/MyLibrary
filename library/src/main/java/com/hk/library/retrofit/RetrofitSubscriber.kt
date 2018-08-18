package com.hk.library.retrofit

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.hk.library.presenter.IPresenter
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.subscribers.LambdaSubscriber
import org.reactivestreams.Subscription

open class RetrofitSubscriber<T>(val iPresenter: IPresenter, private val task:Int = 0){
    val lambdaSubscriber = LambdaSubscriber<T>(Consumer{
        onNext(it)
    }, Consumer { onError(it) }, Action { onComplete() }, Consumer { onSubscribe(it) })

    open fun onNext(t: T) {
        checkResult(t)
        iPresenter.onSuccess(t!!,task)
    }

    private fun onError(e: kotlin.Throwable?){
        e?.printStackTrace()
        if (e is ApiException) {
            this.onError(e)
        } else {
            this.onError(RetrofitException.handleException(e))
        }
        this.onComplete()
    }

    open fun onError(p0: ApiException) {
        iPresenter.onError(p0,task)
    }


    open fun onComplete() {
        iPresenter.controlProgress(false)
    }

    open fun onSubscribe(s: Subscription) {
        s.request(Long.MAX_VALUE)
        onStart(s)
    }

    open fun onStart(s: Subscription) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort("似乎没网O")
            onComplete()
            s.cancel()
            return
        }
        iPresenter.controlProgress(true)
    }

    @Throws(Throwable::class)
    open fun checkResult(t: T){}

}