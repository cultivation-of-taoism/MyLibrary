package com.hk.library

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import com.hk.library.ui.BaseActivity
import com.hk.library.ui.IView
import com.hk.library.view.LoadProgressDialog
import kotlin.properties.Delegates

/**
 * Created by Administrator on 2017/1/2.
 */

open class BaseFragment : Fragment(), IView, View.OnClickListener {
    override fun showProgress(isShow: Boolean) {
        if (isShow) loadProgressDialog.show()
        else loadProgressDialog.dismiss()
    }

    override val lifecycleOwner: LifecycleOwner
        get() = this
    protected var mView: View by Delegates.notNull()
    override val iiView: View by lazy { mView }
    @set:JvmName("setBaseActivity")
    @get:JvmName("getBaseActivity")
    protected var activity: BaseActivity by Delegates.notNull()
    override val mContext: Context
        get() = if (context==null)activity
        else context

    override fun onClick(v: View) {

    }

    open protected fun setView() {}

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as BaseActivity
    }

    override fun showSuccess(`object`: Any, task: Int) {

    }


    override fun showError(error: String) {
        activity.showError(error)
    }

    override fun showError(error: Any, task: Int) {
        showError(error.toString())
    }

    override var loadProgressDialog: LoadProgressDialog
    get() = activity.loadProgressDialog
    set(value) = Unit
}
