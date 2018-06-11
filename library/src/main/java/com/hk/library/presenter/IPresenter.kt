package com.hk.library.presenter


/**
 * Created by Administrator on 2016/12/2.
 */

interface IPresenter {
    fun onSuccess(rs: Any, task:Int)
    fun onError(error: String)
    fun onError(error: Any, task:Int)
    fun showProgress(isShow: Boolean)
}
