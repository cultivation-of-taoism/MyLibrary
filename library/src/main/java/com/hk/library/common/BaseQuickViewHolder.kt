package com.qianmo.stampcoin.common

import android.content.Context
import android.support.v7.widget.TintContextWrapper
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import com.hk.library.ui.BaseActivity
import com.hk.library.ui.IView
import com.hk.library.view.LoadProgressDialog

/**
 * Created by 23641 on 2017/10/16.
 */
open class BaseQuickViewHolder(itemView: View):BaseViewHolder(itemView),IView {
    override val iiView: View = itemView
    override lateinit var loadProgressDialog: LoadProgressDialog
    override val mContext: Context = itemView.context
    protected val activity:BaseActivity
    init {
        activity = when (mContext) {
            is TintContextWrapper -> (mContext as TintContextWrapper).baseContext as BaseActivity
            else -> (mContext as BaseActivity)
        }
        loadProgressDialog = activity.loadProgressDialog

    }

    override fun showError(error: Any, task: Int) {
        showError(error.toString())
    }

    override fun showError(error: String) {
        activity.showError(error)
    }

    override fun showSuccess(`object`: Any, task: Int) {

    }

}