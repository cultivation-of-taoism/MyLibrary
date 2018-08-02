package com.hk.library.adapter

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hk.library.ui.BaseActivity
import com.hk.library.ui.IView
import com.hk.library.view.LoadProgressDialog

/**
 * Created by 23641 on 2017/6/15.
 */
open class RecyclerHolder(itemView:View) : RecyclerView.ViewHolder(itemView), IView {
    override fun showProgress(isShow: Boolean) {
        if (isShow) loadProgressDialog.show()
        else loadProgressDialog.dismiss()
    }

    override val lifecycleOwner: LifecycleOwner
        get() = mContext as BaseActivity
    override val iiView: View
        get() = itemView

    open fun setData(position:Int){}
    override fun showSuccess(`object`: Any, task: Int) {
        (mContext as BaseActivity).showSuccess(`object`,task)
    }

    override fun showError(error: String) {
        (mContext as BaseActivity).showError(error)
    }

    override fun showError(error: Any, task: Int) {
        (mContext as BaseActivity).showError(error,task)
    }

    override val mContext: Context = itemView.context
    override var loadProgressDialog: LoadProgressDialog = (mContext as BaseActivity)
            .loadProgressDialog


}