package com.hk.library.presenter

import android.arch.lifecycle.LifecycleOwner


/**
 * Created by Administrator on 2016/12/2.
 */

interface IPresenter {
    val lifecycleOwner: LifecycleOwner
    fun onSuccess(rs: Any, task:Int)
    fun onError(error: String)
    fun onError(error: Any, task:Int)
    fun showProgress(isShow: Boolean)
}
