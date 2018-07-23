package com.hk.webview_library

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AlertDialog
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

class HKWebViewClient: WebViewClient() {
    lateinit var progress: View
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        LogUtils.v(url)
        // 获取上下文, H5PayDemoActivity为当前页面
        val context = view.context
        // ------  对alipays:相关的scheme处理 -------
        if (url.startsWith("alipays:") || url.startsWith("alipay")) {
            try {
                context.startActivity(Intent("android.intent.action.VIEW", Uri.parse(url)))
            } catch (e: Exception) {
                AlertDialog.Builder(context)
                        .setMessage("未检测到支付宝客户端，请安装后重试。")
                        .setPositiveButton("立即安装") { dialog, which ->
                            val alipayUrl = Uri.parse("https://d.alipay.com")
                            context.startActivity(Intent("android.intent.action.VIEW", alipayUrl))
                        }.setNegativeButton("取消", null).show()
            }

            return true
        }
        // ------- 处理结束 -------

        if (!(url.startsWith("http") || url.startsWith("https"))) {
            return true
        }
        //Android8.0以下的需要返回true 并且需要loadUrl；8.0之后效果相反
        if (Build.VERSION.SDK_INT < 26) {
            view.loadUrl(url)
            return true
        }
        return false
    }

    override fun onPageFinished(p0: WebView?, p1: String?) {
        progress.visibility = View.GONE
    }

}