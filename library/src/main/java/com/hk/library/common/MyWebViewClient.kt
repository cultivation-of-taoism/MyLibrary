package com.qianmo.stampcoin.common

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.LogUtils
import com.hk.library.view.LoadProgressDialog

/**
 * Created by Administrator on 2018/3/21.
 */
class MyWebViewClient(val dialog: LoadProgressDialog) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        LogUtils.v(url)
        view.loadUrl(url)
        return false
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        dialog.show()
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        dialog.dismiss()
        super.onPageFinished(view, url)
    }
}