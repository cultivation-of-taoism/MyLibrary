package com.hk.webview_library

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.UriUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import java.io.File

class HKWebChromeClient: WebChromeClient() {
    lateinit var mUploadMessage: ValueCallback<Array<Uri>>
    private val imageDialog = ChooseImageDialog()
    lateinit var fragmentManager: FragmentManager
    override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
        val activity = view.context as Activity
        activity.runOnUiThread {
            AlertDialog.Builder(activity)
                    .setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定") { _, _ ->
                        view.reload()//重写刷新页面
                    }
                    .setNegativeButton("取消", null)
                    .show()
        }
        result.confirm()//这里必须调用，否则页面会阻塞造成假死
        return true
    }

    override fun onShowFileChooser(webView: WebView, filePathCallback:
    ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
        // Double check that we don't have any existing callbacks
        LogUtils.v("HKWebChromeClient->onShowFileChooser")
        webView.context as Activity
        mUploadMessage = filePathCallback
        imageDialog.mUploadMessage = filePathCallback
        imageDialog.show(fragmentManager, "")
        return true
    }


    /**
     * HINT 实现webView的文件选择时，无论选择成功还是失败都要回调ValueCallback<Array<Uri>>.onReceiveValue(Array<Uri>)
     * HINT 成功时返回为对应文件的uri数组，失败或取消时返回为null 如果不进行回调的话会导致onShowFileChooser只触发一次
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        LogUtils.v("HKWebChromeClient->onActivityResult")
        when (requestCode) {
            PictureConfig.CHOOSE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val image = PictureSelector.obtainMultipleResult(data).firstOrNull()?.cutPath
                    LogUtils.v(image)
                    if (image == null)mUploadMessage.onReceiveValue(null)
                    image?.let {
                        val uri = UriUtils.file2Uri(File(it))
                        mUploadMessage.onReceiveValue(arrayOf(uri))
                    }
                } else mUploadMessage.onReceiveValue(null)
            }

        }
    }


}