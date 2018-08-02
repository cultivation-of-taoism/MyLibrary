package com.hk.library.common

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.hk.library.ui.BaseActivity
import com.hk.library.ui.IView
import com.hk.library.view.LoadProgressDialog

/**
 * Created by 23641 on 2017/9/11.
 */
open class BaseDialogFragment: DialogFragment(), IView {
    override fun showProgress(isShow: Boolean) {
        if (isShow) loadProgressDialog.show()
        else loadProgressDialog.dismiss()
    }

    override val lifecycleOwner: LifecycleOwner
        get() = this
    override val iiView: View
        get() = dialog.window.decorView
    override val mContext: Context
        get() = activity
    override var loadProgressDialog: LoadProgressDialog
        get() = (activity as BaseActivity).loadProgressDialog
        set(value) {  }


    override fun showError(error: Any, task: Int) {
        showError(error.toString())
    }

    override fun showError(error: String) {
        (activity as BaseActivity).showError(error)
    }

    override fun showSuccess(`object`: Any, task: Int) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Dialog_MinWidth)
        super.onCreate(savedInstanceState)
    }
    fun customWidthAndHeight(width: Int, height: Int){
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setLayout(width, height)
    }
}