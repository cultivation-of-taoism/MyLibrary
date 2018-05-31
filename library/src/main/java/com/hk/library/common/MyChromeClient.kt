package com.qianmo.stampcoin.common

import android.webkit.WebChromeClient
import android.webkit.WebView
import com.hk.library.ui.BaseActivity
import com.hk.library.view.LoadProgressDialog

/**
 * Created by Administrator on 2018/3/21.
 */
class MyChromeClient(val onTitleReceive: (String)->Unit) : WebChromeClient() {
    override fun onReceivedTitle(view: WebView, title: String) {
        onTitleReceive(title)
        super.onReceivedTitle(view, title)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        view?.let {
            (it.context as BaseActivity).loadProgressDialog.dismiss()
        }
        super.onProgressChanged(view, newProgress)
    }
}