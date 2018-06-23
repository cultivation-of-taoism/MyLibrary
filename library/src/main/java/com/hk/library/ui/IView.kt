package com.hk.library.ui

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.view.View

import com.hk.library.view.LoadProgressDialog


/**
 * Created by Administrator on 2016/12/2.
 */

interface IView {
    fun showSuccess(`object`: Any, task:Int)
    fun showError(error: String)
    fun showError(error: Any, task:Int)
    val mContext: Context
    var loadProgressDialog: LoadProgressDialog
    val iiView:View
    val lifecycleOwner: LifecycleOwner
}
