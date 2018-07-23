package com.hk.webview_library

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.WebView
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import tbsplus.tbs.tencent.com.tbsplus.TbsPlusMainActivity


@RuntimePermissions
abstract class MainActivity : TbsPlusMainActivity() {
    abstract val url: String
    private val hkWebChromeClient = HKWebChromeClient()
    lateinit var progress: View

    override fun onCreate(savedInstanceState: Bundle?) {
        initDataWithPermissionCheck()
        intent = Intent().apply {
            val var4 = Bundle()
            var4.putString("url", url)
            var4.putInt("screenorientation", 0)
            putExtra("data", var4)
        }
        super.onCreate(savedInstanceState)
        val container = findViewById<FrameLayout>(R.id.mywebview)
        progress = View.inflate(this, R.layout.layout_progress, null)
        progress.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                ConvertUtils.dp2px(60f)
        ).apply {
            gravity = Gravity.CENTER
        }
        container.addView(progress)
        findViewById<LinearLayout>(R.id.toolbar1).visibility = View.GONE
        hkWebChromeClient.apply {
            fragmentManager = supportFragmentManager
        }
        val webView = container.getChildAt(0) as WebView
        webView.webChromeClient = hkWebChromeClient
        webView.webViewClient = HKWebViewClient().apply { progress = this@MainActivity.progress }
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
    fun initData() {
        // 在调用TBS初始化、创建WebView之前进行如下配置，以开启优化方案
        QbSdk.initTbsSettings(mutableMapOf(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true) as Map<String, Any>)
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback{
            override fun onCoreInitFinished() {
                LogUtils.v("X5内核初始化成功！initX5Environment")
            }

            override fun onViewInitFinished(p0: Boolean) {
                if (!p0){
                    LogUtils.v("X5内核初始化失败！onViewInitFinished")
                }else LogUtils.v("X5内核初始化成功！onViewInitFinished")
            }

        })
    }
    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission
            .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
    fun showPermissionDenied() {
        ToastUtils.showShort("权限被拒绝，无法启动应用！")
        finish()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        hkWebChromeClient.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode,  grantResults)
    }
}
